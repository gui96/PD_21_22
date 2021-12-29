import java.io.*;
import java.net.*;
import java.util.*;

public class Principal {
    private static DatagramSocket socket;
    private static final int MAX_SIZE = 4000;

    private static int getWorkers(String fileName, List<Worker> workers){return -1;}
//    {
//        String workerCoord;
//        String workerName;
//        int workerPort;
//
//        workers.clear();
//
//        try(BufferedReader inFile = new BufferedReader(new FileReader(fileName))){ //Objecto para obter informacao sobre os workers a partir do ficheiro de texto com nome fileName
//
//            //Processa cada uma das linhas de texto do ficheiro
//            while((workerCoord = inFile.readLine())!=null){
//                workerCoord = workerCoord.trim();
//                if(workerCoord.length() == 0){
//                    continue;
//                }
//
//                try(Scanner sc = new Scanner(workerCoord)){
//
//                    //Extrai as duas primeiras palavras da String workerCoord usando uma instancia de Scanner
//
//                    workerName = sc.next();
//                    workerPort = sc.nextInt();
//
//                }catch(Exception e){
//                    System.err.print("> Entrada incorrecta no ficheiro ");
//                    System.err.println(fileName + ": \"" + workerCoord + "\"");
//
//                    //Omite as restantes instrucoes da iteraccao actual do ciclo while
//                    continue;
//                }
//
//                System.out.print("> Estabelecendo ligacao com o worker " + (workers.size()+1));
//                System.out.println(" [" + workerName + ":" + workerPort+"]... ");
//
//                try{
//                    Socket novo = new Socket(workerName,workerPort);
//                    workers.add(new Worker(novo));
//                    System.out.println("... ligacao estabelecida!");
//                }catch(UnknownHostException e){
//                    System.err.println();
//                    System.err.println("> Destino " + workerName + " desconhecido!");
//                    System.err.println(); System.err.println(e); System.err.println();
//                }catch(IOException e){
//                    System.out.println("> Impossibilidade de estabelecer ligacao!");
//                    System.err.println(); System.err.println(e); System.err.println();
//                }
//
//            } //while
//
//        }catch(FileNotFoundException e){
//            System.err.println();
//            System.err.println("Impossibilidade de abrir o ficheiro: " + fileName + "\n\t" + e);
//        }catch(IOException e){
//            System.err.println(); System.err.println(e);
//        }
//
//        return workers.size();
//    }

    public static void main(String[] args) throws UnknownHostException, SocketException {
        int tam = 0;
        List<Worker> workers = new ArrayList<>();
        List<Integer> serverPort = new ArrayList<>();
        List<Integer> clientPort = new ArrayList<>();
        int nWorkers = 0;
        int returnValue;
        socket =  new DatagramSocket(6000);

        if(args.length != 3){
            System.out.println("Sintaxe: <ficheiro com os ip e portos UDP dos workers>");
            return;
        }

        //nWorkers = getWorkers(args[0] + " " + args[1] + " " + args[2], workers);

        try {

            /* Recebe os portos para entrar em contacto com os severs e porto que te que reenviar ao cliente para fazer a lifação Server-Cliente*/
            byte[] buffer = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            socket.setSoTimeout(1000);
            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
            ObjectInputStream oss = new ObjectInputStream(baos);
            Integer port = (Integer) oss.readObject();

            /*devolver lista e passar para thread*/

            System.out.println("novo servidor:"+packet.getAddress()+":"+port);

           // serverPort.add(packet.getPort()); //para ligar ao sevidor -- Comunicação GRDS => Servidor
           // clientPort.add(port);//Para ligar o cliente -- Thread
            /*-----------------------------------------------------------------------------------------*/
            socket.close(); //Tenho que fechar o socket senão o GRDS não recebe os datagrams dos Clientes ??? (não sei a razão)
            enviaWorkers(serverPort);   //Reenvia a informação dos servidores ativos para cada um deles
            /*
            *
            *
            * */
            int BUFSIZE = 4096;
            buffer = new byte[BUFSIZE];

            packet = new DatagramPacket(buffer, BUFSIZE);

            socket.receive(packet);
            System.out.println("Packet received from " + packet.getAddress()
                    + ":" + packet.getPort() + " of length " + packet.getLength());

            // SEND TIME
            Calendar calendar = GregorianCalendar.getInstance();
            String timeMsg = calendar.get(GregorianCalendar.HOUR_OF_DAY) + ":" + calendar.get(GregorianCalendar.MINUTE) + ":" + calendar.get(GregorianCalendar.SECOND);

            packet.setData(timeMsg.getBytes());
            packet.setLength(timeMsg.length());

            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(nWorkers <= 0){
            return;
        }

        try {
            socket = new DatagramSocket(6000);  //Voltar abrir para comunicar com os clientes
            tam = 0;
            while (true){
                try{
                    //System.out.println("Cheguei aqui");
                    /* -------------------- Recebe a mensagem do cliente-----------------------*/
                    byte[] buffer = new byte[MAX_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                    ObjectInputStream oin = new ObjectInputStream(bais);

                    String msg = (String) oin.readObject();
                    System.out.println("Cliente ativos - IP: " + packet.getAddress() + " Porto: " + packet.getPort() + " Mensagem: " + msg);
                    /* --------------------------------------------------------------------*/

                    /*---------------Envia mensagem ao cliente com o porto do Server--------*/
                    if(tam>clientPort.size()-1){
                        tam = 0;
                    }
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(boas);
                    oos.writeUnshared(clientPort.get(tam));
                    byte[] buf = boas.toByteArray();
                    DatagramPacket p = new DatagramPacket(buf, buf.length,packet.getAddress(), packet.getPort());
                    socket.send(p);
                    tam++;
                    /*---------------------------------------------------------------------*/
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } finally {
            for (int i = 0; i < nWorkers; i++){
                try {
                    workers.get(i).getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void enviaWorkers(List<Integer> servers){

        try {

            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(6000);

            //Envia as coordenadas do conjunto de servidores
            for(int i = 0; i < servers.size(); i++){
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                ObjectOutputStream oss = new ObjectOutputStream(boas);

                oss.writeUnshared(servers);
                InetAddress addr= InetAddress.getByName("127.0.0.1");
                byte[] buffer = boas.toByteArray();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length,addr,servers.get(i));
                socket.send(packet);
            }
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }
    }
}

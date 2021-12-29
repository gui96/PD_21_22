import java.io.*;
import java.net.*;
import java.util.*;

public class Principal {
    private static DatagramSocket socket;
    private static final int MAX_SIZE = 4000;

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

    try {

        while(true) {
            byte[] buffer = new byte[4096];
            /* Recebe os portosTCP do servidor para entrar em contacto com os severs e porto que te que reenviar ao cliente para fazer a lifação Server-Cliente*/
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            //socket.setSoTimeout(60000);
            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
            ObjectInputStream oss = new ObjectInputStream(baos);
            Integer port = (Integer) oss.readObject();
            /*adiciona dados servidor á lista de workers*/
            System.out.println("novo servidor:" + packet.getAddress() + ":" + port + "\n A enviar lista de servidores ativos...\n Lista enviada com sucesso");
            workers.add(new Worker(packet.getAddress().toString(), packet.getPort(), port));

            /*envia a lista de workers para o servidor*/
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(boas);
            /*escreve o porto*/
            oos.writeUnshared(workers);
            oos.flush();
            /*envia packet para o GRDS, pelo ip e porto fornecidos previamente pelos argumentos de entrada*/
            byte[] buf = boas.toByteArray();
            DatagramPacket p = new DatagramPacket(buf, buf.length,
                    packet.getAddress(), packet.getPort());
            socket.send(p);

        }
    } catch (Exception e) {
        System.out.print(e);
    }





        /*if(nWorkers <= 0){
            return;
        }*/


/*
        try {
            socket = new DatagramSocket(6000);  //Voltar abrir para comunicar com os clientes
            tam = 0;
            while (true){
                try{
                    //System.out.println("Cheguei aqui");
                    */
/* -------------------- Recebe a mensagem do cliente-----------------------*//*

                    byte[] buffer = new byte[MAX_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                    ObjectInputStream oin = new ObjectInputStream(bais);

                    String msg = (String) oin.readObject();
                    System.out.println("Cliente ativos - IP: " + packet.getAddress() + " Porto: " + packet.getPort() + " Mensagem: " + msg);
                    */
/* --------------------------------------------------------------------*//*


                    */
/*---------------Envia mensagem ao cliente com o porto do Server--------*//*

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
                    */
/*---------------------------------------------------------------------*//*

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
*/

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

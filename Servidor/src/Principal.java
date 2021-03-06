import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal /*implements Runnable*/{

    private static int MAX_SIZE = 4000;
    private static final int portoUDP = 6000;

    protected Socket s;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;

    public Principal(Socket toClient){
        s = toClient;
        in = null;
        out = null;
    }

//    public void run(){
//        String str = "";
//        int returnValue;
//
//        try {
//            while (true) {
//                //ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
//                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
//
//                str = (String) in.readObject();
//                System.out.println(str);
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if(s != null) {
//                try {
//                    s.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }

    public static void main(String[] args) throws InterruptedException {
        ServerSocket s = null;
        //Socket toClient;
        Socket toClient;
        TcpServer tcpthread;
        UdpServer udpthread;
        int portToGRDS;
        int myTcpPort;
        int nCreatedThreads = 0;
        List<Integer> workersPort = new ArrayList<>();

        if(args.length != 1){
            System.out.println("Sintaxe: java DistributedPiWorker  <UDP port_optional>");
            return;
        }

        portToGRDS = Integer.parseInt(args[0]);

        tcpthread = new TcpServer();/*inicia socket TCP para cliente, atende novos clientes, por si lança outra thread para atender cada um*/
        Thread.sleep(1000);
        myTcpPort = tcpthread.getPort();
        udpthread = new UdpServer(portToGRDS, myTcpPort);/*lançar thread periodica(envia sinal do seu porto ao GRDS, e recebe lista de servidores atualizada)*/

        /*while(true){


        }*/
        /*
        try (DatagramSocket socket = new DatagramSocket()){
            s = new ServerSocket(0);
//                toClient = s.accept();
//                toClient.setSoTimeout(600000);
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(boas);

                oos.writeUnshared(tcpthread.getServerSocket().getLocalPort());
                byte[] buf = boas.toByteArray();
                DatagramPacket p = new DatagramPacket(buf, buf.length,
                        InetAddress.getByName("127.0.0.1"), 6000);
                socket.send(p);

//                byte[] buffer = new byte[MAX_SIZE];
//                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                socket.receive(packet);
//                ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
//                ObjectInputStream oss = new ObjectInputStream(baos);
//
//                workersPort = (ArrayList<Integer>) oss.readObject();
//                for (int i = 0; i < workersPort.size(); i++) {
//                    System.out.println("Servidores ativos - IP: 127.0.0.1 " + " Porto: " + workersPort.get(i));
//                }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(s!=null) {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
*/
        /*INICIA CICLO DE COMUNICAÇÃO COM CLIENTE, QUANDO RECEBE LANCA UMA THREAD PARA O CLIENTE
        while(true) {
            try (DatagramSocket socket = new DatagramSocket()) {
                s = new ServerSocket(listeningPort);
                toClient = s.accept();
                toClient.setSoTimeout(600000);

                //t = new Thread(new Principal(toClient), "Thread " + nCreatedThreads);
                //t.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (s != null) {
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/


    }
}

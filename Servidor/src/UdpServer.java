import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class UdpServer implements Runnable{
    private int portToGRDS;
    private int myTcpPort;
    private List<Worker> workers;
    private Thread thread;
    private Msg mensagemSend;
    private Msg mensagemReceive;


    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            while(true) {
                /*Envia o porto tcp(receber clientes) para o GRDS*/
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(boas);

                /*cria objecto mensagem*/
                mensagemSend = new Msg();
                mensagemSend.setOperacao("registoServer");
                mensagemSend.setDate( GregorianCalendar.getInstance());
                mensagemSend.setTcpPort(myTcpPort);
                /*escreve objecto*/
                oos.writeUnshared(mensagemSend);
                oos.flush();

                /*envia packet para o GRDS, pelo ip e porto fornecidos previamente pelos argumentos de entrada*/
                byte[] buf = boas.toByteArray();
                DatagramPacket p = new DatagramPacket(buf, buf.length,
                        InetAddress.getByName("127.0.0.1"), portToGRDS);
                socket.send(p);


                /*recebe lista de dos outros servidores como resposta do GRDS*/
                byte[] buffer = new byte[14096];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
                ObjectInputStream oss = new ObjectInputStream(baos);

                mensagemReceive = new Msg();
                mensagemReceive = (Msg) oss.readObject();
                workers = mensagemReceive.getWorkers();

                for (int i = 0; i < workers.size(); i++) {
                    System.out.println("Servidores ativos - IP: 127.0.0.1 " + " Porto: " + workers.get(i).getTcpPort());
                }
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            System.out.print(e);
        }finally{
            socket.close();
        }


    }

    public UdpServer(int portToGRDS, int myTcpPort) {
        this.mensagemSend = null;
        this.mensagemReceive = null;
        this.portToGRDS = portToGRDS;
        this.myTcpPort = myTcpPort;
        this.workers = new ArrayList<Worker>();
        thread = new Thread(this);
        thread.start();
    }
}

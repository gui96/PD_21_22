import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable{
    private int port;
    private Thread thread;
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            //ServerSocket serverSocket = new ServerSocket(0);
            serverSocket = new ServerSocket(0);
            /*lançar thread periodica*/

            /**/
            port = serverSocket.getLocalPort();
            System.out.println("o tcp está na porto:"+port);

            Socket toClient = serverSocket.accept();
            toClient.setSoTimeout(600000);
            //toClient.getInputStream()
            System.out.println("O client ligou");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // port= tcp.port;
       //logica do servidor,...  accept
    }

    public TcpServer() {
        thread = new Thread(this);
        thread.start();
    }

    public ServerSocket getServerSocket(){
        return serverSocket;
    }
}

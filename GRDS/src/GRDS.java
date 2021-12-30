import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GRDS {

    public static void main(String[] args) {
        boolean running  = true;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("GRDS starting...");
        byte[] buf = new byte[4096];
        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());

                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                buf = receivedCommand(received).getBytes();
                socket.send(new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort()));

            }catch (Exception e) {e.printStackTrace();}
        }
        socket.close();
        System.out.println("GRDS ending");
    }

    public static String receivedCommand(String command) {
        if (command.equalsIgnoreCase("connect")) {
            return "SERVER_IP + SERVER_PORT";
        }
        if (command.startsWith("sign in")) {
            return command.split(" ")[2] + " signed in";
        }
        if (command.startsWith("sign up")) {
            return command.split(" ")[2] + " registado";
        }
        if (command.startsWith("get users")) {
            return "lista de utilizadores";
        }
        if (command.startsWith("search")) {
            return "searching user " + command.split(" ")[1];
        }
        if (command.startsWith("add user")) {
            return command.split(" ")[2] + " adicionado aos contactos";
        }
        if (command.startsWith("contacts")) {
            return "lista de contactos";
        }
        if (command.startsWith("del user")) {
            return command.split(" ")[2] + " removido dos contactos";
        }
        if (command.startsWith("chat")) {
            return "grupo " + command.split(" ")[1] + " criado";
        }
        if (command.startsWith("name chat")) {
            return "Novo nome do grupo " + command.split(" ")[2];
        }
        if (command.startsWith("del chat")) {
            return "grupo eliminado";
        }
        if (command.startsWith("user chat add")) {
            return command.split(" ")[3] + " adicionado ao grupo";
        }
        if (command.startsWith("user chat del")) {
            return command.split(" ")[3] + " removido do grupo";
        }
        if (command.startsWith("quit chat")) {
            return "Saiste do grupo";
        }
        if (command.startsWith("get chats")){
            return "Lista de chats + participantes";
        }
        if (command.startsWith("msg chat")) {
            return command.split(" ")[2] + " Enviado para o grupo";
        }
        if (command.startsWith("file chat")) {
            return command.split(" ")[2] + " ficheiro enviado para o grupo";
        }
        if (command.startsWith("hist chat")) {
            return "Historico do grupo";
        }
        if (command.startsWith("del msg chat")) {
            return "Ultima mensagem eliminada";
        }
        if (command.startsWith("end")) {
            return "GRDS foi desligado";
        }
        return "";
    }
}

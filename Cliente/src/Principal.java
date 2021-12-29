import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Principal {

    public static void main(String[]args){
        String nome = "";
        Integer serverPort = -1;

        Scanner sc = new Scanner(System.in);
        int portGRDS = -1;
        InetAddress adrr = null;

        if(args.length != 2){
            System.out.println("\"Sintaxe: <IP GRDS> <Porto UDP GRDS> \"");
            return;
        }

        try {
            adrr = InetAddress.getByName(args[0]);
            portGRDS = Integer.parseInt(args[1]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try (DatagramSocket socket = new DatagramSocket()){
            /*------------------Envia mensagem ao GRDS ---------------*/
            socket.setSoTimeout(10000);
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(boas);

            oos.writeUnshared("Entrou novo cliente");

            byte[] buffer = boas.toByteArray();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,adrr,portGRDS);
            socket.send(packet);
            /*-----------------------------------------------------*/
            /*----- Recebe porto de escuta do servidor-------------*/
            byte[] buf = new byte[4000];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            socket.receive(p);
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream oin = new ObjectInputStream(bais);

            serverPort = (Integer) oin.readObject();
            System.out.println("Porto do Servidor para conexão: " + serverPort);
            /*----------------------------------------------------------------*/

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Socket toServer = new Socket(adrr,serverPort)){
            toServer.setSoTimeout(1000000);
            while (!nome.equalsIgnoreCase("fim")) {
                /* Comunicação com o servidor */
                System.out.print("Escreva aqui >> ");
                nome = sc.next();
                //ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(toServer.getOutputStream());
                out.writeUnshared(nome);
                /* ---------------------------------- */
            }
        }catch (IOException e){
            System.out.println("Exception: " + e);
        }

    }
}

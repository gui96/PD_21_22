import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Principal  {

    /** Comandos
     * connect
     * sign in [User]
     * sign up [User]
     * get users
     * search [User]
     * add user [User]
     * contacts
     * del user [User]
     * chat [Name_chat]
     * name chat [Name_chat]
     * del chat
     * user chat add [User]
     * user chat del [User]
     * quit chat
     * get chats
     * msg chat [Message]
     * file chat [File]
     * hist chat
     * del msg chat
     */

    public static void main(String[]args){

        Scanner sc = new Scanner(System.in);
        System.out.print("Comando: ");
        String command = sc.nextLine();
        Cliente cliente = new Cliente();
        while(!command.equalsIgnoreCase("exit")) {
            if (validateCommand(command)) {
                cliente.toSend(command);
                System.out.print("\nComando: ");
                command = sc.nextLine();
            } else {
                System.out.println("\nComando invalido");
                System.out.print("\nComando: ");
                command = sc.nextLine();
            }
        }
    }

    public static boolean validateCommand(String command) {
        if(command.equalsIgnoreCase("connect") ||
                command.equalsIgnoreCase("end") ||
                command.equalsIgnoreCase("exit") ||
                command.startsWith("sign in") ||
                command.startsWith("sign up") ||
                command.startsWith("get users") ||
                command.startsWith("search") ||
                command.startsWith("add user") ||
                command.startsWith("contacts") ||
                command.startsWith("del user") ||
                command.startsWith("chat") ||
                command.startsWith("name chat") ||
                command.startsWith("del chat") ||
                command.startsWith("user chat add") ||
                command.startsWith("user chat del") ||
                command.startsWith("quit chat") ||
                command.startsWith("get chats") ||
                command.startsWith("msg chat") ||
                command.startsWith("file chat") ||
                command.startsWith("hist chat") ||
                command.startsWith("del msg chat")) {
            return true;
        } else {
            return false;
        }
    }
}

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class UdpToServer implements Runnable{
    private DatagramSocket socket;
    private List<Worker> workers;
    private Thread thread;
    private Msg mensagemSend;
    private Msg mensagemReceive;

    @Override
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[4096];
                /* Recebe os portosTCP do servidor para entrar em contacto com os severs e porto que te que reenviar ao cliente para fazer a lifação Server-Cliente*/
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                //socket.setSoTimeout(60000);
                ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
                ObjectInputStream oss = new ObjectInputStream(baos);

                /*recebe mensagem*/
                mensagemReceive = new Msg();
                mensagemReceive = (Msg) oss.readObject();


                if(mensagemReceive.getOperacao().equals("registoServer")){
                    /*adiciona dados servidor á lista de workers*/
                    System.out.println("novo servidor:" + packet.getAddress() + ":" + mensagemReceive.getTcpPort() + "\n A enviar lista de servidores ativos...\n Lista enviada com sucesso");
                    if(repetidos(workers,mensagemReceive.getTcpPort(), mensagemReceive.getDate())) {
                        workers.add(new Worker(packet.getAddress().toString(), packet.getPort(), mensagemReceive.getTcpPort()));
                    }else{
                        verificaDateWorkers(workers);
                    }
                }else{

                }


                /*envia a lista de workers para o servidor*/
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(boas);
                /*escreve o porto*/
                mensagemSend = new Msg();
                mensagemSend.setWorkers(workers);
                oos.writeUnshared(mensagemSend);
                oos.flush();
                /*envia packet para o GRDS, pelo ip e porto fornecidos previamente pelos argumentos de entrada*/
                byte[] buf = boas.toByteArray();
                DatagramPacket p = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                socket.send(p);

                /*no final de cada sinal periodico ou um pedido, verifica os tempos de inactividade dos servidores*/


            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean repetidos(List<Worker> workers, int port, Calendar Date){
        for(Worker w : workers){
            if(w.getTcpPort() == port){
                w.setDate(Date);
                return false;
            }
        }
        return true;
    }

    private void verificaDateWorkers(List<Worker> workers){
        Calendar actual = GregorianCalendar.getInstance();

        for(int i=0; i < workers.size(); i++){
            if((actual.getTimeInMillis()-workers.get(i).getDate().getTimeInMillis())  > 60000){
                System.out.println("Servidor removido IP:"+workers.get(i).getAddress()+" PORT:"+workers.get(i).getTcpPort());
                workers.remove(workers.get(i));

            }
        }
    }



    public UdpToServer(DatagramSocket socket, List<Worker> workers) {
        this.mensagemSend = null;
        this.mensagemReceive = null;
        this.socket = socket;
        this.workers = workers;
        thread = new Thread(this);
        thread.start();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

}

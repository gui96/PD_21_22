import java.io.Serializable;

public class Cliente implements Serializable, TCommunication.IOnReceivedPacket {

    static final long serialVersionUID = 1L;
    private String nome;
    private String username;
    private String pass;
    private TCommunication comm;

    public Cliente() {
        comm = new TCommunication();
        comm.setReceivedPacket(this);
    }

    public Cliente(String n, String u, String p){
        this.nome = n;
        this.username = u;
        this.pass = p;
    }

    public void toSend(String command) {
        comm.sendCommand(command);
    }

    public String getNome() {
        return nome;
    }

    public String getPass() {
        return pass;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void onReceivedPacket(String packet) {
        System.out.println(packet);
    }
}

import java.io.Serializable;

public class Cliente implements Serializable {

    static final long serialVersionUID = 1L;
    private String nome;
    private String username;
    private String pass;

    public Cliente(String n, String u, String p){
        this.nome = n;
        this.username = u;
        this.pass = p;
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
}

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Msg implements Serializable {
    static final long serialVersionUID = 1L;

    private String operacao;

    /*para comunicação periodica de 20 segundos*/
    private int tcpPort;
    private Calendar Date;
    private List<Worker> workers;



    public Msg() {
        this.Date = null;
        this.workers = new ArrayList<>();
    }


    public String getOperacao() {
        return operacao;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public Calendar getDate() {
        return Date;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void setDate(Calendar date) {
        Date = date;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}

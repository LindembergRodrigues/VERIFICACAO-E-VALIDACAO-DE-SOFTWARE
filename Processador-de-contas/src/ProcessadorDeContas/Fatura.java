import java.util.Date;
import java.util.ArrayList;

public class Fatura {

    private Date dataFatura;
    private double valorTotal;
    private String nomeCliente;
    private String id;
    private String status;
    private ArrayList<Conta> contas;

    public Fatura(Date dataFatura, double valorTotal, String nomeCliente, String id) {
        this.dataFatura = dataFatura;
        this.valorTotal = valorTotal;
        this.nomeCliente = nomeCliente;
        this.id = id;
        this.status = "PENDENTE";
        contas = new ArrayList<>();
        
    }

    public void adicionaConta(Conta conta) {
        contas.add(conta);
    }

    public ArrayList<Conta> getContas() {
        return this.contas;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getData() {
        return this.dataFatura;
    }

    public double getValorTotal() {
        return this.valorTotal;
    }

    public String getNomeCliente() {
        return this.nomeCliente;
    }

    public void setData(Date dataFatura) {
        this.dataFatura = dataFatura;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}


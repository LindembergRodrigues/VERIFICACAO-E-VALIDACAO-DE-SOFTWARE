import java.time.LocalDate;

public class Fatura {

    private LocalDate dataFatura;
    private double valorTotal;
    private String nomeCliente;
    private String id;
    private String status;
    private ArrayList<Conta> contas;

    public Fatura(LocalDate dataFatura, double valorTotal, String nomeCliente, String id, String status){ {
        this.dataFatura = dataFatura;
        this.valorTotal = valorTotal;
        this.nomeCliente = nomeCliente;
        this.id = id;
        this.status = status;
        contas = new ArrayList<>();
        
    }

    

    public void adicionaConta(Conta conta) {
        contas.add(conta);
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getData() {
        return dataFatura;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setData(LocalDate dataFatura) {
        this.dataFatura = dataFatura;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
}
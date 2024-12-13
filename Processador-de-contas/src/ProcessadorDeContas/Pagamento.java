import java.time.LocalDate;
import java.util.Date;

public class Pagamento {
    private double valorPago;
    private Date dataPagamento;
    private String tipoPagamento;
    private String idConta;
    private Conta conta;

    public Pagamento(Conta conta, Date dataPagamento, String tipoPagamento) {
        this.valorPago = conta.getValorPago();
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
        this.idConta = conta.getIdFatura();
        this.conta = conta;
    }


    public void validaPagamento(){
        if (this.valorPago <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que 0.");
        }
        
        if (this.getDataPagamento().after(conta.getData())) {
            throw new IllegalArgumentException("Pagamento com atraso.");
        }


    }

    public double getValorPago() {
        return valorPago;
    }
    
    public Date getDataPagamento() {
        return dataPagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }



}

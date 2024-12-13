import java.time.LocalDate;

public class Pagamento {
    private double valorPago;
    private LocalDate dataPagamento;
    private String tipoPagamento;
    private String idConta;

    public Pagamento(Conta conta, LocalDate dataPagamento, String tipoPagamento) {
        this.valorPago = conta.getValorPago();
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
        this.idConta = conta.getIdFatura();
    }


    public void validaPagamento(){
        if (this.valorPago <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que 0.");
        }

    }

    public double getValorPago() {
        return valorPago;
    }
    
    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }



}

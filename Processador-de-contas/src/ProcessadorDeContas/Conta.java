import java.time.LocalDate;

class Conta {
    private String codigoConta;
    private LocalDate dataPagamento;
    private double valorPago;
    private String idFatura;

    public Conta(String codigoConta, LocalDate dataPagamento, double valorPago, Fatura fatura) {
        this.codigoConta = codigoConta;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
        this.idFatura = fatura.getId();
    }

    public String getCodigoConta() {
        return codigoConta;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String getIdFatura() {
        return idFatura;
    }


}

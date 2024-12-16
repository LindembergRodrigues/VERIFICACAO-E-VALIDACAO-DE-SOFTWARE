package ProcessadorDeContas;

import java.util.Date;

public class Conta {
    private String codigoConta;
    private Date data;
    private double valorPago;
    private String idFatura;

    public Conta(String codigoConta, Date data, double valorPago, Fatura fatura) {
        this.codigoConta = codigoConta;
        this.data = data;
        this.valorPago = valorPago;
        this.idFatura = fatura.getId();
    }

    public String getCodigoConta() {
        return codigoConta;
    }

    public Date getData() {
        return data;
    }

    public double getValorPago() {
        return valorPago;
    }

    public String getIdFatura() {
        return idFatura;
    }


}

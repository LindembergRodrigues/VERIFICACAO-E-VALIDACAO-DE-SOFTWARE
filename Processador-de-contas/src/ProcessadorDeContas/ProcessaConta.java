import java.util.ArrayList;
import java.util.Date;

public class ProcessaConta {

    private double valorFatura;
    private Fatura fatura;
    private ArrayList<Pagamento> pagamentos;

    public ProcessaConta(Fatura fatura) {
        this.valorFatura = fatura.getValorTotal();
        this.fatura = fatura;
        this.pagamentos = new ArrayList<>();
    }

    public void criaPagamento(Conta conta, Date dataPagamento, String tipoPagamento) {
        Pagamento pagamento = new Pagamento(conta, dataPagamento, tipoPagamento);
        pagamento.validaPagamento();
        pagamentos.add(pagamento);

    }

    public double calculaValorTotalPagamentos() {

        double valorTotal = 0;
        for (Pagamento pagamento : pagamentos) {
            valorTotal += pagamento.getValorPago();
        }
        if (valorTotal >= valorFatura) {
            this.fatura.setStatus("PAGA");
        }

        return valorTotal;

    }

}
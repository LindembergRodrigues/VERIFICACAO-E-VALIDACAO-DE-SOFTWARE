import java.util.ArrayList;
import java.util.Date;

public class ProcessaConta {

    private ArrayList<Conta> contas;

    public ProcessaConta(ArrayList<Conta> contas) {
        this.contas = contas;
    }

    public void criaPagamento(Conta conta, Date dataPagamento, String tipoPagamento) {
        Pagamento pagamento = new Pagamento(conta, dataPagamento, tipoPagamento);
    }

    public Double getValorTotalPagamentos() {

        double valorTotal = 0;

        for (Conta conta : contas) {
            valorTotal += conta.getValorPago();
        }

        return valorTotal;

    }



}
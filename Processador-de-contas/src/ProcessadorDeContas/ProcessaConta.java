import java.util.ArrayList;

public class ProcessaConta {

    private ArrayList<Conta> contas;

    public ProcessaConta(ArrayList<Conta> contas) {
        this.contas = contas
    }

    public void criaPagamento(Conta conta, LocalDate dataPagamento, String tipoPagamento) {
        Pagamento pagamento = new Pagamento(conta, dataPagamento, tipoPagamento);
    }

    public void criar


}
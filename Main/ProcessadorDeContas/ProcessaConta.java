package ProcessadorDeContas;

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
        if (conta == null || dataPagamento == null || tipoPagamento == null) {
            throw new IllegalArgumentException("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.");
        }

        Pagamento pagamento = new Pagamento(conta, dataPagamento, tipoPagamento);
        pagamento.validaPagamento();

        if ("TRANSFERENCIA_BANCARIA".equals(tipoPagamento) || "BOLETO".equals(tipoPagamento)) {
            if (validaPagamentoTransferenciaBancariaBoleto(pagamento)) {
                pagamentos.add(pagamento);
                calculaValorTotalPagamentos();
            }
        } else {
            long diffMillis = fatura.getData().getTime() - conta.getData().getTime();
            long diffDias = diffMillis / (1000 * 60 * 60 * 24);
            if (diffDias <= 16) {
                calculaValorTotalPagamentos();
                pagamentos.add(pagamento);
            }
        }
    }

    public double calculaValorTotalPagamentos() {
        double valorTotal = pagamentos.stream()
                                      .mapToDouble(Pagamento::getValorPago)
                                      .sum();

        if (valorTotal >= valorFatura) {
            fatura.setStatus("PAGA");
        }

        return valorTotal;
    }

    private boolean validaPagamentoTransferenciaBancariaBoleto(Pagamento pagamento) {
        return pagamento.getDataPagamento().before(fatura.getData()) || 
               pagamento.getDataPagamento().equals(fatura.getData());
    }
}

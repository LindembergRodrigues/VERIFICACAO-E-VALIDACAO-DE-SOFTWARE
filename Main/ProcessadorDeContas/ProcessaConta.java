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
            fatura.setStatus("PAGA");
        if (conta == null || dataPagamento == null || tipoPagamento == null) {
            throw new IllegalArgumentException("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.");
        }
        Pagamento pagamento = new Pagamento(conta, dataPagamento, tipoPagamento);
        pagamento.validaPagamento();
        
        if ("TRANSFERENCIA_BANCARIA".equals(tipoPagamento)) {
            if (validaPagamentoTransferenciaBancaria(pagamento)) {
                pagamentos.add(pagamento);
            }
        } else if ("BOLETO".equals(tipoPagamento)) {
            pagamentos.add(pagamento);  
        }
        
        else {
            long diffMillis = fatura.getData().getTime() - conta.getData().getTime();
            long diffDias = diffMillis / (1000 * 60 * 60 * 24);
            if (diffDias >= 14) {
                pagamentos.add(pagamento);
            }
        }
        
        atualizaStatusFatura();
    }
    
    public double calculaValorTotalPagamentos() {
        
        return pagamentos.stream()
                         .mapToDouble(Pagamento::getValorPago)
                         .sum();
    }

    private boolean validaPagamentoTransferenciaBancaria(Pagamento pagamento) {
        return pagamento.getDataPagamento().before(fatura.getData()) || 
               pagamento.getDataPagamento().equals(fatura.getData());
    }

    private void atualizaStatusFatura() {
        double totalPago = calculaValorTotalPagamentos();
        if (totalPago >= valorFatura) {
            fatura.setStatus("PAGA");
        } else {
            fatura.setStatus("PENDENTE");
        }
    }

}

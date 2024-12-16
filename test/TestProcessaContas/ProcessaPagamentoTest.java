package TestProcessaContas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import ProcessadorDeContas.Conta;
import ProcessadorDeContas.Fatura;
import ProcessadorDeContas.ProcessaConta;

public class ProcessaPagamentoTest {
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    private Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }


     @Test
    public void testaValorFinalProcessamentoPagamento() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 50, fatura);
        Conta conta2 = new Conta("1", subtractDays(new Date(), 15) , 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);
       
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
        processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "BOLETO");

        assertEquals("PAGA", fatura.getStatus());
        assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos());
    }

    @Test
    public void testaStatusFaturaPaga() {
        Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 50, fatura);
        Conta conta2 = new Conta("2", new Date(), 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);

        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
        processaConta.criaPagamento(conta, subtractDays(new Date(), 5), "BOLETO");
        processaConta.calculaValorTotalPagamentos();
        assertEquals("PAGA", fatura.getStatus());
        assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos());

        
    }

    @Test
    public void testaStatusFaturaPendente() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 40, fatura);
        Conta conta2 = new Conta("1", new Date(), 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);
    
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta2, addDays(new Date(), 1), "CARTAO_CREDITO");
        processaConta.criaPagamento(conta, new Date(), "BOLETO");

        assertEquals("PENDENTE", fatura.getStatus());
    }



    @Test
    public void testaPagamentoTransferenciaAtrasada(){
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);

        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

        assertEquals("PENDENTE", fatura.getStatus());
    }


    @Test
    public void testePagamentoCartaoCredito14DiasAntesDataFatura() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PENDENTE", fatura.getStatus());
    }

    @Test
    public void testePagamentoCartaoCredito15DiasAntesDataFatura() {
        Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PAGA", fatura.getStatus());
        assertEquals(100, processaConta.calculaValorTotalPagamentos());

    }

    @Test
    public void testePagamentoCartaoApos15Dias(){
        Fatura fatura = new Fatura(addDays(new Date(), 14), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PENDENTE", fatura.getStatus());
        assertEquals(0, processaConta.calculaValorTotalPagamentos());
    }


    @Test
    public void testePagamentoTransferenciaBancariaDepoisDataFatura() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

        assertEquals("PENDENTE", fatura.getStatus());

    }

    @Test
    public void testePagamentoTransferenciaBancariaAntesDataFatura() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

        assertEquals("PAGA", fatura.getStatus());
        assertEquals(100, processaConta.calculaValorTotalPagamentos());
    }
}

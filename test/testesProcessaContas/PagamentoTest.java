package testesProcessaContas;

import ProcessadorDeContas.Conta;
import ProcessadorDeContas.Fatura;
import ProcessadorDeContas.Pagamento;
import ProcessadorDeContas.ProcessaConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    @BeforeEach
    void prepareScenarios() {
    }

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    @Test
    public void testaPagamentoBoletoComAtraso() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 110.0, fatura);
        Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "BOLETO");

        assertEquals(110, pagamento.getValorPago());
    }

    @Test
    public void testaPagamentoCartaoCreditoValido() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PAGO", fatura.getStatus());
    }

    @Test
    public void testaPagamentoTransferenciaValorNegativo() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), -1, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
    }

    @Test
    public void testaPagamentoTipoInvalido() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "PIX");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("Tipo de pagamento inválido.", exception.getMessage());
    }

    @Test
    public void testaPagamentoTransferenciaValorAcimaDoLimiteBoleto() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 5001, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());
    }

    @Test
    public void testaPagamentoTransferenciaValorNoLimiteSuperiorBoleto() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 5000, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

        assertDoesNotThrow(pagamento::validaPagamento);
    }

    @Test
    public void testaPagamentoTransferenciaValorAbaixoDoLimiteBoleto() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 0.0, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
    }

    @Test
    public void testaValorFinalProcessamentoPagamento() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 50, fatura);
        Conta conta2 = new Conta("1", new Date(), 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);
        Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");
        Pagamento pagamento2 = new Pagamento(conta2, new Date(), "CARTAO_CREDITO");

        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
        processaConta.criaPagamento(conta, new Date(), "BOLETO");

        assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos());
    }

    @Test
    public void testaStatusFaturaPaga() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 50, fatura);
        Conta conta2 = new Conta("2", new Date(), 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);

        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
        processaConta.criaPagamento(conta, new Date(), "BOLETO");
        processaConta.calculaValorTotalPagamentos();
        assertEquals("PAGA", fatura.getStatus());
        
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
        Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

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
    public void testePagamentoCartaoCredito16DiasAntesDataFatura() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PAGA", fatura.getStatus());

    }

    @Test
    public void testePagamentoCartao(){
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
        
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertEquals("PAGO", fatura.getStatus());
        assertEquals(100, processaConta.calculaValorTotalPagamentos());
    }


    @Test
    public void testePagamentoTransferenciaBancariaDepoisDataFatura() {
        //Pagamento não deve ser contabilizado

    }


}

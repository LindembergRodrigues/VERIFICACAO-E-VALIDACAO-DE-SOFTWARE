package TestProcessaContas;

import ProcessadorDeContas.Conta;
import ProcessadorDeContas.Fatura;
import ProcessadorDeContas.Pagamento;
import ProcessadorDeContas.ProcessaConta;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    @Test
    public void testaPagamentoBoletoComAtraso() {
        Fatura fatura = new Fatura(addDays(new Date(), 2), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 110.0, fatura);
        Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "BOLETO");

        assertEquals("PENDENTE", fatura.getStatus());
        assertEquals(110, pagamento.getValorPago());
    }
    
    
    @Test
    public void testaPagamentoCartaoCreditoValido() {
        Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");
        assertEquals("PAGA", fatura.getStatus());
        assertEquals(100, processaConta.calculaValorTotalPagamentos());
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

   
}

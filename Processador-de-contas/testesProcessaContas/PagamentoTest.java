import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoTest {

    @BeforeEach
    void prepareScenarios() {
    }


    @Test
    public void testaPagamentoBoletoComAtraso() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now().plusDays(1), "BOLETO");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("Pagamento com atraso.", exception.getMessage());
    }

    @Test
    public void testaPagamentoCartaoCreditoValido() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "CARTAO_CREDITO");

        assertDoesNotThrow(pagamento::validaPagamento);
    }

    
    @Test
    public void testaPagamentoTransferenciaValorNegativo() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), -1, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "TRANSFERENCIA_BANCARIA");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser maior que 0.", exception.getMessage());
    }

    @Test
    public void testaPagamentoTipoInvalido() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "PIX");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("Tipo de pagamento inválido.", exception.getMessage());
    }

    @Test
    public void testaPagamentoTransferenciaValorAcimaDoLimiteBoleto() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 5001, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "BOLETO");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());
    }

    @Test
    public void testaPagamentoTransferenciaValorNoLimiteSuperiorBoleto() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 5000, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "BOLETO");
    
        assertDoesNotThrow(pagamento::validaPagamento);        
    }

    @Test
    public void testaPagamentoTransferenciaValorAbaixoDoLimiteBoleto() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 4999, fatura);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "BOLETO");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser maior que 0.", exception.getMessage());
    }
  

    @Test
    public void testaProcessamentoPagamento() {
        Fatura fatura = new Fatura(LocalDate.now(), 100.0, "João", "1");
        Conta conta = new Conta("1", LocalDate.now(), 50, fatura);
        Conta conta2 = new Conta("1", LocalDate.now(), 50, fatura);
        fatura.adicionaConta(conta);
        fatura.adicionaConta(conta2);
        Pagamento pagamento = new Pagamento(conta, LocalDate.now(), "BOLETO");
        Pagamento pagamento2 = new Pagamento(conta2, LocalDate.now(), "CARTAO_CREDITO");

        ProcessaConta processaConta = new ProcessaConta();
        processaConta.adicionaPagamento(pagamento);
        processaConta.adicionaPagamento(pagamento2);
        
        assertEquals(fatura.getValorTotal(), processaConta.getValorTotalPagamentos());
    }
}

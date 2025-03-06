package junit5Tests.TestProcessaContas;

//import static org.junit.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Testa valor final no processamento de pagamento")
void testaValorFinalProcessamentoPagamento() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 50, fatura);
    Conta conta2 = new Conta("1", subtractDays(new Date(), 15), 50, fatura);
    fatura.adicionaConta(conta);
    fatura.adicionaConta(conta2);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
    processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "BOLETO");

    assertAll("Validação do processamento de pagamento",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos())
    );
}


@Test
@DisplayName("Testa status da fatura como paga")
void testaStatusFaturaPaga() {
    Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 50, fatura);
    Conta conta2 = new Conta("2", new Date(), 50, fatura);
    fatura.adicionaConta(conta);
    fatura.adicionaConta(conta2);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta2, new Date(), "CARTAO_CREDITO");
    processaConta.criaPagamento(conta, subtractDays(new Date(), 5), "BOLETO");

    assertAll("Validação do status da fatura como paga",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos())
    );
}

@Test
@DisplayName("Testa status da fatura como pendente")
void testaStatusFaturaPendente() {
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
@DisplayName("Testa pagamento com transferência atrasada")
void testaPagamentoTransferenciaAtrasada() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

    assertEquals("PENDENTE", fatura.getStatus());
}


@Test
@DisplayName("Teste de pagamento com cartão de crédito 14 dias antes da data da fatura")
void testePagamentoCartaoCredito14DiasAntesDataFatura() {
    Fatura fatura = new Fatura(addDays(new Date(), 14), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
@DisplayName("Teste de pagamento com cartão de crédito 15 dias antes da data da fatura")
void testePagamentoCartaoCredito15DiasAntesDataFatura() {
    Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertAll("Validação do pagamento com cartão de crédito 15 dias antes da data da fatura",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(100, processaConta.calculaValorTotalPagamentos())
    );
}

@Test
@DisplayName("Teste de pagamento com cartão de crédito após 15 dias")
void testePagamentoCartaoApos15Dias() {
    Fatura fatura = new Fatura(addDays(new Date(), 14), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertAll("Validação do pagamento com cartão de crédito após 15 dias",
        () -> assertEquals("PENDENTE", fatura.getStatus()),
        () -> assertEquals(0, processaConta.calculaValorTotalPagamentos())
    );
}


@Test
@DisplayName("Teste de pagamento com transferência bancária após a data da fatura")
void testePagamentoTransferenciaBancariaDepoisDataFatura() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
@DisplayName("Teste de pagamento com transferência bancária antes da data da fatura")
void testePagamentoTransferenciaBancariaAntesDataFatura() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

    assertAll("Validação do pagamento com transferência bancária antes da data da fatura",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(100, processaConta.calculaValorTotalPagamentos())
    );
}

    // Testes utilizando partições por equivalência


    @Test
    @DisplayName("CT1 - Pagamento com transferência bancária antes da data da fatura")
    void CT1() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100, fatura);
        fatura.adicionaConta(conta);
    
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");
    
        assertAll("Validação do pagamento com transferência bancária antes da data da fatura",
            () -> assertEquals("PAGA", fatura.getStatus()),
            () -> assertEquals(100, processaConta.calculaValorTotalPagamentos())
        );
    }

@Test
@DisplayName("CT2 - Pagamento com boleto antes da data da fatura")
void CT2() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, subtractDays(new Date(), 1), "BOLETO");

    assertAll("Validação do pagamento com boleto antes da data da fatura",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(fatura.getValorTotal(), processaConta.calculaValorTotalPagamentos())
    );
}

@Test
@DisplayName("CT3 - Pagamento com cartão de crédito 14 dias antes da data da fatura")
void CT3() {
    Fatura fatura = new Fatura(addDays(new Date(), 14), 200.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 200, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertEquals("PAGA", fatura.getStatus());
}
@Test
@DisplayName("CT4 - Pagamento com cartão de crédito 12 dias antes da data da fatura")
void CT4() {
    Fatura fatura = new Fatura(new Date(), 200.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, subtractDays(new Date(), 12), "CARTAO_CREDITO");

    assertEquals("PENDENTE", fatura.getStatus());
}
@Test
@DisplayName("CT5 - Pagamento com conta nula")
void CT5() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        processaConta.criaPagamento(null, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA")
    );

    assertEquals("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.", exception.getMessage());
}

@Test
@DisplayName("CT6 - Pagamento com data de pagamento nula")
void CT6() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        processaConta.criaPagamento(conta, null, "BOLETO")
    );

    assertEquals("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.", exception.getMessage());
}


@Test
@DisplayName("CT7 - Pagamento com data de pagamento e tipo de pagamento nulos")
void CT7() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100, fatura);
    fatura.adicionaConta(conta);

    ProcessaConta processaConta = new ProcessaConta(fatura);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        processaConta.criaPagamento(conta, null, null)
    );

    assertEquals("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.", exception.getMessage());
}


@Test
@DisplayName("CT8 - Pagamento com transferência bancária após a data da fatura")
void CT8() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);

    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA");

    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
public void CT9(){
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            processaConta.criaPagamento(conta, addDays(new Date(), 1), "TRANSFERENCIA_BANCARIA"));

    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());

}

// Testes utilizando tabela de decisão


@Test
@DisplayName("CT10 - Pagamento com parâmetros nulos")
void CT10() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    ProcessaConta processaConta = new ProcessaConta(fatura);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        processaConta.criaPagamento(null, null, null)
    );

    assertEquals("Os parâmetros conta, dataPagamento e tipoPagamento não podem ser nulos.", exception.getMessage());
}

@Test
@DisplayName("CT11 - Pagamento com transferência bancária válida")
void CT11() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");

    assertEquals("PAGA", fatura.getStatus());
}

@Test
@DisplayName("CT12 - Pagamento com cartão de crédito e valor parcial")
void CT12() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 50.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertEquals("PENDENTE", fatura.getStatus());
}

@Test
@DisplayName("CT13 - Pagamento com boleto válido")
void CT13() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "BOLETO");

    assertEquals("PAGA", fatura.getStatus());
}

@Test
@DisplayName("CT14 - Pagamento com cartão de crédito 15 dias antes da data da fatura")
void CT14() {
    Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);

    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertEquals("PAGA", fatura.getStatus());
}


 @Test
@DisplayName("CT15 - Pagamento com cartão de crédito 10 dias antes da data da fatura")
void CT15() {
    Fatura fatura = new Fatura(addDays(new Date(), 10), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);

    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertEquals("PENDENTE", fatura.getStatus());
}

}


package junit5Tests.TestProcessaContas;
import ProcessadorDeContas.Conta;
import ProcessadorDeContas.Fatura;
import ProcessadorDeContas.Pagamento;
import ProcessadorDeContas.ProcessaConta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    private Fatura fatura;
    private Conta conta;

    @BeforeEach
    void setup() {
        fatura = new Fatura(new Date(), 100.0, "João", "1");
        conta = new Conta("1", new Date(), 100.0, fatura);
    }


    // private Date addDays(Date date, int days) {
    //     Calendar calendar = Calendar.getInstance();
    //     calendar.setTime(date);
    //     calendar.add(Calendar.DATE, days);
    //     return calendar.getTime();
    // }

    
    @Test
    @DisplayName("Pagamento via boleto com atraso")
    void testaPagamentoBoletoComAtraso() {
        fatura = new Fatura(addDays(new Date(), 2), 100.0, "João", "1");
        conta = new Conta("1", new Date(), 110.0, fatura);
        Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "BOLETO");

        assertAll("Validação do pagamento com atraso",
            () -> assertEquals("PENDENTE", fatura.getStatus()),
            () -> assertEquals(110, pagamento.getValorPago())
        );
    }

    @Test
    @DisplayName("Pagamento com cartão de crédito válido")
    void testaPagamentoCartaoCreditoValido() {
        fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
        conta = new Conta("1", new Date(), 100.0, fatura);
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

        assertAll("Validação do pagamento com cartão",
            () -> assertEquals("PAGA", fatura.getStatus()),
            () -> assertEquals(100, processaConta.calculaValorTotalPagamentos())
        );
    }

    @ParameterizedTest
    @CsvSource({
        "-1, O valor do pagamento deve ser maior que 0.0",
        "100, Tipo de pagamento inválido."
    })
    @DisplayName("Pagamentos inválidos")
    void testaPagamentosInvalidos(double valor, String mensagemEsperada) {
        conta = new Conta("1", new Date(), valor, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), valor < 0 ? "TRANSFERENCIA_BANCARIA" : "PIX");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals(mensagemEsperada, exception.getMessage());
    }




    @Test
    @DisplayName("Pagamento com transferência e valor negativo")
    void testaPagamentoTransferenciaValorNegativo() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), -1, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");
    
        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
    }

    @Test
    @DisplayName("Pagamento com tipo de pagamento inválido")
    void testaPagamentoTipoInvalido() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "PIX");
    
        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("Tipo de pagamento inválido.", exception.getMessage());
    }

    @Test
@DisplayName("Pagamento com valor acima do limite para boleto")
void testaPagamentoTransferenciaValorAcimaDoLimiteBoleto() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 5001, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());
}
    

@Test
@DisplayName("Pagamento com transferência e valor no limite superior para boleto")
void testaPagamentoTransferenciaValorNoLimiteSuperiorBoleto() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 5000, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    assertDoesNotThrow(pagamento::validaPagamento);
}

@Test
@DisplayName("Pagamento com transferência e valor abaixo do limite para boleto")
void testaPagamentoTransferenciaValorAbaixoDoLimiteBoleto() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
}

   

// Testes utilizando AVL(Análise de valores limites)
@Test
@DisplayName("Pagamento com transferência e valor no limite inferior para boleto")
void testaPagamentoTransferenciaValorNoLimiteInferiorBoleto() {
    Fatura fatura = new Fatura(addDays(new Date(), 1), 0.1, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.1, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "BOLETO");

    assertEquals("PAGA", fatura.getStatus());
}

@Test
@DisplayName("Pagamento com cartão de crédito no limite superior")
void testaPagamentoCartaoCreditoLimiteSuperior() {
    Fatura fatura = new Fatura(addDays(new Date(), 15), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertAll("Validação do pagamento com cartão de crédito no limite superior",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(100, processaConta.calculaValorTotalPagamentos())
    );
}


@Test
@DisplayName("Pagamento com boleto válido")
void testaPagamentoBoletoValido() {
    Fatura fatura = new Fatura(addDays(new Date(), 1), 110.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 110.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "BOLETO");

    assertEquals("PAGA", fatura.getStatus());
}

@Test
@DisplayName("Pagamento com transferência no limite inferior")
void testaPagamentoTransferenciaNoLimite() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
}


// Utilizando partição por equialência

@Test
@DisplayName("CT1 - Pagamento com cartão de crédito e valor zero")
void CT1() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "CARTAO_CREDITO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
}

@Test
@DisplayName("CT2 - Pagamento com cartão de crédito no limite inferior")
void CT2() {
    Fatura fatura = new Fatura(addDays(new Date(), 15), 0.1, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.1, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "CARTAO_CREDITO");

    assertAll("Validação do pagamento com cartão de crédito no limite inferior",
        () -> assertEquals("PAGA", fatura.getStatus()),
        () -> assertEquals(0.1, processaConta.calculaValorTotalPagamentos())
    );
}

@Test
@DisplayName("CT3 - Pagamento com transferência bancária válida")
void CT3() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");

    assertEquals("PAGA", fatura.getStatus());
}

@Test
@DisplayName("CT4 - Pagamento com boleto e valor acima do limite")
void CT4() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 5001, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());
}


@Test
@DisplayName("CT5 - Pagamento com boleto e atraso")
void CT5() {
    Fatura fatura = new Fatura(addDays(new Date(), 2), 100, "João", "1");
    Conta conta = new Conta("1", new Date(), 110.0, fatura);
    Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "BOLETO");

    assertAll("Validação do pagamento com atraso",
        () -> assertEquals("PENDENTE", fatura.getStatus()),
        () -> assertEquals(110, pagamento.getValorPago())
    );
}


@Test
public void CT6() {
    Fatura fatura = new Fatura(addDays(new Date(), 30), 100, "João", "1");
    Conta conta = new Conta("1", new Date(), 100.0, fatura);
    ProcessaConta processaConta = new ProcessaConta(fatura);
    processaConta.criaPagamento(conta, new Date(), "BOLETO");
    
    assertEquals("PAGA", fatura.getStatus());
}


@Test
    public void CT7() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        Pagamento pagamento = new Pagamento(conta, new Date(), "PIX");

        Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
        assertEquals("Tipo de pagamento inválido.", exception.getMessage());
    }


// Testes utilizando tabela de decisão

@Test
public void CT11() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", addDays(new Date(), 1), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());


}

@Test
public void CT14() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "PIX");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());


}


@Test
public void CT15() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", addDays(new Date(), 1), 0.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "CARTAO_CREDITO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser maior que 0.0", exception.getMessage());
}

@Test
public void CT17() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", addDays(new Date(), 1), 5001.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());
}


@Test
public void CT19() {
    Fatura fatura = new Fatura(addDays(new Date(), 2), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 110.0, fatura);
    Pagamento pagamento = new Pagamento(conta, addDays(new Date(), 1), "BOLETO");

    assertEquals(110, pagamento.getValorPago());
}

@Test
public void CT21() {
    Fatura fatura = new Fatura(addDays(new Date(), 2), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 5001.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "BOLETO");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("O valor do pagamento deve ser menor ou igual a 5000", exception.getMessage());

}

@Test
public void CT22() {
    Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
    Conta conta = new Conta("1", new Date(), 5001.0, fatura);
    Pagamento pagamento = new Pagamento(conta, new Date(), "PIX");

    Exception exception = assertThrows(IllegalArgumentException.class, pagamento::validaPagamento);
    assertEquals("Tipo de pagamento inválido.", exception.getMessage());

}

@Test
    public void CT23() {
        Fatura fatura = new Fatura(new Date(), 100.0, "João", "1");
        Conta conta = new Conta("1", new Date(), 100.0, fatura);
        ProcessaConta processaConta = new ProcessaConta(fatura);
        processaConta.criaPagamento(conta, new Date(), "TRANSFERENCIA_BANCARIA");
        assertEquals("PAGA", fatura.getStatus());

    }

}


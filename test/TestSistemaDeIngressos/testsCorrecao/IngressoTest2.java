package TestSistemaDeIngressos.testsCorrecao;

import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngressoTest2 {
    private Ingresso ingressoVip;
    private Ingresso ingressoNormal;
    private Ingresso ingressoMeia;

    @BeforeEach
    void setUp() {
        ingressoNormal = new Ingresso(Tipo.NORMAL, 100.0);
        ingressoVip = new Ingresso(Tipo.VIP, ingressoNormal.getPreco()*2);
        ingressoMeia = new Ingresso(Tipo.MEIA_ENTRADA, ingressoNormal.getPreco()/2);
    }

    @Test
    @DisplayName("Testar ingresso VIP com desconto com percentual igual a 0(zero)")
    void testValoresLimitesDescontoC1() {
        ingressoVip.aplicarDesconto(0.00);
        assertEquals(200.0, ingressoVip.getPreco(), "O preço deveria permanecer o mesmo.");
    }
    @Test
    @DisplayName("Testar ingresso VIP com desconto com percentual igual a 25%/válido")
    void testValoresLimitesDescontoC2() {
        ingressoVip.aplicarDesconto(0.25);
        assertEquals(150.0, ingressoVip.getPreco(), "Desconto máximo aplicado corretamente.");
    }
    @Test
    @DisplayName("Testar ingresso VIP com desconto com percentual negativo")
    void testValoresLimitesDescontoC3() {

        assertAll("Validações de descontos inválidos",
                () -> {
                    Exception exceptionNegativo = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoVip.aplicarDesconto(-0.01);
                    });
                    assertEquals("Desconto deve ser entre 0 e 25%.", exceptionNegativo.getMessage());
                },
                () -> {
                    Exception exceptionMaiorQue25 = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoVip.aplicarDesconto(26.00);
                    });
                    assertEquals("Desconto deve ser entre 0 e 25%.", exceptionMaiorQue25.getMessage());
                }
        );

        assertEquals(200.0, ingressoVip.getPreco(), "Desconto negativo não deveria ser aplicado.");
    }

    @Test
    @DisplayName("Testar ingresso MEIA com desconto com percentual igual a 10%/válido")
    void testValoresLimitesDescontoC6() {
        assertAll("Validações de descontos inválidos",
                () -> {
                    Exception exceptionNegativo = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoMeia.aplicarDesconto(-0.01);
                    });
                    assertEquals("Ingresso de meia entrada não pode ter desconto.", exceptionNegativo.getMessage());
                },
                () -> {
                    Exception exceptionMaiorQue25 = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoMeia.aplicarDesconto(26.00);
                    });
                    assertEquals("Ingresso de meia entrada não pode ter desconto.", exceptionMaiorQue25.getMessage());
                },() -> {
                    Exception exceptionMaiorQue25 = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoMeia.aplicarDesconto(10.00);
                    });
                    assertEquals("Ingresso de meia entrada não pode ter desconto.", exceptionMaiorQue25.getMessage());
                }

        );

        assertEquals(50.0, ingressoMeia.getPreco(), "Desconto máximo aplicado corretamente.");
    }

    @Test
    @DisplayName("Testar ingresso NORMAL com desconto com percentual igual a 0(zero)")
    void testValoresLimitesDescontoC9() {
        ingressoNormal.aplicarDesconto(0.00);
        assertEquals(100.0, ingressoNormal.getPreco(), "O preço deveria permanecer o mesmo.");
    }

    @Test
    @DisplayName("Testar ingresso NORMAL com desconto com percentual igual a 10%/válido")
    void testValoresLimitesDescontoC10() {
        ingressoNormal.aplicarDesconto(0.10);
        assertEquals(90.0, ingressoNormal.getPreco(), "Desconto máximo aplicado corretamente.");
    }
    @Test
    @DisplayName("Testar ingresso NORMAL com desconto com percentual negativo e maior que 25/Inválido")
    void testValoresLimitesDescontoC11() {
        assertAll("Validações de descontos inválidos",
                () -> {
                    Exception exceptionNegativo = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoNormal.aplicarDesconto(-0.01);
                    });
                    assertEquals("Desconto deve ser entre 0 e 25%.", exceptionNegativo.getMessage());
                },
                () -> {
                    Exception exceptionMaiorQue25 = assertThrows(IllegalArgumentException.class, () -> {
                        ingressoNormal.aplicarDesconto(26.00);
                    });
                    assertEquals("Desconto deve ser entre 0 e 25%.", exceptionMaiorQue25.getMessage());
                }
        );
        assertEquals(100.0, ingressoNormal.getPreco(), "Desconto acima do limite não deve ser aplicado.");
    }

    @Test
    @DisplayName("Testar desconto de 25% em ingresso VIP e 15% em ingresso NORMAL")
    void testPiorCasoC15() {
        ingressoVip.aplicarDesconto(0.25);
        ingressoVip.setVendido(true);

        ingressoNormal.aplicarDesconto(0.15);
        ingressoNormal.setVendido(true);

        assertAll("Validação de ingressos vendidos",
                () -> assertTrue(ingressoVip.isVendido(), "Ingresso VIP deveria estar vendido"),
                () -> assertTrue(ingressoNormal.isVendido(), "Ingresso Normal deveria estar vendido")
        );
    }

    @Test
    @DisplayName("Testar pior caso com vários tipos de ingressos")
    void testPiorCasoVariosIngressosC16() {
        Ingresso i1 = new Ingresso(Tipo.NORMAL, 0.01);
        Ingresso i2 = new Ingresso(Tipo.VIP, Double.MAX_VALUE);
        Ingresso i3 = new Ingresso(Tipo.MEIA_ENTRADA, Double.MIN_VALUE);


        assertAll("Validação de ingressos vendidos",
                () -> assertEquals(0.01, i1.getPreco()),
                () -> assertEquals(Double.MAX_VALUE, i2.getPreco()),
                () -> assertEquals(Double.MIN_VALUE, i3.getPreco())
        );

    }
}

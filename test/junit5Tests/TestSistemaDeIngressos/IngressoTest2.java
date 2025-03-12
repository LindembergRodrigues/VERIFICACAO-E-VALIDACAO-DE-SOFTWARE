package junit5Tests.TestSistemaDeIngressos;

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
        ingressoVip = new Ingresso(Tipo.VIP, 200.0);
        ingressoNormal = new Ingresso(Tipo.NORMAL, 100.0);
        ingressoMeia = new Ingresso(Tipo.MEIA_ENTRADA, 50.0);
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
        ingressoVip.aplicarDesconto(-0.01);
        assertEquals(200.0, ingressoVip.getPreco(), "Desconto negativo não deveria ser aplicado.");
    }
    @Test
    @DisplayName("Testar ingresso VIP com desconto com percentual igual a 26%/inválido")
    void testValoresLimitesDescontoC4() {
        ingressoVip.aplicarDesconto(0.26);
        assertEquals(200.0, ingressoVip.getPreco(), "Desconto acima do limite não deve ser aplicado.");
    }
    @Test
    @DisplayName("Testar ingresso MEIA com  desconto com percentual igual a 0(zero)")
    void testValoresLimitesDescontoC5() {
        ingressoMeia.aplicarDesconto(0.00);
        assertEquals(50.0, ingressoMeia.getPreco(), "O preço deveria permanecer o mesmo.");
    }
    @Test
    @DisplayName("Testar ingresso MEIA com desconto com percentual igual a 10%/válido")
    void testValoresLimitesDescontoC6() {
        ingressoMeia.aplicarDesconto(0.10);
        assertEquals(50.0, ingressoMeia.getPreco(), "Desconto máximo aplicado corretamente.");
    }
    @Test
    @DisplayName("Testar ingresso MEIA com desconto com percentual negativo/Inválido")
    void testValoresLimitesDescontoC7() {
        ingressoMeia.aplicarDesconto(-0.01);
        assertEquals(50.0, ingressoMeia.getPreco(), "Desconto negativo não deve ser aplicado.");
    }
    @Test
    @DisplayName("Testar ingresso MEIA com desconto com percentual igual a 26%/Inválido")
    void testValoresLimitesDescontoC8() {
        ingressoMeia.aplicarDesconto(0.26);
        assertEquals(50.0, ingressoMeia.getPreco(), "Desconto acima do limite não deve ser aplicado.");
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
    @DisplayName("Testar ingresso NORMAL com desconto com percentual negativo/Inválido")
    void testValoresLimitesDescontoC11() {
        ingressoNormal.aplicarDesconto(-0.01);
        assertEquals(100.0, ingressoNormal.getPreco(), "Desconto negativo não deve ser aplicado.");
    }
    @Test
    @DisplayName("Testar ingresso NORMAL com desconto com percentual igual a 26%/Inválido")
    void testValoresLimitesDescontoC12() {
        ingressoNormal.aplicarDesconto(0.26);
        assertEquals(100.0, ingressoNormal.getPreco(), "Desconto acima do limite não deve ser aplicado.");
    }

    @Test
    @DisplayName("Testar desconto de 10% em ingresso NORMAL")
    void testParticaoEquivalenciaC13() {
        ingressoNormal.aplicarDesconto(0.10);
        assertEquals(90.0, ingressoNormal.getPreco(), "Desconto de 10% aplicado corretamente.");
    }
    @Test
    @DisplayName("Testar desconto de 10% em ingresso MEIA/Inválido")
    void testParticaoEquivalenciaC14() {
        ingressoMeia.aplicarDesconto(0.10);
        assertEquals(50.0, ingressoMeia.getPreco(), "Ingresso meia entrada não deve receber desconto.");
    }

    @Test
    @DisplayName("Testar desconto de 25% em ingresso VIP e 15% em ingresso NORMAL")
    void testPiorCasoC15() {
        ingressoVip.aplicarDesconto(0.25);
        ingressoVip.setVendido(true);

        ingressoNormal.aplicarDesconto(0.15);
        ingressoNormal.setVendido(true);

        assertTrue(ingressoVip.isVendido());
        assertTrue(ingressoNormal.isVendido());
    }

    @Test
    @DisplayName("Testar pior caso com vários tipos de ingressos")
    void testPiorCasoVariosIngressosC16() {
        Ingresso i1 = new Ingresso(Tipo.NORMAL, 0.01);
        Ingresso i2 = new Ingresso(Tipo.VIP, Double.MAX_VALUE);
        Ingresso i3 = new Ingresso(Tipo.MEIA_ENTRADA, Double.MIN_VALUE);

        assertEquals(0.01, i1.getPreco());
        assertEquals(Double.MAX_VALUE, i2.getPreco());
        assertEquals(Double.MIN_VALUE, i3.getPreco());
    }
}

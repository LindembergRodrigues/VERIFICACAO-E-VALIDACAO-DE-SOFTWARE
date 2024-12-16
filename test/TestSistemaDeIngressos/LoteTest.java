package TestSistemaDeIngressos;

import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Tipo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngressoTest {
    @Test
    void testCriarIngresso() {
        Ingresso ingresso = new Ingresso(Tipo.VIP,200.00);
        assertEquals(1, ingresso.getId());
        assertEquals(Tipo.VIP, ingresso.getTipo());
        assertFalse(ingresso.isVendido());

        Ingresso ingressoMeia = new Ingresso(Tipo.MEIA_ENTRADA,200.00);
        assertEquals(2, ingressoMeia.getId());
        assertEquals(Tipo.MEIA_ENTRADA, ingressoMeia.getTipo());
        assertFalse(ingressoMeia.isVendido());

        Ingresso ingressoNormal = new Ingresso(Tipo.NORMAL,200.00);
        assertEquals(3, ingressoNormal.getId());
        assertEquals(Tipo.NORMAL, ingressoNormal.getTipo());
        assertFalse(ingressoNormal.isVendido());
    }

    @Test
    void testMarcarComoVendido() {
        Ingresso ingresso = new Ingresso(Tipo.NORMAL,100);
        ingresso.setVendido(true);
        assertTrue(ingresso.isVendido());
    }

    @Test
    void testMarcarComoNãoVendido() {
        Ingresso ingresso = new Ingresso(Tipo.NORMAL,100);
        assertFalse(ingresso.isVendido());
    }




}

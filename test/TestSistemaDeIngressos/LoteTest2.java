package TestSistemaDeIngressos;

import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Lote;
import SistemaDeIngressos.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoteTest2 {

    private Lote lote;

    @BeforeEach
    void setUp() {
        lote = new Lote(10, 100.0, 0.2);
    }

    @Test
    void testCriacaoLoteC1() {
        assertEquals(10, lote.getIngressos().size(), "O lote deveria ter 10 ingressos.");
    }

    @Test
    void testDistribuicaoIngressoC2() {
        long vipCount = lote.getIngressos().stream().filter(i -> i.getTipo() == Tipo.VIP).count();
        assertEquals(2, vipCount);
    }
    @Test
    void testDistribuicaoIngressoC3() {
        long normalCount = lote.getIngressos().stream().filter(i -> i.getTipo() == Tipo.NORMAL).count();
        assertEquals(7, normalCount);
    }
    @Test
    void testDistribuicaoIngressoC4() {
        long meiaCount = lote.getIngressos().stream().filter(i -> i.getTipo() == Tipo.MEIA_ENTRADA).count();
        assertEquals(1, meiaCount);

    }

    @Test
    void testAplicacaoDescontoC5() {
        for (Ingresso ingresso : lote.getIngressos()) {
            if (ingresso.getTipo() != Tipo.MEIA_ENTRADA) {
                assertTrue(ingresso.getPreco() < ingresso.getPrecoOriginal());
            } else {
                assertEquals(ingresso.getPreco(), ingresso.getPrecoOriginal());
            }
        }
    }
    @Test
    void testarCriarLoteComQuantidadeNegativaC6() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Lote(-10, 50, 0.20);
        });
    }
    @Test
    void testarCriacaoLoteComQuantidadeZeroc7() {
        Lote lote = new Lote(0, 50, 0.10);
        assertEquals(0, lote.getIngressos().size(), "Lote com quantidade zero não deve ter ingressos");
    }

    @Test
    void testarLoteComDescontoExcedenteC8() {
        Lote lote2 = new Lote(100, 50, 0.30);
        assertEquals(75, lote2.getIngressos().get(0).getPreco(), "Desconto maior que 25% deve ser ajustado para 25%");
    }

    @Test
    void testarVendaDeIngressosComDescontoMaximoC9() {
        Lote lote = new Lote(100, 50, 0.25);  // Lote com 25% de desconto
        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);  // Marca todos os ingressos como vendidos
        }
        assertEquals(100, lote.getIngressos().size(), "Todos os ingressos no lote devem ser vendidos");
    }
}

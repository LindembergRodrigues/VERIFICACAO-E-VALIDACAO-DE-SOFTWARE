package TestSistemaDeIngressos;

import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Lote;
import SistemaDeIngressos.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoteTest {

    @Test
    public void testCriacaoLoteComQuantidadeValida() {
        int quantidade = 10;
        double precoNormal = 100.0;
        double desconto = 0.2;

        Lote lote = new Lote(quantidade, precoNormal, desconto);

        assertEquals(10, lote.getIngressos().size(), "Quantidade total de ingressos incorreta.");
        assertEquals(2, contarIngressosPorTipo(lote.getIngressos(), Tipo.VIP), "Quantidade de ingressos VIP incorreta.");
        assertEquals(1, contarIngressosPorTipo(lote.getIngressos(), Tipo.MEIA_ENTRADA), "Quantidade de ingressos MEIA_ENTRADA incorreta.");
        assertEquals(7, contarIngressosPorTipo(lote.getIngressos(), Tipo.NORMAL), "Quantidade de ingressos NORMAL incorreta.");
    }

    @Test
    public void testDescontoMaximoAplicado() {
        int quantidade = 10;
        double precoNormal = 100.0;
        double desconto = 0.3; // Maior que o limite permitido

        Lote lote = new Lote(quantidade, precoNormal, desconto);
        double descontoEsperado = 0.25; // Limite de desconto

        for (Ingresso ingresso : lote.getIngressos()) {
            if (ingresso.getTipo() == Tipo.VIP || ingresso.getTipo() == Tipo.NORMAL) {
                assertEquals(ingresso.getPrecoOriginal() * (1 - descontoEsperado), ingresso.getPreco(), "Desconto aplicado incorretamente.");
            }
        }
    }

    @Test
    public void testSemDesconto() {
        int quantidade = 10;
        double precoNormal = 100.0;
        double desconto = 0.0;

        Lote lote = new Lote(quantidade, precoNormal, desconto);

        for (Ingresso ingresso : lote.getIngressos()) {
            assertEquals(ingresso.getPrecoOriginal(), ingresso.getPreco(), "Preço sem desconto incorreto.");
        }
    }

    @Test
    public void testIdsIncrementais() {
        Lote lote1 = new Lote(5, 100.0, 0.1);
        Lote lote2 = new Lote(5, 100.0, 0.1);

        assertTrue(lote1.getId() < lote2.getId(), "Os IDs dos lotes não estão incrementando corretamente.");
    }

    @Test
    public void testQuantidadeIngressosMenorQue10() {
        int quantidade = 7;
        double precoNormal = 100.0;
        double desconto = 0.2;

        Lote lote = new Lote(quantidade, precoNormal, desconto);

        assertEquals(7, lote.getIngressos().size(), "Quantidade total de ingressos incorreta.");
        assertEquals(2, contarIngressosPorTipo(lote.getIngressos(), Tipo.VIP), "Quantidade de ingressos VIP incorreta.");
        assertEquals(1, contarIngressosPorTipo(lote.getIngressos(), Tipo.MEIA_ENTRADA), "Quantidade de ingressos MEIA_ENTRADA incorreta.");
        assertEquals(4, contarIngressosPorTipo(lote.getIngressos(), Tipo.NORMAL), "Quantidade de ingressos NORMAL incorreta.");
    }

    @Test
    public void testPrecoDosIngressos() {
        int quantidade = 10;
        double precoNormal = 100.0;
        double desconto = 0.1;

        Lote lote = new Lote(quantidade, precoNormal, desconto);

        for (Ingresso ingresso : lote.getIngressos()) {
            if (ingresso.getTipo() == Tipo.VIP) {
                assertEquals(200.0 * (1 - desconto), ingresso.getPreco(), "Preço do ingresso VIP incorreto.");
            } else if (ingresso.getTipo() == Tipo.MEIA_ENTRADA) {
                assertEquals(50.0, ingresso.getPreco(), "Preço do ingresso MEIA_ENTRADA incorreto.");
            } else if (ingresso.getTipo() == Tipo.NORMAL) {
                assertEquals(100.0 * (1 - desconto), ingresso.getPreco(), "Preço do ingresso NORMAL incorreto.");
            }
        }
    }

    private int contarIngressosPorTipo(List<Ingresso> ingressos, Tipo tipo) {
        return (int) ingressos.stream().filter(ingresso -> ingresso.getTipo() == tipo).count();
    }
}

package TestSistemaDeIngressos;
import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Lote;
import SistemaDeIngressos.Show;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShowTest {

    @Test
    public void testAdicionarLote() {
        Show show = new Show("2024-12-31", "Banda X", 50000, 20000, false);
        Lote lote = new Lote(10, 100.0, 0.1);

        show.adicionarLote(lote);

        assertEquals(1, show.getLotes().size(), "Lote não foi adicionado corretamente.");
    }

    @Test
    public void testGerarRelatorioLucro() {
        Show show = new Show("2024-12-31", "Artista A", 3000, 2000, false);
        Lote lote = new Lote(10, 1000.0, 0.1);

        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }

        show.adicionarLote(lote);
        String status = show.gerarRelatorio();

        assertEquals("LUCRO", status, "Status financeiro incorreto para caso de lucro.");
    }

    @Test
    public void testGerarRelatorioPrejuizo() {
        Show show = new Show("2024-12-31", "Artista B", 100000, 50000, true);
        Lote lote = new Lote(10, 100.0, 0.1);

        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }

        show.adicionarLote(lote);
        String status = show.gerarRelatorio();

        assertEquals("PREJUÍZO", status, "Status financeiro incorreto para caso de prejuízo.");
    }

    @Test
    public void testGerarRelatorioEstavel() {
        Show show = new Show("2024-12-31", "Artista C", 2000, 1500, false);
        Lote lote = new Lote(3, 1000.0, 0.0);

        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }

        show.adicionarLote(lote);
        String status = show.gerarRelatorio();

        assertEquals("ESTÁVEL", status, "Status financeiro incorreto para caso de equilíbrio.");
    }

    @Test
    public void testRelatorioComDataEspecial() {
        Show show = new Show("2024-12-31", "Artista Especial", 50000, 20000, true);
        Lote lote = new Lote(10, 100.0, 0.2);

        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }

        show.adicionarLote(lote);
        String status = show.gerarRelatorio();

        assertNotNull(status, "Relatório gerado para data especial não deve ser nulo.");
        assertTrue(status.equals("LUCRO") || status.equals("PREJUÍZO") || status.equals("ESTÁVEL"), "Status financeiro inesperado.");
    }
}


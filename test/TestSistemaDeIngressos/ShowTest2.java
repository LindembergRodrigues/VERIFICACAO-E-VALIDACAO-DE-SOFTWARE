package TestSistemaDeIngressos;
import SistemaDeIngressos.Ingresso;
import SistemaDeIngressos.Lote;
import SistemaDeIngressos.Show;
import SistemaDeIngressos.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShowTest2 {
    @Test
    void testarRelatorioSemIngressosVendidosC1() {
        Show show = new Show("2025-02-25", "Artista X", 100000, 5000, true);
        Lote lote = new Lote(0, 50, 0.10);
        show.adicionarLote(lote);
        assertEquals("PREJUÍZO", show.gerarRelatorio(), "O show deve gerar prejuízo sem ingressos vendidos");
    }

    @Test
    void testarGerarRelatorioComIngressosVendidosC2() {
        Show show = new Show("2025-02-25", "Artista Y", 100000, 5000, true);
        Lote lote = new Lote(100, 50, 0.10);
        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }
        show.adicionarLote(lote);
        assertEquals("LUCRO", show.gerarRelatorio(), "O show deve gerar lucro após a venda dos ingressos");
    }

    @Test
    void testarMostrarStatusFinanceiroComPrejuizoC3() {
        Show show = new Show("2025-02-25", "Artista Z", 100000, 5000, true);
        Lote lote = new Lote(100, 50, 0.30);
        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }
        show.adicionarLote(lote);
        assertEquals("PREJUÍZO", show.gerarRelatorio(), "O show deve gerar prejuízo se as despesas superarem a receita");
    }

    @Test
    void testarRelatorioComDespesasInfraAltaC4() {
        Show show = new Show("2025-02-25", "Artista W", 100000, 20000, true);
        Lote lote = new Lote(500, 50, 0.10);
        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }
        show.adicionarLote(lote);
        assertEquals("PREJUÍZO", show.gerarRelatorio(), "O show deve gerar prejuízo com grandes despesas de infraestrutura");
    }

    @Test
    void testarGerarRelatorioComLucroC5() {
        Show show = new Show("2025-02-25", "Artista X", 80000, 5000, false);
        Lote lote = new Lote(300, 50, 0.15);
        for (Ingresso ingresso : lote.getIngressos()) {
            ingresso.setVendido(true);
        }
        show.adicionarLote(lote);
        assertEquals("LUCRO", show.gerarRelatorio(), "O show deve gerar lucro após a venda dos ingressos");
    }
}


package SistemaDeIngressos;

import java.lang.invoke.StringConcatException;
import java.util.ArrayList;
import java.util.List;

public class Show {
    private final String data;

    public String getArtista() {
        return artista;
    }

    private final String artista;
    private final double cache;
    private final double despesasInfra;
    private final boolean dataEspecial;

    public List<Lote> getLotes() {
        return lotes;
    }

    private final List<Lote> lotes;

    public Show(String data, String artista, double cache, double despesasInfra, boolean dataEspecial) {
        this.data = data;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfra = despesasInfra;
        this.dataEspecial = dataEspecial;
        this.lotes = new ArrayList<>();
    }

    public void adicionarLote(Lote lote) {
        this.lotes.add(lote);
    }

    public String gerarRelatorio() {
        int vendidosVIP = 0, vendidosNormal = 0, vendidosMeia = 0;
        double receita = 0;

        for (Lote lote : lotes) {
            for (Ingresso ingresso : lote.getIngressos()) {
                if (ingresso.isVendido()) {
                    receita += ingresso.getPreco();
                    switch (ingresso.getTipo()) {
                        case VIP -> vendidosVIP++;
                        case NORMAL -> vendidosNormal++;
                        case MEIA_ENTRADA -> vendidosMeia++;
                    }
                }
            }
        }

        double despesasTotais = cache + despesasInfra * (dataEspecial ? 1.15 : 1);
        double receitaLiquida = receita - despesasTotais;

        String statusFinanceiro;
        if (receitaLiquida > 0) {
            statusFinanceiro = "LUCRO";
        } else if (receitaLiquida == 0) {
            statusFinanceiro = "ESTÁVEL";
        } else {
            statusFinanceiro = "PREJUÍZO";
        }

        System.out.println("Relatório do Show:");
        System.out.println("Artista: " + artista);
        System.out.println("Ingressos vendidos (VIP): " + vendidosVIP);
        System.out.println("Ingressos vendidos (Normal): " + vendidosNormal);
        System.out.println("Ingressos vendidos (Meia Entrada): " + vendidosMeia);
        System.out.printf("Receita Líquida: R$ %.2f%n", receitaLiquida);
        System.out.println("Status Financeiro: " + statusFinanceiro);
        return statusFinanceiro;
    }
}

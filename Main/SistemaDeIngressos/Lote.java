package SistemaDeIngressos;

import java.util.ArrayList;
import java.util.List;

public class Lote {
    private static int contador = 1;
    private final int id;
    private final List<Ingresso> ingressos;
    private final double desconto;

    public Lote(int quantidade, double precoNormal, double desconto) {
        this.id = contador++;
        this.ingressos = new ArrayList<>();
        this.desconto = Math.min(desconto, 0.25);

        int vipCount = (int) Math.ceil(quantidade * 0.2);
        int meiaEntradaCount = (int) Math.ceil(quantidade * 0.1);
        int normalCount = quantidade - vipCount - meiaEntradaCount;

        for (int i = 0; i < vipCount; i++) {
            ingressos.add(new Ingresso(Tipo.VIP, precoNormal * 2));
        }
        for (int i = 0; i < meiaEntradaCount; i++) {
            ingressos.add(new Ingresso(Tipo.MEIA_ENTRADA, precoNormal / 2));
        }
        for (int i = 0; i < normalCount; i++) {
            ingressos.add(new Ingresso(Tipo.NORMAL, precoNormal));
        }

        // Aplica desconto em ingressos VIP e NORMAL
        for (Ingresso ingresso : ingressos) {
            ingresso.aplicarDesconto(this.desconto);
        }
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public int getId() {
        return id;
    }
}

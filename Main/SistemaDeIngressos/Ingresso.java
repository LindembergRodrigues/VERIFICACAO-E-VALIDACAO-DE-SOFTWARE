package SistemaDeIngressos;

public class Ingresso {

    private static int contador = 1;
    private final int id;
    private final Tipo tipo;
    private boolean status;
    private double preco;

    public Ingresso(Tipo tipo, double preco) {
        this.id = contador++;
        this.tipo = tipo;
        this.status = false;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public boolean isVendido() {
        return status;
    }

    public void setVendido(boolean vendido) {
        this.status = vendido;
    }

    public double getPreco() {
        return preco;
    }

    public void aplicarDesconto(double desconto) {
        if (tipo != Tipo.MEIA_ENTRADA && (desconto >= 0.00 || desconto <= 0.25 )) { // para teste individual
            this.preco -= this.preco * desconto;
        }
    }
}

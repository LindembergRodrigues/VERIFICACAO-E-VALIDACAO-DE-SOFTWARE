package SistemaDeIngressos;

public class Ingresso {

    private static int contador = 1;
    private final int id;
    private final Tipo tipo;
    private boolean status;
    private double preco;
    private double precoOriginal;

    public Ingresso(Tipo tipo, double preco) {
        if (preco <=0){
            throw new IllegalArgumentException("Preço do ingresso deve ser maior que zero.");
        }
        this.id = contador++;
        this.tipo = tipo;
        this.status = false;
        this.preco = preco;
        this.precoOriginal = preco;
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
        if (this.isVendido()) {
            throw new IllegalStateException("Ingresso já vendido.");
        }
        this.status = vendido;
    }

    public double getPreco() {
        return preco;
    }

    public void aplicarDesconto(double desconto) {
        if (tipo == Tipo.MEIA_ENTRADA){
            throw  new IllegalArgumentException("Ingresso de meia entrada não pode ter desconto.");
        }
        if (desconto < 0.00 || desconto > 0.25) {
            throw new IllegalArgumentException("Desconto deve ser entre 0 e 25%.");
        }

        this.preco -= this.preco * desconto;
    }

    public double getPrecoOriginal() {
        return precoOriginal;
    }
}

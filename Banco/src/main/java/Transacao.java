

public class Transacao {
    private int id;
    private String descricao;
    private double valor;
    private boolean receita; // true receita, false despesa
     
    //Construção
    public Transacao(int id, String descricao, double valor, boolean receita) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.receita = receita;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public boolean isReceita() {
        return receita;
    }

    @Override
    public String toString() {
        return "Transacao [id=" + id + ", descricao=" + descricao + ", valor=" + valor + ", receita=" + receita + "]";
    }
}

package gestao.pessoal.habito;

import java.time.LocalDate;
import java.util.UUID;

public class Meta {

    public enum Tipo { DIARIA, SEMANAL, MENSAL }

    private final UUID id;
    private final UUID usuarioId;
    private final UUID habitoId;
    private Tipo tipo;
    private String descricao;
    private LocalDate prazo;
    private boolean concluida;
    private int quantidade;
    private int habitosCompletos;
    private boolean alertaProximoFalha;

    public Meta(UUID usuarioId, UUID habitoId, Tipo tipo, String descricao, int quantidade) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da meta não pode ser vazia.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("tipo inválido");
        }

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.habitoId = habitoId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.prazo = LocalDate.now().plusDays(tipo == Tipo.SEMANAL ? 7 : tipo == Tipo.MENSAL ? 30 : 1);
        this.concluida = false;
        this.quantidade = quantidade;
        this.habitosCompletos = 0;
        this.alertaProximoFalha = false;
    }

    // --- Métodos de negócio ---
    public void concluir() {
        if (this.concluida) {
            throw new IllegalStateException("A meta já foi concluída.");
        }
        this.concluida = true;
    }

    public void atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = novaQuantidade;
    }

    public void dispararAlertaSeNecessario() {
        double percentual = (double) habitosCompletos / quantidade;

        switch (tipo) {
            case SEMANAL:
                alertaProximoFalha = percentual < 0.5; // alerta se menos de 50% completado
                break;
            case MENSAL:
                alertaProximoFalha = percentual < 0.25; // alerta se menos de 25% completado
                break;
            default:
                alertaProximoFalha = false;
        }
    }

    public boolean estaPertoDeFalhar() {
        return alertaProximoFalha;
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getHabitoId() { return habitoId; }
    public Tipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public LocalDate getPrazo() { return prazo; }
    public boolean isConcluida() { return concluida; }
    public int getQuantidade() { return quantidade; }
    public int getHabitosCompletos() { return habitosCompletos; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }
    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

}

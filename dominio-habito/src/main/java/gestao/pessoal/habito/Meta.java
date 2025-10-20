package gestao.pessoal.habito;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Meta {

    public enum Tipo { DIARIA, SEMANAL, MENSAL }

    private final UUID id;
    private final UUID usuarioId;
    private final Tipo tipo;
    private final String descricao;
    private LocalDate prazo;
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
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }

        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.descricao = descricao;
        this.prazo = LocalDate.now().plusDays(tipo == Tipo.SEMANAL ? 7 : tipo == Tipo.MENSAL ? 30 : 1);
        this.quantidade = quantidade;
        this.habitosCompletos = 0;
        this.alertaProximoFalha = false;
    }

    public void atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = novaQuantidade;
    }

    public void dispararAlertaSeNecessario() {
        double percentual = (double) habitosCompletos / quantidade;

        boolean pertoDoPrazo = false;
        if (prazo != null) {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), prazo);
            pertoDoPrazo = diasRestantes <= 2; // alerta se estiver a 2 dias ou menos do prazo
        }

        switch (tipo) {
            case SEMANAL:
                alertaProximoFalha = percentual < 0.5 || pertoDoPrazo;
                break;
            case MENSAL:
                alertaProximoFalha = percentual < 0.25 || pertoDoPrazo;
                break;
            default:
                alertaProximoFalha = false;
        }
    }

    // --- Getters e Setters ---
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }
    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
}

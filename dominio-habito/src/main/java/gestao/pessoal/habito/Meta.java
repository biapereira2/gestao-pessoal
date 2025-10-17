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
        this.prazo = LocalDate.now().plusDays(30);
        this.concluida = false;
        this.quantidade = quantidade;
    }

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

    public boolean estaPertoDeFalhar() {
        return !concluida && prazo.minusDays(3).isBefore(LocalDate.now());
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getHabitoId() { return habitoId; }
    public Tipo getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public LocalDate getPrazo() { return prazo; }
    public boolean isConcluida() { return concluida; }
    public int getQuantidade() { return quantidade; }
}

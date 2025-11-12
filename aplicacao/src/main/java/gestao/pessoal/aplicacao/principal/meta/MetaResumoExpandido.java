package gestao.pessoal.aplicacao.principal.meta;

import java.time.LocalDate;
import java.util.UUID;

public class MetaResumoExpandido {

    private UUID id;
    private UUID usuarioId;
    private String descricao;
    private int quantidade;
    private int habitosCompletos;
    private LocalDate prazo;
    private boolean alertaProximoFalha;

    public MetaResumoExpandido(UUID id, UUID usuarioId, String descricao, int quantidade,
                               int habitosCompletos, LocalDate prazo, boolean alertaProximoFalha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.habitosCompletos = habitosCompletos;
        this.prazo = prazo;
        this.alertaProximoFalha = alertaProximoFalha;
    }

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getHabitosCompletos() { return habitosCompletos; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setAlertaProximoFalha(boolean alertaProximoFalha) { this.alertaProximoFalha = alertaProximoFalha; }
}

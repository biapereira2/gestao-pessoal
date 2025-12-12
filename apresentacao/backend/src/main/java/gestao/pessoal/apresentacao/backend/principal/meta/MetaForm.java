package gestao.pessoal.apresentacao.backend.principal.meta;

import gestao.pessoal.dominio.principal.princ.meta.Meta;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MetaForm {

    @NotNull
    @Size(min = 1, max = 100)
    private String descricao;

    private Integer quantidade; // Deixamos opcional, será calculado por habitosIds.size()

    private int habitosCompletos;

    private LocalDate prazo;

    private boolean alertaProximoFalha;

    @NotNull
    private Meta.Tipo tipo;

    @NotNull
    private UUID usuarioId;

    @NotNull
    @NotEmpty(message = "A meta deve ter pelo menos um hábito associado.")
    private List<UUID> habitosIds; // NOVO

    // Getters e Setters
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public int getHabitosCompletos() { return habitosCompletos; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setAlertaProximoFalha(boolean alertaProximoFalha) { this.alertaProximoFalha = alertaProximoFalha; }

    public Meta.Tipo getTipo() { return tipo; }
    public void setTipo(Meta.Tipo tipo) { this.tipo = tipo; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }
}
package gestao.pessoal.aplicacao.principal.rotina;

import java.util.List;
import java.util.UUID;

public class RotinaResumoExpandido {

    private UUID id;
    private UUID usuarioId;
    private String nome;
    private String descricao;
    private List<UUID> habitosIds;

    public RotinaResumoExpandido(UUID id, UUID usuarioId, String nome, String descricao, List<UUID> habitosIds) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.descricao = descricao;
        this.habitosIds = habitosIds;
    }

    public RotinaResumoExpandido(){}

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }
}

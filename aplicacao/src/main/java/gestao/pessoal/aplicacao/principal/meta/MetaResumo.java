package gestao.pessoal.aplicacao.principal.meta;

import java.util.UUID;

public class MetaResumo {

    private UUID id;
    private String descricao;
    private UUID usuarioId;

    public MetaResumo(UUID id, String descricao, UUID usuarioId) {
        this.id = id;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
    }

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
}
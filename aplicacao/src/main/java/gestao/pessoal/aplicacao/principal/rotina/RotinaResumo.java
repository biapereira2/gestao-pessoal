package gestao.pessoal.aplicacao.principal.rotina;

import java.util.UUID;

public class RotinaResumo {

    private UUID id;
    private String nome;
    private UUID usuarioId;

    public RotinaResumo(UUID id, String nome, UUID usuarioId) {
        this.id = id;
        this.nome = nome;
        this.usuarioId = usuarioId;
    }

    public RotinaResumo(){}

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
}

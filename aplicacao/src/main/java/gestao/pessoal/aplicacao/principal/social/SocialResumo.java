package gestao.pessoal.aplicacao.principal.social;

import java.util.UUID;

public class SocialResumo {

    private UUID id;
    private UUID usuarioId;
    private int totalAmigos;

    public SocialResumo(UUID id, UUID usuarioId, int totalAmigos) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.totalAmigos = totalAmigos;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public int getTotalAmigos() { return totalAmigos; }
}
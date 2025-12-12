package gestao.pessoal.aplicacao.principal.social;

import java.util.Set;
import java.util.UUID;

public class SocialResumoExpandido {

    private UUID id;
    private UUID usuarioId;
    private Set<UUID> amigosIds; // Lista de IDs dos amigos

    public SocialResumoExpandido(UUID id, UUID usuarioId, Set<UUID> amigosIds) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.amigosIds = amigosIds;
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public Set<UUID> getAmigosIds() { return amigosIds; }
}
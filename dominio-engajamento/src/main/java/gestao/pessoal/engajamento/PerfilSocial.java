package gestao.pessoal.engajamento;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PerfilSocial {
    private final UUID id;
    private final UUID usuarioId;
    private final Set<UUID> amigos;

    public PerfilSocial(UUID usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário ID não pode ser nulo.");
        }
        this.id = UUID.randomUUID();
        this.usuarioId = usuarioId;
        this.amigos = new HashSet<>();
    }

    public void adicionarAmigo(UUID amigoId) {
        if (amigoId == null) {
            throw new IllegalArgumentException("ID do amigo não pode ser nulo.");
        }
        if (this.usuarioId.equals(amigoId)) {
            throw new IllegalStateException("Não é possível adicionar a si mesmo como amigo.");
        }
        if (this.amigos.contains(amigoId)) {
            throw new IllegalStateException("Este usuário já é seu amigo.");
        }
        this.amigos.add(amigoId);
    }

    public void removerAmigo(UUID amigoId) {
        if (!this.amigos.contains(amigoId)) {
            throw new IllegalStateException("Este usuário não está na sua lista de amigos.");
        }
        this.amigos.remove(amigoId);
    }

    public boolean ehAmigoDe(UUID amigoId) {
        return this.amigos.contains(amigoId);
    }

    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }

    public Set<UUID> getAmigos() {
        return Collections.unmodifiableSet(amigos);
    }
}
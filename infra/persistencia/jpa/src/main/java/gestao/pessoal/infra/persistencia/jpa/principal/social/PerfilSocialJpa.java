package gestao.pessoal.infra.persistencia.jpa.principal.social;

import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "perfil_social")
public class PerfilSocialJpa {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID usuarioId;

    // Cria uma tabela auxiliar "perfil_social_amigos" contendo os IDs dos amigos
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "perfil_social_amigos", joinColumns = @JoinColumn(name = "perfil_social_id"))
    @Column(name = "amigo_id")
    private Set<UUID> amigos = new HashSet<>();

    public PerfilSocialJpa() {}

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public Set<UUID> getAmigos() { return amigos; }
    public void setAmigos(Set<UUID> amigos) { this.amigos = amigos; }
}
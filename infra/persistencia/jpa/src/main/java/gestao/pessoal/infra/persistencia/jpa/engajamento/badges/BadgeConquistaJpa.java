package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "BADGE_CONQUISTA",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuarioId", "badgeModeloId"}))
public class BadgeConquistaJpa {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private UUID badgeModeloId;

    // getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public UUID getBadgeModeloId() { return badgeModeloId; }
    public void setBadgeModeloId(UUID badgeModeloId) { this.badgeModeloId = badgeModeloId; }
}


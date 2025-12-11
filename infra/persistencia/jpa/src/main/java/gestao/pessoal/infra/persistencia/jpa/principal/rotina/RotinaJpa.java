package gestao.pessoal.infra.persistencia.jpa.principal.rotina;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ROTINA")
public class RotinaJpa {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @ElementCollection
    @CollectionTable(name = "ROTINA_HABITOS", joinColumns = @JoinColumn(name = "rotina_id"))
    @Column(name = "habito_id")
    private List<UUID> habitosIds;

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

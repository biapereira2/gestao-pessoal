package gestao.pessoal.infra.persistencia.jpa.principal.meta;

import gestao.pessoal.dominio.principal.princ.meta.Meta;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "META")
public class MetaJpa {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    private int habitosCompletos;

    private LocalDate prazo;

    private boolean alertaProximoFalha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Meta.Tipo tipo;

    // NOVO: Mapeamento de Coleção de IDs de Hábitos (Cria a tabela de junção META_HABITOS)
    @ElementCollection
    @CollectionTable(name = "META_HABITOS", joinColumns = @JoinColumn(name = "meta_id"))
    @Column(name = "habito_id")
    private List<UUID> habitosIds;

    // Getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getHabitosCompletos() { return habitosCompletos; }
    public void setHabitosCompletos(int habitosCompletos) { this.habitosCompletos = habitosCompletos; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public boolean isAlertaProximoFalha() { return alertaProximoFalha; }
    public void setAlertaProximoFalha(boolean alertaProximoFalha) { this.alertaProximoFalha = alertaProximoFalha; }

    public Meta.Tipo getTipo() { return tipo; }
    public void setTipo(Meta.Tipo tipo) { this.tipo = tipo; }

    // Getter e Setter para habitosIds
    public List<UUID> getHabitosIds() { return habitosIds; }
    public void setHabitosIds(List<UUID> habitosIds) { this.habitosIds = habitosIds; }
}
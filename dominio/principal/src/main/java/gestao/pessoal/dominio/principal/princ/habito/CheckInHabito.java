package gestao.pessoal.dominio.principal.princ.habito;

import jakarta.persistence.Entity; // ❗ ISSO É ESSENCIAL
import jakarta.persistence.Id;     // ❗ ISSO É ESSENCIAL
import jakarta.persistence.GeneratedValue; // Recomendado para UUID
import java.time.LocalDate;
import java.util.UUID;

@Entity // <--- A anotação @Entity é obrigatória para o Hibernate gerenciar a classe.
public class CheckInHabito {

    @Id // <--- A anotação @Id é obrigatória para identificar a chave primária.
    // O Hibernate (parte do Spring Data JPA) precisa dessas anotações para mapear objetos para o banco de dados.
    private UUID id;

    private UUID habitoId;
    private UUID usuarioId;
    private LocalDate data;

    // Construtor completo
    public CheckInHabito(UUID habitoId, UUID usuarioId, LocalDate data) {
        this.id = UUID.randomUUID();
        this.habitoId = habitoId;
        this.usuarioId = usuarioId;
        this.data = data;
    }

    // Construtor vazio (obrigatório para JPA)
    public CheckInHabito() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHabitoId() {
        return habitoId;
    }

    public void setHabitoId(UUID habitoId) {
        this.habitoId = habitoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
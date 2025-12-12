package gestao.pessoal.infra.persistencia.jpa.principal.checkin;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// Define a chave primária composta
class CheckInId implements Serializable {
    private UUID habitoId;
    private LocalDate data;
    private UUID usuarioId;

    public CheckInId() {}

    // Necessário para o JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckInId checkInId = (CheckInId) o;
        return Objects.equals(habitoId, checkInId.habitoId) &&
                Objects.equals(data, checkInId.data) &&
                Objects.equals(usuarioId, checkInId.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(habitoId, data, usuarioId);
    }
}

@Entity
@Table(name = "check_in")
@IdClass(CheckInId.class) // Usando chave composta
public class CheckInJpa {

    @Id
    @Column(name = "habito_id", nullable = false)
    private UUID habitoId;

    @Id
    @Column(nullable = false)
    private LocalDate data;

    @Id
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    // Coluna opcional para o ID gerado (não faz parte da PK composta)
    @Column(name = "id")
    private UUID id;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getHabitoId() { return habitoId; }
    public void setHabitoId(UUID habitoId) { this.habitoId = habitoId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
}
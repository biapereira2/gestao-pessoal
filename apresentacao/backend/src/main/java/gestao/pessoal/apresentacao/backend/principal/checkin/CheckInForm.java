package gestao.pessoal.apresentacao.backend.principal.checkin;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class CheckInForm {

    @NotNull
    private UUID habitoId;

    @NotNull
    private LocalDate data;

    @NotNull
    private UUID usuarioId;

    // Getters e Setters
    public UUID getHabitoId() { return habitoId; }
    public void setHabitoId(UUID habitoId) { this.habitoId = habitoId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
}
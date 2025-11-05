package gestao.pessoal.habito.checkIn;

import java.time.LocalDate;
import java.util.UUID;

public class CheckIn {
    private final UUID id;
    private final UUID habitoId;
    private final UUID usuarioId;
    private final LocalDate data;

    public CheckIn(UUID habitoId, UUID usuarioId, LocalDate data) {
        this.id = UUID.randomUUID();
        this.habitoId = habitoId;
        this.usuarioId = usuarioId;
        this.data = data;
    }

    public UUID getId() { return id; }
    public UUID getHabitoId() { return habitoId; }
    public UUID getUsuarioId() { return usuarioId; }
    public LocalDate getData() { return data; }
}

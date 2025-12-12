package gestao.pessoal.aplicacao.principal.checkin;

import java.time.LocalDate;
import java.util.UUID;

public class CheckInResumo {

    private UUID id;
    private UUID habitoId;
    private LocalDate data;

    public CheckInResumo(UUID id, UUID habitoId, LocalDate data) {
        this.id = id;
        this.habitoId = habitoId;
        this.data = data;
    }

    public UUID getId() { return id; }
    public UUID getHabitoId() { return habitoId; }
    public LocalDate getData() { return data; }
}
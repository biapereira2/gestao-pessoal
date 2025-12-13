package gestao.pessoal.apresentacao.backend.principal.checkin;

import java.time.LocalDate;
import java.util.UUID;

// Usado para receber os dados necessários para o Check-in e Desmarque
public class CheckInForm {

    private UUID usuarioId;
    private LocalDate data; // Assumindo que o frontend envia a data no formato YYYY-MM-DD

    // Getters e Setters
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
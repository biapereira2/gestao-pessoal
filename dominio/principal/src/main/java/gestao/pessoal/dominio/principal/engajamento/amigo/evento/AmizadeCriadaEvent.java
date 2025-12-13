package gestao.pessoal.dominio.principal.engajamento.amigo.evento;

import java.util.UUID;

public class AmizadeCriadaEvent {
    private UUID usuarioId;
    private UUID novoAmigoId;

    public AmizadeCriadaEvent(UUID usuarioId, UUID novoAmigoId) {
        this.usuarioId = usuarioId;
        this.novoAmigoId = novoAmigoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public UUID getNovoAmigoId() {
        return novoAmigoId;
    }
    @Override
    public String toString() {
        return "AmizadeCriadaEvent{" +
                "usuarioId=" + usuarioId +
                ", novoAmigoId=" + novoAmigoId +
                '}';
    }
}

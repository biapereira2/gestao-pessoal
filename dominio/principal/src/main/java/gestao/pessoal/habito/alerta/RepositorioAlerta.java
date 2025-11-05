package gestao.pessoal.habito.alerta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioAlerta {
    void salvar(Alerta alerta);
    Optional<Alerta> buscarPorId(UUID id);
    List<Alerta> listarPorUsuario(UUID usuarioId);
    void remover(UUID id);
}
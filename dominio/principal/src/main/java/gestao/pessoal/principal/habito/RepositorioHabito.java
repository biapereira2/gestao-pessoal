package gestao.pessoal.principal.habito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioHabito {
    void salvar(Habito habito);
    Optional<Habito> buscarPorId(UUID habitoId);
    List<Habito> listarTodosPorUsuario(UUID usuarioId);
    void excluir(UUID habitoId);
}
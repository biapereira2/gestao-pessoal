package gestao.pessoal.habito.rotina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioRotina {
    void salvar(Rotina rotina);
    Optional<Rotina> buscarPorId(UUID rotinaId);
    List<Rotina> listarTodosPorUsuario(UUID usuarioId);
    void atualizar(Rotina rotina);
    void excluir(UUID rotinaId);
}

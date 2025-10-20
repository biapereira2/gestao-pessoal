package gestao.pessoal.engajamento;

import java.util.Optional;
import java.util.UUID;

public interface RepositorioProgressoUsuario {

    void salvar(ProgressoUsuario progresso);
    Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId);
    boolean existeParaUsuario(UUID usuarioId);
}
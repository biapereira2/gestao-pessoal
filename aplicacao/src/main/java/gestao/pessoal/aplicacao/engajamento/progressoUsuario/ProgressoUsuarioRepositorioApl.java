package gestao.pessoal.aplicacao.engajamento.progressoUsuario;

import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;

import java.util.Optional;
import java.util.UUID;

public interface ProgressoUsuarioRepositorioApl {

    void salvar(ProgressoUsuario progresso);

    Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId);

    Optional<ProgressoUsuarioResumo> buscarResumoPorUsuarioId(UUID usuarioId);
}

package gestao.pessoal.infra.persistencia.jpa.engajamento.progressoUsuario;

import gestao.pessoal.aplicacao.engajamento.progressoUsuario.ProgressoUsuarioRepositorioApl;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.RepositorioProgressoUsuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class RepositorioProgressoUsuarioAdapter
        implements RepositorioProgressoUsuario {

    private final ProgressoUsuarioRepositorioApl apl;

    RepositorioProgressoUsuarioAdapter(
            ProgressoUsuarioRepositorioApl apl
    ) {
        this.apl = apl;
    }

    @Override
    public void salvar(ProgressoUsuario progresso) {
        apl.salvar(progresso);
    }

    @Override
    public Optional<ProgressoUsuario> buscarPorUsuarioId(UUID usuarioId) {
        return apl.buscarPorUsuarioId(usuarioId);
    }

    @Override
    public boolean existeParaUsuario(UUID usuarioId) {
        return apl.buscarPorUsuarioId(usuarioId).isPresent();
    }
}

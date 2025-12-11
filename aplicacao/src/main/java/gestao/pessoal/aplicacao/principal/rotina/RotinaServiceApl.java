package gestao.pessoal.aplicacao.principal.rotina;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RotinaServiceApl {

    private final RotinaRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;

    public RotinaServiceApl(RotinaRepositorioApl repositorio, UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    public void criar(RotinaResumoExpandido rotina) {
        if (usuarioService.buscarPorId(rotina.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar rotina: usuário com ID " + rotina.getUsuarioId() + " não existe."
            );
        }
        repositorio.salvar(rotina);
    }

    public Optional<RotinaResumoExpandido> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<RotinaResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<RotinaResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    public void atualizar(RotinaResumoExpandido rotina) {
        repositorio.salvar(rotina);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }
}

package gestao.pessoal.aplicacao.principal.habito;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.habito.Habito;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HabitoServiceApl {

    private final HabitoRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;

    public HabitoServiceApl(HabitoRepositorioApl repositorio, UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    public void criar(Habito habito) {
        if (usuarioService.buscarPorId(habito.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado: " + habito.getUsuarioId());
        }

        repositorio.salvar(habito);
    }

    public Optional<Habito> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<HabitoResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<HabitoResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    public void atualizar(Habito habito) {
        repositorio.salvar(habito);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }
}

package gestao.pessoal.aplicacao.principal.meta;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetaServiceApl {

    private final MetaRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;

    public MetaServiceApl(MetaRepositorioApl repositorio, UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    public void criar(Meta meta) {
        if (usuarioService.buscarPorId(meta.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar meta: usuário com ID " + meta.getUsuarioId() + " não existe."
            );
        }

        repositorio.salvar(meta);
    }

    public Optional<Meta> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<MetaResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<MetaResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    public void atualizar(Meta meta) {
        repositorio.salvar(meta);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }
}

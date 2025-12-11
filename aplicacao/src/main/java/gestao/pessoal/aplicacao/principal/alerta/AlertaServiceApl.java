package gestao.pessoal.aplicacao.principal.alerta;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.aplicacao.principal.meta.MetaServiceApl;
import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AlertaServiceApl {

    private final AlertaRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;
    private final MetaServiceApl metaService;

    public AlertaServiceApl(AlertaRepositorioApl repositorio, UsuarioServiceApl usuarioService, MetaServiceApl metaService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
        this.metaService = metaService;
    }

    public void criar(Alerta alerta) {
        if (usuarioService.buscarPorId(alerta.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar alerta: usuário com ID " + alerta.getUsuarioId() + " não existe."
            );
        }

        if (metaService.buscarPorId(alerta.getMetaId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar alerta: meta com ID " + alerta.getMetaId() + " não existe."
            );
        }
        repositorio.salvar(alerta);
    }

    public Alerta editar(UUID id, int novoValor) {
        Alerta alerta = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));
        alerta.setValor(novoValor);
        repositorio.salvar(alerta);
        return alerta;
    }


    public void excluir(UUID id) {
        repositorio.remover(id);
    }

    public List<AlertaResumo> listarPorUsuario(UUID usuarioId) {
        return repositorio.listarPorUsuario(usuarioId);
    }

    public List<AlertaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }
}

package gestao.pessoal.aplicacao.principal.alerta;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.alerta.Alerta;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AlertaServiceApl {

    private final AlertaRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;

    public AlertaServiceApl(AlertaRepositorioApl repositorio, UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    public void criar(Alerta alerta) {
        if (usuarioService.buscarPorId(alerta.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar alerta: usuário com ID " + alerta.getUsuarioId() + " não existe."
            );
        }
        repositorio.salvar(alerta);
    }

    public Alerta editar(UUID id, String novoTitulo, String novaDescricao, LocalDate novaData, String novaCategoria) {
        Alerta alerta = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));

        if (novoTitulo != null && !novoTitulo.isBlank()) alerta.setTitulo(novoTitulo);
        if (novaDescricao != null) alerta.setDescricao(novaDescricao);
        if (novaData != null) alerta.setDataDisparo(novaData);
        if (novaCategoria != null && !novaCategoria.isBlank()) alerta.setCategoria(novaCategoria);

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

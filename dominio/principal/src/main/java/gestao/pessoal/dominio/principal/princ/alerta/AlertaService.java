package gestao.pessoal.dominio.principal.princ.alerta;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AlertaService {

    private final RepositorioAlerta repositorio;

    public AlertaService(RepositorioAlerta repositorio) {
        this.repositorio = repositorio;
    }

    // Criar alerta
    public void criar(UUID usuarioId, String titulo, String descricao, LocalDate dataDisparo, String categoria) {
        Alerta alerta = new Alerta(usuarioId, titulo, descricao, dataDisparo, categoria);
        repositorio.salvar(alerta);
    }

    // Editar alerta
    public void editar(UUID alertaId, String novoTitulo, String novaDescricao, LocalDate novaData, String novaCategoria) {
        Alerta alerta = repositorio.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));

        if (novoTitulo != null && !novoTitulo.isBlank()) alerta.setTitulo(novoTitulo);
        if (novaDescricao != null) alerta.setDescricao(novaDescricao);
        if (novaData != null) alerta.setDataDisparo(novaData);
        if (novaCategoria != null && !novaCategoria.isBlank()) alerta.setCategoria(novaCategoria);
    }

    // Remover alerta
    public void excluir(UUID alertaId) {
        repositorio.remover(alertaId);
    }

    // Verificar disparo
    public void verificarDisparo(UUID alertaId) {
        Alerta alerta = repositorio.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));
        if (alerta.deveDisparar()) {
            alerta.marcarComoDisparado();
            System.out.println("⏰ Lembrete: " + alerta.getTitulo() + " - " + alerta.getDescricao() + " [" + alerta.getCategoria() + "]");
        }
    }

    // Listar alertas do usuário
    public List<Alerta> listarPorUsuario(UUID usuarioId) {
        return repositorio.listarPorUsuario(usuarioId);
    }
}

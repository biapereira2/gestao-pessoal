package gestao.pessoal.dominio.principal.princ.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.observer.AlertaObserver;

import java.util.List;
import java.util.UUID;

public class AlertaService implements AlertaObserver {

    private final RepositorioAlerta repositorio;

    public AlertaService(RepositorioAlerta repositorio) {
        this.repositorio = repositorio;
    }

    // Criar alerta
    public void criar(UUID usuarioId, String titulo, String descricao, java.time.LocalDate dataDisparo, String categoria) {
        Alerta alerta = new Alerta(usuarioId, titulo, descricao, dataDisparo, categoria);
        alerta.adicionarObservador(this); // adiciona service como observador
        repositorio.salvar(alerta);
    }

    // Editar alerta
    public void editar(UUID alertaId, String novoTitulo, String novaDescricao, java.time.LocalDate novaData, String novaCategoria) {
        Alerta alerta = repositorio.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));

        if (novoTitulo != null && !novoTitulo.isBlank()) alerta.setTitulo(novoTitulo);
        if (novaDescricao != null) alerta.setDescricao(novaDescricao);
        if (novaData != null) alerta.setDataDisparo(novaData);
        if (novaCategoria != null && !novaCategoria.isBlank()) alerta.setCategoria(novaCategoria);

        alerta.adicionarObservador(this); // garante que o service observa alterações
        repositorio.salvar(alerta);
    }

    // Remover alerta
    public void excluir(UUID alertaId) {
        repositorio.remover(alertaId);
    }

    // Listar alertas
    public List<Alerta> listarPorUsuario(UUID usuarioId) {
        return repositorio.listarPorUsuario(usuarioId);
    }

    // --- Observer callback ---
    @Override
    public void alterado(Alerta alerta) {
        if (alerta.deveDisparar()) {
            alerta.marcarComoDisparado();
            System.out.println("⏰ Lembrete: " + alerta.getTitulo() + " - " + alerta.getDescricao() + " [" + alerta.getCategoria() + "]");
        }
    }

    // Verificar disparo manual (opcional)
    public void verificarDisparo(UUID alertaId) {
        Alerta alerta = repositorio.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado"));
        if (alerta.deveDisparar()) {
            alerta.marcarComoDisparado();
        }
    }
}

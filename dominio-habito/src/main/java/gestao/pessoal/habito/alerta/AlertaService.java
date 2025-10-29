package gestao.pessoal.aplicacao;

import gestao.pessoal.habito.Alerta;
import gestao.pessoal.habito.Meta;
import gestao.pessoal.habito.RepositorioAlerta;
import gestao.pessoal.habito.RepositorioMeta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AlertaService {

    private final RepositorioAlerta repositorioAlerta;
    private final RepositorioMeta repositorioMeta;

    public AlertaService(RepositorioAlerta repositorioAlerta, RepositorioMeta repositorioMeta) {
        this.repositorioAlerta = repositorioAlerta;
        this.repositorioMeta = repositorioMeta;
    }

    public void criar(UUID usuarioId, UUID metaId, Alerta.Condicao condicao, int valor, String descricao) {
        if (condicao == null)
            throw new IllegalArgumentException("Condição inválida");
        if (valor <= 0)
            throw new IllegalArgumentException("Valor inválido");
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição obrigatória");

        Optional<Meta> metaOpt = repositorioMeta.buscarPorId(metaId);
        if (metaOpt.isEmpty())
            throw new IllegalArgumentException("Meta não encontrada.");

        Alerta alerta = new Alerta(usuarioId, metaId, condicao, valor, descricao);
        repositorioAlerta.salvar(alerta);
    }

    public void editar(UUID alertaId, int novoValor) {
        Alerta alerta = repositorioAlerta.buscarPorId(alertaId).orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado."));alerta.setValor(novoValor);
    }


    public void excluir(UUID alertaId) {
        repositorioAlerta.remover(alertaId);
    }

    public void verificarDisparo(UUID alertaId, LocalDate dataLimite) {
        Alerta alerta = repositorioAlerta.buscarPorId(alertaId).orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado."));
        if (alerta.deveDisparar(dataLimite)) {
            alerta.marcarComoDisparado();
            System.out.println("🔔 Alerta disparado: " + alerta.getDescricao());
        }
    }

    public List<Alerta> listarPorUsuario(UUID usuarioId) {
        return repositorioAlerta.listarPorUsuario(usuarioId);
    }
}
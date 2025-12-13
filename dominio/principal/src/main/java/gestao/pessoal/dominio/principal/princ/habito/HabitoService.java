package gestao.pessoal.dominio.principal.princ.habito;

import java.util.List;
import java.util.UUID;

public class HabitoService {

    private final RepositorioHabito repositorioHabito;

    public HabitoService(RepositorioHabito repositorioHabito) {
        this.repositorioHabito = repositorioHabito;
    }

    public Habito criar(UUID usuarioId, String nome, String descricao, String categoria, String frequencia) {
        boolean jaExiste = repositorioHabito.listarTodosPorUsuario(usuarioId).stream()
                .anyMatch(h -> h.getNome().equalsIgnoreCase(nome));

        if (jaExiste)
            throw new IllegalStateException("O hábito já existe");

        Habito habito = new Habito(usuarioId, nome, descricao, categoria, frequencia);
        repositorioHabito.salvar(habito);
        return habito;
    }

    public void atualizar(Habito habito) {
        repositorioHabito.salvar(habito);
    }

    public void excluir(UUID id) {
        repositorioHabito.excluir(id);
    }

    public List<Habito> listarPorUsuario(UUID usuarioId) {
        return repositorioHabito.listarTodosPorUsuario(usuarioId);
    }
}

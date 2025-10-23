package gestao.pessoal.aplicacao.fake;

import gestao.pessoal.habito.Habito;
import gestao.pessoal.habito.RepositorioHabito;

import java.util.*;
import java.util.stream.Collectors;

public class FakeRepositorioHabito implements RepositorioHabito {

    private final Map<UUID, Habito> habitos = new HashMap<>();

    @Override
    public void salvar(Habito habito) {
        habitos.put(habito.getId(), habito);
    }

    @Override
    public Optional<Habito> buscarPorId(UUID habitoId) {
        return Optional.ofNullable(habitos.get(habitoId));
    }

    @Override
    public List<Habito> listarTodosPorUsuario(UUID usuarioId) {
        return habitos.values()
                .stream()
                .filter(habito -> habito.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(UUID habitoId) {
        habitos.remove(habitoId);
    }

    public void limpar() {
        this.habitos.clear();
    }

    public Optional<Habito> buscarPorNomeEUsuario(String nome, UUID usuarioId) {
        return listarTodosPorUsuario(usuarioId).stream()
                .filter(h -> h.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst();
    }
}

package gestao.pessoal.aplicacao.alerta;

import gestao.pessoal.principal.alerta.Alerta;
import gestao.pessoal.principal.alerta.RepositorioAlerta;

import java.util.*;
import java.util.stream.Collectors;

class FakeRepositorioAlerta implements RepositorioAlerta {

    private final Map<UUID, Alerta> alertas = new HashMap<>();

    @Override
    public void salvar(Alerta alerta) {
        alertas.put(alerta.getId(), alerta);
    }

    @Override
    public Optional<Alerta> buscarPorId(UUID id) {
        return Optional.ofNullable(alertas.get(id));
    }

    @Override
    public List<Alerta> listarPorUsuario(UUID usuarioId) {
        return alertas.values().stream()
                .filter(a -> a.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        alertas.remove(id);
    }

    public void limpar() {
        alertas.clear();
    }
}
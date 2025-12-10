package gestao.pessoal.jpa.aplicacao.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import gestao.pessoal.dominio.principal.engajamento.badges.RepositorioBadges;

import java.util.*;

class FakeRepositorioBadges implements RepositorioBadges {

    private final Map<UUID, Badges> modelos = new HashMap<>();
    private final Map<UUID, List<Badges>> conquistas = new HashMap<>();

    @Override
    public void salvarModelo(Badges badgeModelo) {
        modelos.put(badgeModelo.getId(), badgeModelo);
    }

    @Override
    public Optional<Badges> buscarModeloPorId(UUID id) {
        // Assume-se que o ID é o mesmo para o modelo e a conquista
        return modelos.values().stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public List<Badges> listarTodosModelos() {
        return new ArrayList<>(modelos.values());
    }

    @Override
    public void salvarConquista(Badges badgeConquistada) {
        conquistas.computeIfAbsent(badgeConquistada.getUsuarioId(), k -> new ArrayList<>()).add(badgeConquistada);
    }

    @Override
    public List<Badges> listarConquistasPorUsuario(UUID usuarioId) {
        return conquistas.getOrDefault(usuarioId, Collections.emptyList());
    }

    @Override
    public boolean usuarioConquistouBadge(UUID usuarioId, UUID badgeModeloId) {
        return conquistas.getOrDefault(usuarioId, Collections.emptyList()).stream()
                .anyMatch(c -> c.getId().equals(badgeModeloId));
    }

    public void limpar() {
        modelos.clear();
        conquistas.clear();
    }
}

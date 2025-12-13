package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import gestao.pessoal.aplicacao.engajamento.badges.BadgesRepositorioApl;
import gestao.pessoal.aplicacao.engajamento.badges.BadgeResumo;
import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import gestao.pessoal.dominio.principal.engajamento.badges.RepositorioBadges;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RepositorioBadgesAdapter implements RepositorioBadges {

    private final BadgesRepositorioApl repositorioApl;

    public RepositorioBadgesAdapter(BadgesRepositorioApl repositorioApl) {
        this.repositorioApl = repositorioApl;
    }

    // ======================================================
    // ================= MODELOS ============================
    // ======================================================

    @Override
    public void salvarModelo(Badges badgeModelo) {
        throw new UnsupportedOperationException(
                "Modelos de badges são gerenciados diretamente pela infraestrutura."
        );
    }

    @Override
    public Optional<Badges> buscarModeloPorId(UUID id) {
        return Optional.empty(); // não utilizado pelo domínio no momento
    }

    @Override
    public List<Badges> listarTodosModelos() {
        // Recupera todos os modelos disponíveis (não necessariamente conquistados)
        List<BadgeResumo> modelos = repositorioApl.listarBadgesDisponiveis(null); // passando null para pegar todos
        return modelos.stream()
                .map(resumo -> Badges.reidratarModelo(
                        resumo.getId(),
                        resumo.getNome(),
                        resumo.getDescricao(),
                        resumo.getCategoria(),
                        resumo.getValorRequerido()
                ))
                .collect(Collectors.toList());
    }

    // ======================================================
    // ================= CONQUISTAS =========================
    // ======================================================

    @Override
    public void salvarConquista(Badges badgeConquistada) {
        throw new UnsupportedOperationException(
                "Fluxo de concessão é responsabilidade da camada de aplicação."
        );
    }

    @Override
    public List<Badges> listarConquistasPorUsuario(UUID usuarioId) {
        List<BadgeResumo> resumos = repositorioApl.listarBadgesConquistadas(usuarioId);

        return resumos.stream()
                .map(resumo ->
                        Badges.reidratarConquistada(
                                resumo.getId(),
                                resumo.getNome(),
                                resumo.getDescricao(),
                                resumo.getCategoria(),
                                resumo.getValorRequerido(),
                                usuarioId
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean usuarioConquistouBadge(UUID usuarioId, UUID badgeModeloId) {
        return repositorioApl.listarBadgesConquistadas(usuarioId).stream()
                .anyMatch(resumo -> resumo.getId().equals(badgeModeloId));
    }
}

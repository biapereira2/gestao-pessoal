package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import gestao.pessoal.aplicacao.engajamento.badges.BadgeResumo;
import gestao.pessoal.aplicacao.engajamento.badges.BadgesRepositorioApl;
import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BadgesRepositorioImpl implements BadgesRepositorioApl {

    private final BadgeModeloJpaRepositorio modeloRepo;
    private final BadgeConquistaJpaRepositorio conquistaRepo;

    public BadgesRepositorioImpl(
            BadgeModeloJpaRepositorio modeloRepo,
            BadgeConquistaJpaRepositorio conquistaRepo
    ) {
        this.modeloRepo = modeloRepo;
        this.conquistaRepo = conquistaRepo;
    }

    // ======================================================
    // BADGES CONQUISTADAS
    // ======================================================
    @Override
    public List<BadgeResumo> listarBadgesConquistadas(UUID usuarioId) {

        return conquistaRepo.findByUsuarioId(usuarioId).stream()
                .map(conquista -> modeloRepo.findById(conquista.getBadgeModeloId())
                        .map(modelo -> new BadgeResumo(
                                modelo.getId(),
                                modelo.getNome(),
                                modelo.getDescricao(),
                                modelo.getCategoria(),
                                modelo.getValorRequerido(),
                                true
                        ))
                )
                .flatMap(java.util.Optional::stream)
                .collect(Collectors.toList());
    }

    // ======================================================
    // BADGES DISPONÍVEIS
    // ======================================================
    @Override
    public List<BadgeResumo> listarBadgesDisponiveis(UUID usuarioId) {

        List<UUID> conquistadasIds = conquistaRepo.findByUsuarioId(usuarioId).stream()
                .map(BadgeConquistaJpa::getBadgeModeloId)
                .toList();

        return modeloRepo.findAll().stream()
                .filter(modelo -> !conquistadasIds.contains(modelo.getId()))
                .map(modelo -> new BadgeResumo(
                        modelo.getId(),
                        modelo.getNome(),
                        modelo.getDescricao(),
                        modelo.getCategoria(),
                        modelo.getValorRequerido(),
                        false
                ))
                .collect(Collectors.toList());
    }
}

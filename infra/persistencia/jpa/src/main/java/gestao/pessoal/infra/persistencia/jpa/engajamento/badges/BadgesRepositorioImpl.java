package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import gestao.pessoal.dominio.principal.engajamento.badges.Badges;
import gestao.pessoal.dominio.principal.engajamento.badges.RepositorioBadges;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BadgesRepositorioImpl implements RepositorioBadges {

    private final BadgeModeloJpaRepositorio modeloRepo;
    private final BadgeConquistaJpaRepositorio conquistaRepo;
    private final JpaMapper mapper;

    public BadgesRepositorioImpl(BadgeModeloJpaRepositorio modeloRepo,
                                 BadgeConquistaJpaRepositorio conquistaRepo,
                                 JpaMapper mapper) {
        this.modeloRepo = modeloRepo;
        this.conquistaRepo = conquistaRepo;
        this.mapper = mapper;
    }

    // --- MODELOS ---

    @Override
    public void salvarModelo(Badges badgeModelo) {
        BadgeModeloJpa jpa = mapper.map(badgeModelo, BadgeModeloJpa.class);
        modeloRepo.save(jpa);
    }

    @Override
    public Optional<Badges> buscarModeloPorId(UUID id) {
        return modeloRepo.findById(id)
                .map(jpa -> mapper.map(jpa, Badges.class));
    }

    @Override
    public List<Badges> listarTodosModelos() {
        return modeloRepo.findAll().stream()
                .map(jpa -> mapper.map(jpa, Badges.class))
                .collect(Collectors.toList());
    }

    // --- CONQUISTAS ---

    @Override
    public void salvarConquista(Badges badgeConquistada) {
        BadgeConquistaJpa jpa = new BadgeConquistaJpa();
        jpa.setUsuarioId(badgeConquistada.getUsuarioId());
        jpa.setBadgeModeloId(badgeConquistada.getId());
        conquistaRepo.save(jpa);
    }

    @Override
    public List<Badges> listarConquistasPorUsuario(UUID usuarioId) {
        return conquistaRepo.findByUsuarioId(usuarioId).stream()
                .map(conquista -> modeloRepo.findById(conquista.getBadgeModeloId())
                        .map(modelo -> Badges.conceder(
                                mapper.map(modelo, Badges.class),
                                usuarioId))
                )
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean usuarioConquistouBadge(UUID usuarioId, UUID badgeModeloId) {
        return conquistaRepo.existsByUsuarioIdAndBadgeModeloId(usuarioId, badgeModeloId);
    }
}

package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BadgeConquistaJpaRepositorio extends JpaRepository<BadgeConquistaJpa, UUID> {

    List<BadgeConquistaJpa> findByUsuarioId(UUID usuarioId);

    boolean existsByUsuarioIdAndBadgeModeloId(UUID usuarioId, UUID badgeModeloId);
}


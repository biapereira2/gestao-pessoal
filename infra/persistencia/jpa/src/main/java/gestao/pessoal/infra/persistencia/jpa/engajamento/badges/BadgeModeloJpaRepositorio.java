package gestao.pessoal.infra.persistencia.jpa.engajamento.badges;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BadgeModeloJpaRepositorio extends JpaRepository<BadgeModeloJpa, UUID> {
}

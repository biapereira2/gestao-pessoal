package gestao.pessoal.infra.persistencia.jpa.principal.social;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PerfilSocialJpaRepositorio extends JpaRepository<PerfilSocialJpa, UUID> {
    Optional<PerfilSocialJpa> findByUsuarioId(UUID usuarioId);
}

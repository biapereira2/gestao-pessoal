package gestao.pessoal.infra.persistencia.jpa.principal.habito;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HabitoJpaRepositorio extends JpaRepository<HabitoJpa, UUID> {

    List<HabitoJpa> findByUsuarioId(UUID usuarioId);
}

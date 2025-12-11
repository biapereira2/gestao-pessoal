package gestao.pessoal.infra.persistencia.jpa.principal.rotina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RotinaJpaRepositorio extends JpaRepository<RotinaJpa, UUID> {
    List<RotinaJpa> findByUsuarioId(UUID usuarioId);
}

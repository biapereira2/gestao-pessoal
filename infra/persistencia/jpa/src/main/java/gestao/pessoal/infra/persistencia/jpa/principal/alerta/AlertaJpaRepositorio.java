package gestao.pessoal.infra.persistencia.jpa.principal.alerta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertaJpaRepositorio extends JpaRepository<AlertaJpa, UUID> {
    List<AlertaJpa> findByUsuarioId(UUID usuarioId);
}

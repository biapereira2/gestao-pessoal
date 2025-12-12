package gestao.pessoal.infra.persistencia.jpa.principal.checkin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CheckInJpaRepositorio extends JpaRepository<CheckInJpa, CheckInId> {

    Optional<CheckInJpa> findByHabitoIdAndDataAndUsuarioId(UUID habitoId, LocalDate data, UUID usuarioId);

    List<CheckInJpa> findByHabitoIdAndUsuarioIdOrderByDataAsc(UUID habitoId, UUID usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CheckInJpa c WHERE c.habitoId = :habitoId AND c.data = :data AND c.usuarioId = :usuarioId")
    void deleteByHabitoIdAndDataAndUsuarioId(@Param("habitoId") UUID habitoId, @Param("data") LocalDate data, @Param("usuarioId") UUID usuarioId);
}
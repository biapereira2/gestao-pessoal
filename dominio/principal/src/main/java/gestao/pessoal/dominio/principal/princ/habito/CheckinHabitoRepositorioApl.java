package gestao.pessoal.dominio.principal.princ.habito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface CheckinHabitoRepositorioApl extends JpaRepository<CheckInHabito, UUID> {

    // 1. Método gerado automaticamente (DEVE estar funcionando, pois a validação é implícita)
    boolean existsByHabitoIdAndUsuarioIdAndData(UUID habitoId, UUID usuarioId, LocalDate data);

    // 2. Método com @Query: CORRIGIR O NOME DA ENTIDADE DE CHECKINHABITO PARA CHECKINHABITO
    //                                                    ^ (Maiúscula)
    @Query("SELECT c FROM CheckInHabito c WHERE c.habitoId = :hId AND c.usuarioId = :uId AND c.data = :data")
    Optional<CheckInHabito> findByHabitoIdAndUsuarioIdAndData(
            @Param("hId") UUID habitoId,
            @Param("uId") UUID usuarioId,
            @Param("data") LocalDate data
    );
}
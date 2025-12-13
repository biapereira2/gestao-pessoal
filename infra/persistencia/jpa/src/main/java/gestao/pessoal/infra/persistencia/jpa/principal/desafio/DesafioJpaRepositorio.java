package gestao.pessoal.infra.persistencia.jpa.principal.desafio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DesafioJpaRepositorio extends JpaRepository<DesafioJpa, UUID> {

    // Método para buscar desafios em que um usuário é participante
    List<DesafioJpa> findByParticipantesIdsContaining(UUID participanteId);
}
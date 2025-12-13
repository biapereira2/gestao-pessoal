package gestao.pessoal.infra.persistencia.jpa.principal.desafio;

import org.springframework.data.jpa.repository.JpaRepository;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio.StatusConvite;
import java.util.List;
import java.util.UUID;

public interface ConviteJpaRepositorio extends JpaRepository<ConviteDesafioJpa, UUID> {

    // Método para buscar convites pendentes para um usuário específico
    List<ConviteDesafioJpa> findByConvidadoIdAndStatus(UUID convidadoId, StatusConvite status);
}
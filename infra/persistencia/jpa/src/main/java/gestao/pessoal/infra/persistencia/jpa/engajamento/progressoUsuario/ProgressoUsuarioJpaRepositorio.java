package gestao.pessoal.infra.persistencia.jpa.engajamento.progressoUsuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgressoUsuarioJpaRepositorio
        extends JpaRepository<ProgressoUsuarioJpa, UUID> {
}


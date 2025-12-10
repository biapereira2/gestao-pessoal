package gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioJpaRepositorio extends JpaRepository<UsuarioJpa, UUID> {
    Optional<UsuarioJpa> findByEmail(String email);
    boolean existsByEmail(String email);
}

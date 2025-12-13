package gestao.pessoal.infra.persistencia.jpa.principal.meta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MetaJpaRepositorio extends JpaRepository<MetaJpa, UUID> {

    List<MetaJpa> findByUsuarioId(UUID usuarioId);

    /**
     * Busca entidades MetaJpa de um usuário onde a lista 'habitosIds' contém o habitoId fornecido.
     * Usa JPQL com 'MEMBER OF' para buscar dentro da lista de IDs.
     */
    @Query("SELECT m FROM MetaJpa m WHERE m.usuarioId = :usuarioId AND :habitoId MEMBER OF m.habitosIds")
    List<MetaJpa> findByUsuarioIdAndHabitosIdsContaining(
            @Param("usuarioId") UUID usuarioId,
            @Param("habitoId") UUID habitoId
    );
}
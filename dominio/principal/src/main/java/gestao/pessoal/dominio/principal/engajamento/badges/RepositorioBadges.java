package gestao.pessoal.dominio.principal.engajamento.badges;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioBadges {

    // Métodos para persistir modelos de Badges (as badges que existem no sistema)
    void salvarModelo(Badges badgeModelo);
    Optional<Badges> buscarModeloPorId(UUID id);
    List<Badges> listarTodosModelos();

    // Métodos para persistir as Conquistas do Usuário
    void salvarConquista(Badges badgeConquistada);
    List<Badges> listarConquistasPorUsuario(UUID usuarioId);

    // Verifica se o usuário já conquistou esta badge (para evitar duplicidade)
    boolean usuarioConquistouBadge(UUID usuarioId, UUID badgeModeloId);
}
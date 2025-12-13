package gestao.pessoal.aplicacao.engajamento.badges;

import java.util.List;
import java.util.UUID;

public interface BadgesRepositorioApl {

    List<BadgeResumo> listarBadgesDisponiveis(UUID usuarioId);

    List<BadgeResumo> listarBadgesConquistadas(UUID usuarioId);
}


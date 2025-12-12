package gestao.pessoal.aplicacao.principal.social;

import java.util.Optional;
import java.util.UUID;

public interface SocialRepositorioApl {
    Optional<SocialResumo> buscarResumoPorUsuario(UUID usuarioId);
    Optional<SocialResumoExpandido> buscarResumoExpandidoPorUsuario(UUID usuarioId);
}
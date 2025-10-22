package gestao.pessoal.engajamento;

import java.util.Optional;
import java.util.UUID;

public interface RepositorioPerfilSocial {
    void salvar(PerfilSocial perfil);
    Optional<PerfilSocial> buscarPorUsuarioId(UUID usuarioId);
}
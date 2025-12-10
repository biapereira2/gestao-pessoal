package gestao.pessoal.dominio.principal.engajamento.perfilSocial;

import java.util.Optional;
import java.util.UUID;

public interface RepositorioPerfilSocial {
    void salvar(PerfilSocial perfil);
    Optional<PerfilSocial> buscarPorUsuarioId(UUID usuarioId);
}
package gestao.pessoal.jpa.aplicacao.perfilSocial;

import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeRepositorioPerfilSocial implements RepositorioPerfilSocial {
    private final Map<UUID, PerfilSocial> perfis = new HashMap<>();
    @Override public void salvar(PerfilSocial perfil) { perfis.put(perfil.getUsuarioId(), perfil); }
    @Override public Optional<PerfilSocial> buscarPorUsuarioId(UUID usuarioId) { return Optional.ofNullable(perfis.get(usuarioId)); }
}

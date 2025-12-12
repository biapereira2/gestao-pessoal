package gestao.pessoal.infra.persistencia.jpa.principal.social;

import gestao.pessoal.aplicacao.principal.social.SocialRepositorioApl;
import gestao.pessoal.aplicacao.principal.social.SocialResumo;
import gestao.pessoal.aplicacao.principal.social.SocialResumoExpandido;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.RepositorioPerfilSocial;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PerfilSocialRepositorioImpl implements RepositorioPerfilSocial, SocialRepositorioApl {

    private final PerfilSocialJpaRepositorio repo;
    private final JpaMapper mapper;

    public PerfilSocialRepositorioImpl(PerfilSocialJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(PerfilSocial perfil) {
        repo.save(mapper.map(perfil, PerfilSocialJpa.class));
    }

    @Override
    public Optional<PerfilSocial> buscarPorUsuarioId(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .map(jpa -> mapper.map(jpa, PerfilSocial.class));
    }

    @Override
    public Optional<SocialResumo> buscarResumoPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .map(jpa -> new SocialResumo(jpa.getId(), jpa.getUsuarioId(), jpa.getAmigos().size()));
    }

    @Override
    public Optional<SocialResumoExpandido> buscarResumoExpandidoPorUsuario(UUID usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .map(jpa -> new SocialResumoExpandido(jpa.getId(), jpa.getUsuarioId(), jpa.getAmigos()));
    }
}
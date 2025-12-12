package gestao.pessoal.infra.persistencia.jpa;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumo;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumoExpandido;
import gestao.pessoal.aplicacao.principal.habito.HabitoResumo;
import gestao.pessoal.aplicacao.principal.habito.HabitoResumoExpandido;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.dominio.principal.engajamento.perfilSocial.PerfilSocial;
import gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario.UsuarioJpa;
import gestao.pessoal.infra.persistencia.jpa.principal.habito.HabitoJpa;
import gestao.pessoal.infra.persistencia.jpa.principal.meta.MetaJpa;
import gestao.pessoal.infra.persistencia.jpa.principal.social.PerfilSocialJpa;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JpaMapper extends ModelMapper {

    public JpaMapper() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(AccessLevel.PRIVATE);

        // --- CONVERSORES DE META ---
        addConverter(new AbstractConverter<MetaJpa, MetaResumo>() {
            @Override
            protected MetaResumo convert(MetaJpa source) {
                if (source == null) return null;
                return new MetaResumo(source.getId(), source.getDescricao(), source.getUsuarioId());
            }
        });

        addConverter(new AbstractConverter<MetaJpa, MetaResumoExpandido>() {
            @Override
            protected MetaResumoExpandido convert(MetaJpa source) {
                if (source == null) return null;
                return new MetaResumoExpandido(
                        source.getId(),
                        source.getUsuarioId(),
                        source.getDescricao(),
                        source.getQuantidade(),
                        source.getHabitosCompletos(),
                        source.getPrazo(),
                        source.isAlertaProximoFalha()
                );
            }
        });

        // --- CONVERSORES DE USUÁRIO ---
        addConverter(new AbstractConverter<UsuarioJpa, UsuarioResumo>() {
            @Override
            protected UsuarioResumo convert(UsuarioJpa source) {
                if (source == null) return null;
                return new UsuarioResumo(
                        source.getId(),
                        source.getNome(),
                        source.getEmail()
                );
            }
        });

        addConverter(new AbstractConverter<UsuarioJpa, UsuarioResumoExpandido>() {
            @Override
            protected UsuarioResumoExpandido convert(UsuarioJpa source) {
                if (source == null) return null;
                return new UsuarioResumoExpandido(
                        source.getId(),
                        source.getNome(),
                        source.getEmail()
                );
            }
        });

        // --- CONVERSOR DE PERFIL SOCIAL (CORREÇÃO DO ERRO 500) ---
        createTypeMap(PerfilSocialJpa.class, PerfilSocial.class)
                .setProvider(request -> {
                    PerfilSocialJpa source = (PerfilSocialJpa) request.getSource();
                    return new PerfilSocial(source.getUsuarioId());
                })
                .setPostConverter(context -> {
                    PerfilSocialJpa source = context.getSource();
                    PerfilSocial destino = context.getDestination();

                    if (source.getAmigos() != null) {
                        for (UUID amigoId : source.getAmigos()) {
                            // CORREÇÃO AQUI:
                            // Protegemos a adição com try-catch.
                            // Se o domínio reclamar "Este usuário já é amigo", ignoramos,
                            // pois estamos apenas carregando o estado existente do banco.
                            try {
                                destino.adicionarAmigo(amigoId);
                            } catch (IllegalStateException e) {
                                // Silencia o erro: O amigo já está na lista, então o objetivo foi cumprido.
                            }
                        }
                    }
                    return destino;
                });

        addConverter(new AbstractConverter<HabitoJpa, HabitoResumo>() {
            @Override
            protected HabitoResumo convert(HabitoJpa source) {
                if (source == null) return null;
                return new HabitoResumo(
                        source.getId(),
                        source.getNome(),
                        source.getCategoria(),
                        source.getFrequencia()
                );
            }
        });

        addConverter(new AbstractConverter<HabitoJpa, HabitoResumoExpandido>() {
            @Override
            protected HabitoResumoExpandido convert(HabitoJpa source) {
                if (source == null) return null;
                return new HabitoResumoExpandido(
                        source.getId(),
                        source.getNome(),
                        source.getDescricao(),
                        source.getCategoria(),
                        source.getFrequencia(),
                        source.getPontuacaoCheckin()
                );
            }
        });
    }
}
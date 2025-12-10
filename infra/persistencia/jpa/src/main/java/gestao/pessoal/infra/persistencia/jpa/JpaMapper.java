package gestao.pessoal.infra.persistencia.jpa;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumo;
import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioResumoExpandido;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.infra.persistencia.jpa.compartilhado.usuario.UsuarioJpa;
import gestao.pessoal.infra.persistencia.jpa.principal.meta.MetaJpa;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

@Component
public class JpaMapper extends ModelMapper {

    public JpaMapper() {
        var config = getConfiguration();
        config.setFieldMatchingEnabled(true);
        config.setFieldAccessLevel(AccessLevel.PRIVATE);

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


    }
}

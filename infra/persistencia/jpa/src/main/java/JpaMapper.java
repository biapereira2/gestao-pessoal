import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
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
    }
}

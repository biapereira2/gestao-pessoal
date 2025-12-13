package gestao.pessoal.infra.persistencia.jpa.principal.meta;

import gestao.pessoal.aplicacao.principal.meta.MetaRepositorioApl;
import gestao.pessoal.aplicacao.principal.meta.MetaResumo;
import gestao.pessoal.aplicacao.principal.meta.MetaResumoExpandido;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import gestao.pessoal.dominio.principal.princ.meta.RepositorioMeta;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RepositorioMetaAdapter implements RepositorioMeta {

    private final MetaRepositorioApl repositorioApl;

    public RepositorioMetaAdapter(MetaRepositorioApl repositorioApl) {
        this.repositorioApl = repositorioApl;
    }

    // ======================
    // Escrita
    // ======================

    @Override
    public void salvar(Meta meta) {
        repositorioApl.salvar(meta);
    }

    @Override
    public void remover(UUID id) {
        repositorioApl.remover(id);
    }

    // ======================
    // Leitura
    // ======================

    @Override
    public Optional<Meta> buscarPorId(UUID id) {
        return repositorioApl.buscarPorId(id);
    }

    @Override
    public List<Meta> listarPorUsuario(UUID usuarioId) {
        List<MetaResumoExpandido> resumos = repositorioApl.listarResumosExpandidoPorUsuario(usuarioId);

        return resumos.stream()
                .map(this::converterResumoParaMeta)
                .collect(Collectors.toList());
    }

    // ======================
    // Conversão (regra do adapter)
    // ======================

    private Meta converterResumoParaMeta(MetaResumoExpandido resumo) {
        return new Meta(
                resumo.getUsuarioId(),
                resumo.getTipo(),
                resumo.getDescricao(),
                resumo.getQuantidade(),
                resumo.getHabitosIds()
        );
    }
}

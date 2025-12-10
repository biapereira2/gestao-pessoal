package gestao.pessoal.aplicacao.principal.meta;

import gestao.pessoal.dominio.principal.princ.meta.Meta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetaRepositorioApl {

    void salvar(Meta meta);

    Optional<Meta> buscarPorId(UUID id);

    List<MetaResumo> listarResumosPorUsuario(UUID usuarioId);

    List<MetaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId);

    void remover(UUID id);
}
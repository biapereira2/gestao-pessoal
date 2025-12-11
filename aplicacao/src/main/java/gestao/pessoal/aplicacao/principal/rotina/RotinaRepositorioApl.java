package gestao.pessoal.aplicacao.principal.rotina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RotinaRepositorioApl {

    void salvar(RotinaResumoExpandido rotina);

    Optional<RotinaResumoExpandido> buscarPorId(UUID id);

    List<RotinaResumo> listarResumosPorUsuario(UUID usuarioId);

    List<RotinaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId);

    void remover(UUID id);
}

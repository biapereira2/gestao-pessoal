package gestao.pessoal.aplicacao.principal.habito;

import gestao.pessoal.dominio.principal.princ.habito.Habito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HabitoRepositorioApl {

    void salvar(Habito habito);

    Optional<Habito> buscarPorId(UUID id);

    List<HabitoResumo> listarResumosPorUsuario(UUID usuarioId);

    List<HabitoResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId);

    void remover(UUID id);
}

package gestao.pessoal.aplicacao.principal.alerta;

import gestao.pessoal.dominio.principal.princ.alerta.Alerta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertaRepositorioApl {
    void salvar(Alerta alerta);
    Optional<Alerta> buscarPorId(UUID id);
    List<AlertaResumo> listarPorUsuario(UUID usuarioId);
    List<AlertaResumoExpandido> listarResumosExpandidoPorUsuario(UUID usuarioId); // novo método
    void remover(UUID id);
}

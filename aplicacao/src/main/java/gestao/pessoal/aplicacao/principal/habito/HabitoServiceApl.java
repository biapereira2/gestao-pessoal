package gestao.pessoal.aplicacao.principal.habito;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.habito.*;
import gestao.pessoal.dominio.principal.princ.habito.decorator.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HabitoServiceApl {

    private final HabitoRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;

    public HabitoServiceApl(HabitoRepositorioApl repositorio,
                            UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
    }

    // =========================
    // MÉTODOS JÁ USADOS PELO CONTROLLER
    // =========================

    public void criar(Habito habito) {
        if (usuarioService.buscarPorId(habito.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        repositorio.salvar(habito);
    }

    public Optional<Habito> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<HabitoResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<HabitoResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    public void atualizar(Habito habito) {
        repositorio.salvar(habito);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }

    // =========================
    // ✨ MÉTODO NOVO COM DECORATOR
    // =========================

    public int calcularPontosDecorados(UUID habitoId) {
        Habito habito = repositorio.buscarPorId(habitoId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito não encontrado."));

        HabitoBase habitoDecorado =
                new HabitoComPontuacaoExtra(
                        new HabitoComRestricaoHorario(
                                habito,
                                LocalTime.of(6, 0),
                                LocalTime.of(22, 0)
                        ),
                        50 // bônus extra
                );

        return habitoDecorado.getPontos();
    }
}

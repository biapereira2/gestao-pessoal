package gestao.pessoal.aplicacao.principal.habito;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.habito.*;
import gestao.pessoal.dominio.principal.princ.habito.decorator.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HabitoServiceApl {

    private final HabitoRepositorioApl repositorio;
    private final CheckinHabitoRepositorioApl checkinRepositorio;
    private final UsuarioServiceApl usuarioService;

    public HabitoServiceApl(HabitoRepositorioApl repositorio,
                            CheckinHabitoRepositorioApl checkinRepositorio,
                            UsuarioServiceApl usuarioService) {
        this.repositorio = repositorio;
        this.checkinRepositorio = checkinRepositorio;
        this.usuarioService = usuarioService;
    }

    // =======================================================
    // MÉTODOS JÁ USADOS PELO CONTROLLER (Inalterados)
    // =======================================================

    public void criar(Habito habito) {
        if (usuarioService.buscarPorId(habito.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        repositorio.salvar(habito);
    }

    public Optional<Habito> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public Optional<Habito> buscarPorIdExpandido(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<HabitoResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<HabitoResumo> obterHabitoExpandido(UUID usuarioId) {
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

    // =======================================================
    // MÉTODOS NOVOS PARA CHECK-IN/DESMARQUE (CORRIGIDOS)
    // =======================================================

    public void marcarCheckin(UUID habitoId, UUID usuarioId, LocalDate data) {
        if (repositorio.buscarPorId(habitoId).isEmpty()) {
            throw new IllegalArgumentException("Hábito não encontrado.");
        }

        // 1. Usando o método gerado pelo Spring Data JPA na interface
        if (checkinRepositorio.existsByHabitoIdAndUsuarioIdAndData(habitoId, usuarioId, data)) {
            throw new IllegalStateException("Check-in para esta data já foi realizado.");
        }

        // 2. Cria a entidade (Assumindo que CheckInHabito tem um construtor compatível)
        CheckInHabito checkin = new CheckInHabito(habitoId, usuarioId, data);

        // 3. Usa o método 'save' (herdado do JpaRepository)
        checkinRepositorio.save(checkin);
    }

    public void desmarcarCheckin(UUID habitoId, UUID usuarioId, LocalDate data) {

        // 1. Usando o método customizado findByHabitoIdAndUsuarioIdAndData definido na interface
        Optional<CheckInHabito> checkinOptional = checkinRepositorio.findByHabitoIdAndUsuarioIdAndData(
                habitoId, usuarioId, data
        );

        if (checkinOptional.isEmpty()) {
            throw new IllegalArgumentException("Check-in não encontrado para desmarcar.");
        }

        // 2. Usa o método 'delete' (herdado do JpaRepository)
        // Note: Se você usa o ID, pode usar deleteById, mas usar delete(entity) é mais comum.
        checkinRepositorio.delete(checkinOptional.get());
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
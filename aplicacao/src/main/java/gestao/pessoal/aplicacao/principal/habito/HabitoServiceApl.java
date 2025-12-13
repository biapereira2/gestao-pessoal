package gestao.pessoal.aplicacao.principal.habito;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.aplicacao.principal.meta.MetaServiceApl;
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
    private final MetaServiceApl metaService; // 👈 Injeção

    public HabitoServiceApl(HabitoRepositorioApl repositorio,
                            CheckinHabitoRepositorioApl checkinRepositorio,
                            UsuarioServiceApl usuarioService,
                            MetaServiceApl metaService) {
        this.repositorio = repositorio;
        this.checkinRepositorio = checkinRepositorio;
        this.usuarioService = usuarioService;
        this.metaService = metaService; // 👈 Atribuição
    }

    // -----------------------------------------------------------------------
    // MÉTODOS DE CHECK-IN/DESMARQUE (COM DISPARO PARA META)
    // -----------------------------------------------------------------------

    public void marcarCheckin(UUID habitoId, UUID usuarioId, LocalDate data) {
        if (repositorio.buscarPorId(habitoId).isEmpty()) {
            throw new IllegalArgumentException("Hábito não encontrado.");
        }

        if (checkinRepositorio.existsByHabitoIdAndUsuarioIdAndData(habitoId, usuarioId, data)) {
            throw new IllegalStateException("Check-in para esta data já foi realizado.");
        }

        CheckInHabito checkin = new CheckInHabito(habitoId, usuarioId, data);
        checkinRepositorio.save(checkin);

        // 🎯 AÇÃO CRÍTICA: Notifica o serviço de metas para recalcular o progresso.
        metaService.atualizarMetasAssociadas(habitoId, usuarioId, data);
    }

    public void desmarcarCheckin(UUID habitoId, UUID usuarioId, LocalDate data) {

        Optional<CheckInHabito> checkinOptional = checkinRepositorio.findByHabitoIdAndUsuarioIdAndData(
                habitoId, usuarioId, data
        );

        if (checkinOptional.isEmpty()) {
            throw new IllegalArgumentException("Check-in não encontrado para desmarcar.");
        }

        checkinRepositorio.delete(checkinOptional.get());

        // 🎯 AÇÃO CRÍTICA: Notifica o serviço de metas para recalcular o progresso.
        metaService.atualizarMetasAssociadas(habitoId, usuarioId, data);
    }

    // -----------------------------------------------------------------------
    // ✨ NOVO MÉTODO: Usado pelo MetaServiceApl para Recálculo
    // -----------------------------------------------------------------------

    /**
     * Conta quantos dos hábitos fornecidos (IDs) fizeram check-in para o usuário no dia.
     */
    public int contarHabitosConcluidosNoDia(List<UUID> habitosIds, UUID usuarioId, LocalDate data) {
        long count = checkinRepositorio.countByHabitoIdInAndUsuarioIdAndData(habitosIds, usuarioId, data);
        return (int) count;
    }

    // -----------------------------------------------------------------------
    // MÉTODOS DE CRUD, ETC. (Inalterados)
    // -----------------------------------------------------------------------

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
package gestao.pessoal.aplicacao.principal.meta;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.aplicacao.principal.habito.HabitoServiceApl;
import gestao.pessoal.dominio.principal.princ.meta.Meta;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetaServiceApl {

    private final MetaRepositorioApl repositorio;
    private final UsuarioServiceApl usuarioService;
    private final HabitoServiceApl habitoService;

    public MetaServiceApl(MetaRepositorioApl repositorio,
                          UsuarioServiceApl usuarioService,
                          @Lazy HabitoServiceApl habitoService) {
        this.repositorio = repositorio;
        this.usuarioService = usuarioService;
        this.habitoService = habitoService;
    }

    // 🔹 Atualiza o progresso das metas quando um hábito muda de status
    public void atualizarMetasAssociadas(UUID habitoId, UUID usuarioId, LocalDate data) {
        List<Meta> metasParaAtualizar = repositorio.buscarMetasPorHabitoIdEUsuarioId(habitoId, usuarioId);

        for (Meta meta : metasParaAtualizar) {
            // Conta quantos hábitos da meta foram concluídos no dia
            int habitosCompletosNoDia = habitoService.contarHabitosConcluidosNoDia(
                    meta.getHabitosIds(),
                    usuarioId,
                    data
            );

            meta.setHabitosCompletos(habitosCompletosNoDia);
            meta.dispararAlertaSeNecessario();
            repositorio.salvar(meta);
        }
    }

    // -----------------------------------------------------------------------
    // MÉTODOS EXISTENTES
    // -----------------------------------------------------------------------
    public void criar(Meta meta) {
        if (usuarioService.buscarPorId(meta.getUsuarioId()).isEmpty()) {
            throw new IllegalArgumentException(
                    "Não é possível criar meta: usuário com ID " + meta.getUsuarioId() + " não existe."
            );
        }

        meta.setHabitosCompletos(0);
        repositorio.salvar(meta);
    }

    public Optional<Meta> buscarPorId(UUID id) {
        return repositorio.buscarPorId(id);
    }

    public List<MetaResumo> listarResumos(UUID usuarioId) {
        return repositorio.listarResumosPorUsuario(usuarioId);
    }

    public List<MetaResumoExpandido> listarResumosExpandido(UUID usuarioId) {
        return repositorio.listarResumosExpandidoPorUsuario(usuarioId);
    }

    public void atualizar(Meta meta) {
        repositorio.salvar(meta);
    }

    public void remover(UUID id) {
        repositorio.remover(id);
    }
}

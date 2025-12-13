package gestao.pessoal.dominio.principal.engajamento.badges;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.dominio.principal.princ.meta.RepositorioMeta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BadgesService {

    private final RepositorioBadges repositorioBadges;
    private final RepositorioMeta repositorioMeta;
    private final RepositorioUsuario repositorioUsuario;
    private final ProgressoUsuarioService progressoUsuarioService;

    public BadgesService(
            RepositorioBadges repositorioBadges,
            RepositorioMeta repositorioMeta,
            RepositorioUsuario repositorioUsuario,
            ProgressoUsuarioService progressoUsuarioService
    ) {
        this.repositorioBadges = repositorioBadges;
        this.repositorioMeta = repositorioMeta;
        this.repositorioUsuario = repositorioUsuario;
        this.progressoUsuarioService = progressoUsuarioService;
    }

    /**
     * Verifica todas as badges existentes e concede automaticamente
     * aquelas cujos requisitos o usuário atendeu.
     *
     * Este método deve ser chamado por serviços de aplicação
     * (ex: ProgressoUsuarioService, MetaService).
     */
    public void verificarEConcederBadges(UUID usuarioId) {
        validarUsuario(usuarioId);

        List<Badges> modelos = repositorioBadges.listarTodosModelos();

        for (Badges modelo : modelos) {

            // Já conquistou? pula.
            if (repositorioBadges.usuarioConquistouBadge(usuarioId, modelo.getId())) {
                continue;
            }

            if (atendeRequisitos(usuarioId, modelo)) {
                Badges conquista = Badges.reidratarConquistada(
                        modelo.getId(),
                        modelo.getNome(),
                        modelo.getDescricao(),
                        modelo.getCategoria(),
                        modelo.getValorRequerido(),
                        usuarioId
                );
                repositorioBadges.salvarConquista(conquista);
            }
        }
    }

    /**
     * Lista todas as badges conquistadas pelo usuário.
     */
    public List<Badges> listarConquistas(UUID usuarioId) {
        validarUsuario(usuarioId);
        return repositorioBadges.listarConquistasPorUsuario(usuarioId);
    }

    /**
     * Lista todas as badges que o usuário ainda NÃO conquistou.
     */
    public List<Badges> listarBadgesDisponiveis(UUID usuarioId) {
        validarUsuario(usuarioId);

        return repositorioBadges.listarTodosModelos().stream()
                .filter(modelo ->
                        !repositorioBadges.usuarioConquistouBadge(usuarioId, modelo.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Lógica centralizada de verificação de requisitos.
     * Facilita evolução e testes.
     */
    private boolean atendeRequisitos(UUID usuarioId, Badges modelo) {

        return switch (modelo.getCategoria()) {

            case NIVEL -> {
                int nivelAtual =
                        progressoUsuarioService.visualizarProgresso(usuarioId).getNivel();
                yield nivelAtual >= modelo.getValorRequerido();
            }

            case META_ATINGIDA -> {
                long metasConcluidas = repositorioMeta.listarPorUsuario(usuarioId).stream()
                        .filter(meta ->
                                meta.getHabitosCompletos() >= meta.getQuantidade())
                        .count();
                yield metasConcluidas >= modelo.getValorRequerido();
            }

            // Futuras categorias (ex: HABITO_CONSECUTIVO, CHECKINS_SEGUIDOS)
            default -> false;
        };
    }

    private void validarUsuario(UUID usuarioId) {
        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Usuário com ID " + usuarioId + " não existe."
            );
        }
    }

    /**
     * Método utilitário para setup inicial ou testes.
     */
    public void carregarModelosPadrao() {
        repositorioBadges.salvarModelo(
                new Badges("Nível 5", "Atingiu o Nível 5",
                        Badges.Categoria.NIVEL, 5));

        repositorioBadges.salvarModelo(
                new Badges("Nível 10", "Atingiu o Nível 10",
                        Badges.Categoria.NIVEL, 10));

        repositorioBadges.salvarModelo(
                new Badges("Mestre de Metas",
                        "Completou 3 metas com sucesso",
                        Badges.Categoria.META_ATINGIDA, 3));
    }
}

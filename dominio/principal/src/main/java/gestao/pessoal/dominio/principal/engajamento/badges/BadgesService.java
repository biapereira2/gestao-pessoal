package gestao.pessoal.dominio.principal.engajamento.badges;

import gestao.pessoal.dominio.principal.compartilhado.RepositorioUsuario;
import gestao.pessoal.dominio.principal.engajamento.progressoUsuario.ProgressoUsuarioService;
import gestao.pessoal.dominio.principal.princ.meta.RepositorioMeta;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BadgesService {

    private final RepositorioBadges repositorioBadges;
    private final RepositorioMeta repositorioMeta;
    private final RepositorioUsuario repositorioUsuario;
    private final ProgressoUsuarioService progressoUsuarioService; // Para verificar o nível

    public BadgesService(RepositorioBadges repositorioBadges, RepositorioMeta repositorioMeta,
                         RepositorioUsuario repositorioUsuario, ProgressoUsuarioService progressoUsuarioService) {
        this.repositorioBadges = repositorioBadges;
        this.repositorioMeta = repositorioMeta;
        this.repositorioUsuario = repositorioUsuario;
        this.progressoUsuarioService = progressoUsuarioService;
    }

    /**
     * Concede automaticamente uma badge ao usuário se ele atender aos requisitos.
     * Este método seria chamado por outros services (ex: ProgressoUsuarioService ao subir de nível, MetaService ao bater meta).
     */
    public void verificarEConcederBadges(UUID usuarioId) {
        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        List<Badges> modelos = repositorioBadges.listarTodosModelos();

        for (Badges modelo : modelos) {
            if (repositorioBadges.usuarioConquistouBadge(usuarioId, modelo.getId())) {
                continue; // Pula se já conquistada
            }

            boolean conceder = false;

            switch (modelo.getCategoria()) {
                case NIVEL:
                    int nivelAtual = progressoUsuarioService.visualizarProgresso(usuarioId).getNivel();
                    if (nivelAtual >= modelo.getValorRequerido()) {
                        conceder = true;
                    }
                    break;
                case META_ATINGIDA:
                    // Exemplo: Conta quantas metas o usuário já bateu (usando RepositorioMeta)
                    // (Esta lógica seria mais complexa no mundo real, mas simplificada para o teste)
                    long metasConcluidas = repositorioMeta.listarPorUsuario(usuarioId).stream()
                            .filter(m -> m.getHabitosCompletos() >= m.getQuantidade())
                            .count();
                    if (metasConcluidas >= modelo.getValorRequerido()) {
                        conceder = true;
                    }
                    break;
                // Outras lógicas de concessão (ex: HABITO_CONSECUTIVO)
            }

            if (conceder) {
                Badges conquista = Badges.conceder(modelo, usuarioId);
                repositorioBadges.salvarConquista(conquista);
            }
        }
    }

    /**
     * Lista todas as badges conquistadas pelo usuário.
     */
    public List<Badges> listarConquistas(UUID usuarioId) {
        if (repositorioUsuario.buscarPorId(usuarioId).isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return repositorioBadges.listarConquistasPorUsuario(usuarioId);
    }

    /**
     * Lista todas as badges que o usuário AINDA não conquistou.
     */
    public List<Badges> listarBadgesDisponiveis(UUID usuarioId) {
        List<Badges> todosModelos = repositorioBadges.listarTodosModelos();

        // Filtra os modelos que o usuário AINDA NÃO CONQUISTOU
        return todosModelos.stream()
                .filter(modelo -> !repositorioBadges.usuarioConquistouBadge(usuarioId, modelo.getId()))
                .collect(Collectors.toList());
    }

    // Método para setup de teste: carrega modelos de badges
    public void carregarModelosPadrao() {
        repositorioBadges.salvarModelo(new Badges("Nível 5", "Atingiu o Nível 5", Badges.Categoria.NIVEL, 5));
        repositorioBadges.salvarModelo(new Badges("Nível 10", "Atingiu o Nível 10", Badges.Categoria.NIVEL, 10));
        repositorioBadges.salvarModelo(new Badges("Mestre de Metas", "Completou 3 metas com sucesso", Badges.Categoria.META_ATINGIDA, 3));
    }
}
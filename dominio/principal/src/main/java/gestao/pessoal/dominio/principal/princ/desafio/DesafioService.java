package gestao.pessoal.dominio.principal.princ.desafio;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe Abstrata que define o esqueleto (Template Method) para operações no Desafio.
 * Delega a busca e validação da entidade (por nome ou por ID) para as subclasses.
 */
public abstract class DesafioService { // Agora é a classe abstrata de domínio

    protected final RepositorioDesafio repositorioDesafio;
    protected final RepositorioUsuario repositorioUsuario;

    // CONSTRUTOR CORRIGIDO: Deve usar o nome da classe: DesafioService
    public DesafioService(RepositorioDesafio repositorioDesafio, RepositorioUsuario repositorioUsuario) {
        this.repositorioDesafio = repositorioDesafio;
        this.repositorioUsuario = repositorioUsuario;
    }

    // --- MÉTODOS PRIMITIVOS ABSTRATOS (O Hook do Template Method) ---

    /**
     * Busca e valida um Convite Desafio. Implementação varia (por nome ou ID).
     * O 'identificador' pode ser String (nome) ou UUID (ID).
     */
    protected abstract ConviteDesafio buscarEValidarConviteParaResolucao(UUID convidadoId, Object identificador)
            throws IllegalArgumentException, IllegalStateException;

    /**
     * Busca e valida um Desafio para encerramento. Implementação varia (por nome ou ID).
     */
    protected abstract Desafio buscarEValidarDesafioParaEncerramento(UUID criadorId, Object identificador)
            throws IllegalArgumentException;

    /**
     * Busca e valida um Desafio para saída. Implementação varia (por nome ou ID).
     */
    protected abstract Desafio buscarEValidarDesafioParaSaida(UUID participanteId, Object identificador)
            throws IllegalArgumentException, IllegalStateException;

    // --- MÉTODOS TEMPLATE (A Lógica Central, que é FIXA) ---

    // Template para Aceitar Convite
    public final Desafio aceitarConviteTemplate(UUID convidadoId, Object identificador) {
        // 1. Passo Variável: Busca e valida o Convite (MÉTODO PRIMITIVO)
        ConviteDesafio convite = buscarEValidarConviteParaResolucao(convidadoId, identificador);

        // 2. Passos Fixos: Lógica Comum do Domínio
        convite.aceitar();
        repositorioDesafio.salvarConvite(convite);

        Desafio desafio = repositorioDesafio.buscarPorId(convite.getDesafioId())
                .orElseThrow(() -> new IllegalStateException("Desafio não encontrado após aceitação."));

        desafio.adicionarParticipante(convite.getConvidadoId());
        repositorioDesafio.salvar(desafio);

        return desafio;
    }

    // Template para Rejeitar Convite
    public final void rejeitarConviteTemplate(UUID convidadoId, Object identificador) {
        // 1. Passo Variável: Busca e valida o Convite (MÉTODO PRIMITIVO)
        ConviteDesafio convite = buscarEValidarConviteParaResolucao(convidadoId, identificador);

        // 2. Passos Fixos: Lógica Comum do Domínio
        convite.rejeitar();
        repositorioDesafio.salvarConvite(convite);
    }

    // Template para Encerrar Desafio
    public final void encerrarDesafioTemplate(UUID criadorId, Object identificador) {
        // 1. Passo Variável: Busca e valida o Desafio (MÉTODO PRIMITIVO)
        Desafio desafio = buscarEValidarDesafioParaEncerramento(criadorId, identificador);

        // 2. Passos Fixos: Lógica Comum do Domínio
        desafio.encerrar();
        repositorioDesafio.salvar(desafio);
    }

    // Template para Sair do Desafio
    public final void sairDoDesafioTemplate(UUID participanteId, Object identificador) {
        // 1. Passo Variável: Busca e valida o Desafio (MÉTODO PRIMITIVO)
        Desafio desafio = buscarEValidarDesafioParaSaida(participanteId, identificador);

        // 2. Passos Fixos: Lógica Comum do Domínio
        desafio.removerParticipante(participanteId);
        repositorioDesafio.salvar(desafio);
    }

    // --- MÉTODOS COMPARTILHADOS (Não fazem parte do Template Method, mas são necessários) ---

    // Método para o Cenário 1: Criar Desafio e Enviar Convites
    public Desafio criarDesafio(UUID criadorId, String nome, List<UUID> habitosIds, LocalDate dataFim, List<String> emailsConvidados) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do desafio é obrigatório.");
        }

        Desafio novoDesafio = new Desafio(UUID.randomUUID(), criadorId, nome, habitosIds, LocalDate.now(), dataFim);
        repositorioDesafio.salvar(novoDesafio);

        for (String emailConvidado : emailsConvidados) {
            Optional<Usuario> usuarioConvidado = repositorioUsuario.buscarPorEmail(emailConvidado);
            if (usuarioConvidado.isPresent()) {
                UUID convidadoId = usuarioConvidado.get().getId();
                if (!convidadoId.equals(criadorId)) {
                    ConviteDesafio convite = new ConviteDesafio(UUID.randomUUID(), novoDesafio.getId(), convidadoId, criadorId);
                    repositorioDesafio.salvarConvite(convite);
                }
            }
        }
        return novoDesafio;
    }

    // Métodos de Busca e Listagem
    public Optional<Desafio> buscarPorId(UUID id) {
        return repositorioDesafio.buscarPorId(id);
    }

    public List<Desafio> listarPorUsuario(UUID usuarioId) {
        return repositorioDesafio.buscarTodosDoUsuario(usuarioId);
    }

    public List<ConviteDesafio> listarConvitesPendentes(UUID convidadoId) {
        return repositorioDesafio.buscarConvitesPendentes(convidadoId);
    }

    // Simulação de Progresso
    public int calcularProgressoTotal(String nomeDesafio) {
        if (nomeDesafio.equals("Desafio de Leitura")) {
            return 8;
        }
        return 0;
    }
}
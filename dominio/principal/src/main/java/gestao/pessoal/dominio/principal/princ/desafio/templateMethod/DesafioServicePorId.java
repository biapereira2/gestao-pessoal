package gestao.pessoal.dominio.principal.princ.desafio.templateMethod;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do DesafioService que utiliza o ID do desafio/convite como identificador.
 */
public class DesafioServicePorId extends DesafioService {

    public DesafioServicePorId(RepositorioDesafio repositorioDesafio, RepositorioUsuario repositorioUsuario) {
        super(repositorioDesafio, repositorioUsuario);
    }

    // --- MÉTODOS DE SUPORTE (Simulação de busca de Convite por ID) ---

    // Simulação de busca por ID a partir dos convites pendentes.
    private Optional<ConviteDesafio> buscarConvitePorId(UUID conviteId) {
        // Idealmente, o repositório teria um método direto para isso.
        // null no buscarConvitesPendentes significa buscar todos (ineficiente, mas necessário pela assinatura).
        return repositorioDesafio.buscarConvitesPendentes(null).stream()
                .filter(c -> c.getId().equals(conviteId))
                .findFirst();
    }

    // --- IMPLEMENTAÇÃO DOS MÉTODOS PRIMITIVOS (HOOKS) POR ID ---

    @Override
    protected ConviteDesafio buscarEValidarConviteParaResolucao(UUID convidadoId, Object identificador) {
        UUID conviteId = (UUID) identificador;
        ConviteDesafio convite = buscarConvitePorId(conviteId)
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado ou já resolvido."));

        if (!convite.getConvidadoId().equals(convidadoId)) {
            // Este check garante que o usuário autenticado (convidadoId) é quem está resolvendo o convite.
            throw new IllegalArgumentException("Convite não pertence ao usuário autenticado.");
        }

        if (convite.getStatus() != ConviteDesafio.StatusConvite.PENDENTE) {
            throw new IllegalStateException("O convite não está pendente.");
        }
        return convite;
    }

    @Override
    protected Desafio buscarEValidarDesafioParaEncerramento(UUID criadorId, Object identificador) {
        UUID desafioId = (UUID) identificador;
        Desafio desafio = repositorioDesafio.buscarPorId(desafioId)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado."));

        if (!desafio.getCriadorId().equals(criadorId)) {
            throw new IllegalArgumentException("Você não é o criador deste desafio e não pode encerrá-lo.");
        }
        return desafio;
    }

    @Override
    protected Desafio buscarEValidarDesafioParaSaida(UUID participanteId, Object identificador) {
        UUID desafioId = (UUID) identificador;
        Desafio desafio = repositorioDesafio.buscarPorId(desafioId)
                .orElseThrow(() -> new IllegalArgumentException("Desafio não encontrado."));

        if (desafio.getCriadorId().equals(participanteId)) {
            throw new IllegalStateException("O criador não pode sair, deve encerrar o desafio.");
        }

        if (!desafio.getParticipantesIds().contains(participanteId)) {
            throw new IllegalArgumentException("Usuário não é participante deste desafio.");
        }
        return desafio;
    }

    // --- MÉTODOS PÚBLICOS (Chamadas Template por ID) ---

    public Desafio aceitarConvitePorId(UUID conviteId, UUID convidadoId) {
        return aceitarConviteTemplate(convidadoId, conviteId);
    }

    public void rejeitarConvitePorId(UUID conviteId, UUID convidadoId) {
        rejeitarConviteTemplate(convidadoId, conviteId);
    }

    public void encerrarDesafioPorId(UUID criadorId, UUID desafioId) {
        encerrarDesafioTemplate(criadorId, desafioId);
    }

    public void sairDoDesafioPorId(UUID participanteId, UUID desafioId) {
        sairDoDesafioTemplate(participanteId, desafioId);
    }
}
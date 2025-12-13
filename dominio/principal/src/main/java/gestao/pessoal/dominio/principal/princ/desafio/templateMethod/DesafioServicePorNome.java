package gestao.pessoal.dominio.principal.princ.desafio.templateMethod;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementação do DesafioService que utiliza o NOME do desafio/convite como identificador.
 */
public class DesafioServicePorNome extends DesafioService {

    public DesafioServicePorNome(RepositorioDesafio repositorioDesafio, RepositorioUsuario repositorioUsuario) {
        super(repositorioDesafio, repositorioUsuario);
    }

    // --- IMPLEMENTAÇÃO DOS MÉTODOS PRIMITIVOS (HOOKS) POR NOME ---

    @Override
    protected ConviteDesafio buscarEValidarConviteParaResolucao(UUID convidadoId, Object identificador) {
        String nomeDesafio = (String) identificador;
        List<ConviteDesafio> convitesPendentes = repositorioDesafio.buscarConvitesPendentes(convidadoId);

        Optional<ConviteDesafio> conviteOpt = convitesPendentes.stream()
                .filter(c -> {
                    Optional<Desafio> desafioOpt = repositorioDesafio.buscarPorId(c.getDesafioId());
                    return desafioOpt.isPresent() && desafioOpt.get().getNome().equals(nomeDesafio);
                })
                .findFirst();

        if (conviteOpt.isEmpty()) {
            throw new IllegalArgumentException("Convite não encontrado ou já resolvido: " + nomeDesafio);
        }

        return conviteOpt.get();
    }

    @Override
    protected Desafio buscarEValidarDesafioParaEncerramento(UUID criadorId, Object identificador) {
        String nomeDesafio = (String) identificador;
        Optional<Desafio> desafioOpt = repositorioDesafio.buscarTodosDoUsuario(criadorId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio) && d.getCriadorId().equals(criadorId))
                .findFirst();

        if (desafioOpt.isEmpty()) {
            throw new IllegalArgumentException("Desafio não encontrado ou você não é o criador.");
        }
        return desafioOpt.get();
    }

    @Override
    protected Desafio buscarEValidarDesafioParaSaida(UUID participanteId, Object identificador) {
        String nomeDesafio = (String) identificador;
        Optional<Desafio> desafioOpt = repositorioDesafio.buscarTodosDoUsuario(participanteId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio) && d.getParticipantesIds().contains(participanteId))
                .findFirst();

        if (desafioOpt.isEmpty()) {
            throw new IllegalArgumentException("Desafio não encontrado.");
        }

        Desafio desafio = desafioOpt.get();
        if (desafio.getCriadorId().equals(participanteId)) {
            throw new IllegalStateException("O criador não pode sair, deve encerrar o desafio.");
        }
        return desafio;
    }

    // --- MÉTODOS PÚBLICOS (Chamadas Template por Nome) ---

    public Desafio aceitarConvite(UUID convidadoId, String nomeDesafio) {
        return aceitarConviteTemplate(convidadoId, nomeDesafio);
    }

    public void rejeitarConvite(UUID convidadoId, String nomeDesafio) {
        rejeitarConviteTemplate(convidadoId, nomeDesafio);
    }

    public void encerrarDesafio(UUID criadorId, String nomeDesafio) {
        encerrarDesafioTemplate(criadorId, nomeDesafio);
    }

    public void sairDoDesafio(UUID participanteId, String nomeDesafio) {
        sairDoDesafioTemplate(participanteId, nomeDesafio);
    }

    // Método de Busca específico por Nome (mantido do original)
    public Optional<Desafio> buscarDesafioPorNomeECriador(String nomeDesafio, UUID criadorId) {
        return repositorioDesafio.buscarTodosDoUsuario(criadorId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio))
                .findFirst();
    }
}
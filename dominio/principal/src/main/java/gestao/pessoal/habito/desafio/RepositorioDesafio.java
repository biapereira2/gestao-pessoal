package gestao.pessoal.habito.desafio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioDesafio {
    void salvar(Desafio desafio);
    Optional<Desafio> buscarPorId(UUID id);
    List<Desafio> buscarTodosDoUsuario(UUID usuarioId);
    void salvarConvite(ConviteDesafio convite);
    Optional<ConviteDesafio> buscarConvitePorDesafioEConvidado(UUID desafioId, UUID convidadoId);
    List<ConviteDesafio> buscarConvitesPendentes(UUID convidadoId);
    void removerConvite(UUID conviteId);
    void remover(UUID desafioId);
}

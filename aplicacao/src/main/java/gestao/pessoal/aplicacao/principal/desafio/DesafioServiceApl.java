package gestao.pessoal.aplicacao.principal.desafio;

import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DesafioServiceApl {

    Desafio criarDesafio(UUID criadorId, String nome, List<UUID> habitosIds, java.time.LocalDate dataFim, List<String> emailsConvidados);

    void aceitarConvite(UUID convidadoId, UUID conviteId);

    void rejeitarConvite(UUID convidadoId, UUID conviteId);

    void encerrarDesafio(UUID criadorId, UUID desafioId);

    void sairDoDesafio(UUID participanteId, UUID desafioId);

    Optional<Desafio> buscarPorId(UUID id);

    List<DesafioResumo> listarDesafiosDoUsuario(UUID usuarioId);

    List<ConviteResumo> listarConvitesPendentes(UUID convidadoId);

    // DTO para acompanhamento de progresso (Exemplo)
    List<ProgressoParticipanteDTO> acompanharProgresso(UUID desafioId);
}
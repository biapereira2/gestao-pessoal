package gestao.pessoal.aplicacao.principal.desafio;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.DesafioService;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DesafioServiceAplImpl implements DesafioServiceApl {

    // Dependência do Serviço de Domínio para Lógica de Negócio
    private final DesafioService desafioService;

    // Dependência para buscar informações do usuário (para DTOs/validações)
    private final UsuarioServiceApl usuarioService;

    // Dentro de DesafioServiceAplImpl.java
    public DesafioServiceAplImpl(DesafioService desafioService, UsuarioServiceApl usuarioService) {
        this.desafioService = desafioService; // <-- Spring não achou este Bean!
        this.usuarioService = usuarioService;
    }

    // --- Implementação dos Métodos da Interface DesafioServiceApl ---

    @Override
    public Desafio criarDesafio(UUID criadorId, String nome, List<UUID> habitosIds, LocalDate dataFim, List<String> emailsConvidados) {
        // Delega a lógica de criação e envio de convites ao Serviço de Domínio
        return desafioService.criarDesafio(criadorId, nome, habitosIds, dataFim, emailsConvidados);
    }

    @Override
    public void aceitarConvite(UUID convidadoId, UUID conviteId) {
        // Delega a lógica de aceitação de convite e adição ao desafio.
        // Nota: O DesafioService original utilizava o nome do desafio, aqui usamos o ID do convite para ser mais robusto.
        // Assumimos que o DesafioService subjacente foi atualizado para usar o conviteId
        // Para fins deste exemplo, estamos adaptando a chamada.

        Optional<ConviteDesafio> conviteOpt = desafioService.buscarConvitePorId(conviteId);
        if (conviteOpt.isEmpty() || !conviteOpt.get().getConvidadoId().equals(convidadoId)) {
            throw new IllegalArgumentException("Convite inválido ou não pertence ao usuário.");
        }

        desafioService.aceitarConvitePorId(conviteId);
    }

    @Override
    public void rejeitarConvite(UUID convidadoId, UUID conviteId) {
        Optional<ConviteDesafio> conviteOpt = desafioService.buscarConvitePorId(conviteId);
        if (conviteOpt.isEmpty() || !conviteOpt.get().getConvidadoId().equals(convidadoId)) {
            throw new IllegalArgumentException("Convite inválido ou não pertence ao usuário.");
        }
        desafioService.rejeitarConvitePorId(conviteId);
    }

    @Override
    public void encerrarDesafio(UUID criadorId, UUID desafioId) {
        // Delega a lógica de encerramento, incluindo validação de criador, ao Domínio
        desafioService.encerrarDesafioPorId(criadorId, desafioId);
    }

    @Override
    public void sairDoDesafio(UUID participanteId, UUID desafioId) {
        // Delega a lógica de saída, incluindo validação se é o criador, ao Domínio
        desafioService.sairDoDesafioPorId(participanteId, desafioId);
    }

    @Override
    public Optional<Desafio> buscarPorId(UUID id) {
        // Delega a busca simples ao Domínio
        return desafioService.buscarPorId(id);
    }

    @Override
    public List<DesafioResumo> listarDesafiosDoUsuario(UUID usuarioId) {
        // 1. Busca todos os objetos de Domínio (Desafio)
        List<Desafio> desafios = desafioService.listarPorUsuario(usuarioId);

        // 2. Mapeia Desafio (Domínio) para DesafioResumo (DTO de Aplicação)
        return desafios.stream()
                .map(desafio -> new DesafioResumo(
                        desafio.getId(),
                        desafio.getNome(),
                        desafio.getDataInicio(),
                        desafio.getDataFim(),
                        desafio.getStatus(),
                        desafio.getParticipantesIds().size()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConviteResumo> listarConvitesPendentes(UUID convidadoId) {
        // 1. Busca todos os objetos de Domínio (ConviteDesafio)
        List<ConviteDesafio> convites = desafioService.listarConvitesPendentes(convidadoId);

        // 2. Mapeia ConviteDesafio para ConviteResumo (DTO de Aplicação)
        return convites.stream()
                .map(convite -> {
                    // Busca o nome do desafio e o nome do criador para o DTO
                    String nomeDesafio = desafioService.buscarPorId(convite.getDesafioId())
                            .map(Desafio::getNome)
                            .orElse("Desafio Desconhecido");

                    String nomeCriador = usuarioService.buscarPorId(convite.getCriadorId())
                            .map(u -> u.getNome()) // Assumindo que Usuario possui getNome()
                            .orElse("Usuário Desconhecido");

                    return new ConviteResumo(
                            convite.getId(),
                            nomeDesafio,
                            nomeCriador,
                            convite.getDataCriacao()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgressoParticipanteDTO> acompanharProgresso(UUID desafioId) {
        // Este é um método de simulação complexo, pois depende da integração com a história de Hábito/Check-in.
        // Aqui, simulamos o progresso. Em uma aplicação real, chamaria um serviço de Domínio (ex: ProgressoService).

        Optional<Desafio> desafioOpt = desafioService.buscarPorId(desafioId);
        if (desafioOpt.isEmpty()) {
            throw new IllegalArgumentException("Desafio não encontrado.");
        }
        Desafio desafio = desafioOpt.get();

        int totalHabitos = desafio.getHabitosIds().size();

        // Simulação de progresso por participante (mock)
        return desafio.getParticipantesIds().stream()
                .map(participanteId -> {
                    String nomeParticipante = usuarioService.buscarPorId(participanteId)
                            .map(u -> u.getNome()) // Assumindo que Usuario possui getNome()
                            .orElse("Participante Desconhecido");

                    // Lógica de simulação: dar um progresso diferente para o criador e outros
                    int habitosConcluidos = participanteId.equals(desafio.getCriadorId()) ?
                            totalHabitos : Math.max(0, totalHabitos - 2);

                    return new ProgressoParticipanteDTO(participanteId, nomeParticipante, habitosConcluidos, totalHabitos);
                })
                .collect(Collectors.toList());
    }
}
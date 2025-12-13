package gestao.pessoal.aplicacao.principal.desafio;

import gestao.pessoal.aplicacao.compartilhado.usuario.UsuarioServiceApl;
import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
// NOTA: DesafioService agora é a classe abstrata.
import gestao.pessoal.dominio.principal.princ.desafio.templateMethod.DesafioServicePorId; // << IMPORTANTE: Injetamos a implementação concreta por ID
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DesafioServiceAplImpl implements DesafioServiceApl {

    // Dependência do Serviço de Domínio para Lógica de Negócio.
    // Usamos a implementação PorId, pois esta camada de Aplicação trata de IDs (mais robusto para APIs).
    private final DesafioServicePorId desafioServicePorId;

    // Dependência para buscar informações do usuário
    private final UsuarioServiceApl usuarioService;

    // Construtor corrigido e adaptado para a implementação por ID
    public DesafioServiceAplImpl(DesafioServicePorId desafioServicePorId, UsuarioServiceApl usuarioService) {
        // Spring agora buscará o Bean DesafioServicePorId, que é concreto e injetável.
        this.desafioServicePorId = desafioServicePorId;
        this.usuarioService = usuarioService;
    }

    // --- Implementação dos Métodos da Interface DesafioServiceApl ---

    @Override
    public Desafio criarDesafio(UUID criadorId, String nome, List<UUID> habitosIds, LocalDate dataFim, List<String> emailsConvidados) {
        // A lógica de criarDesafio está no DesafioServiceBase e é herdada por DesafioServicePorId
        return desafioServicePorId.criarDesafio(criadorId, nome, habitosIds, dataFim, emailsConvidados);
    }

    @Override
    public void aceitarConvite(UUID convidadoId, UUID conviteId) {
        // Usamos o método limpo por ID da implementação concreta
        // Não precisamos mais do Optional.buscarConvitePorId, pois a lógica de validação
        // de pertencimento ao usuário foi encapsulada dentro do método aceitarConvitePorId (Template Method).

        // Se a validação do Template Method falhar, ele lançará a exceção.
        desafioServicePorId.aceitarConvitePorId(conviteId, convidadoId);
    }

    @Override
    public void rejeitarConvite(UUID convidadoId, UUID conviteId) {
        // Usamos o método limpo por ID da implementação concreta
        desafioServicePorId.rejeitarConvitePorId(conviteId, convidadoId);
    }

    @Override
    public void encerrarDesafio(UUID criadorId, UUID desafioId) {
        // Delega para o método limpo por ID
        desafioServicePorId.encerrarDesafioPorId(criadorId, desafioId);
    }

    @Override
    public void sairDoDesafio(UUID participanteId, UUID desafioId) {
        // Delega para o método limpo por ID
        desafioServicePorId.sairDoDesafioPorId(participanteId, desafioId);
    }

    @Override
    public Optional<Desafio> buscarPorId(UUID id) {
        // Método herdado do DesafioServiceBase
        return desafioServicePorId.buscarPorId(id);
    }

    @Override
    public List<DesafioResumo> listarDesafiosDoUsuario(UUID usuarioId) {
        // 1. Busca todos os objetos de Domínio (Desafio) (Método herdado)
        List<Desafio> desafios = desafioServicePorId.listarPorUsuario(usuarioId);

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
        // 1. Busca todos os objetos de Domínio (ConviteDesafio) (Método herdado)
        List<ConviteDesafio> convites = desafioServicePorId.listarConvitesPendentes(convidadoId);

        // 2. Mapeia ConviteDesafio para ConviteResumo (DTO de Aplicação)
        return convites.stream()
                .map(convite -> {
                    // Busca o nome do desafio e o nome do criador para o DTO
                    String nomeDesafio = desafioServicePorId.buscarPorId(convite.getDesafioId())
                            .map(Desafio::getNome)
                            .orElse("Desafio Desconhecido");

                    // NOTA: Assumindo que o UsuarioServiceApl retorna um objeto com getNome()
                    String nomeCriador = usuarioService.buscarPorId(convite.getCriadorId())
                            .map(u -> u.getNome())
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

        Optional<Desafio> desafioOpt = desafioServicePorId.buscarPorId(desafioId);
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

// NOTE: Para este código compilar e rodar, você precisará garantir que:
// 1. DesafioServicePorId está marcado como @Service ou @Component para ser injetável.
// 2. As classes DTOs (DesafioResumo, ConviteResumo, ProgressoParticipanteDTO) existem.
// 3. A interface DesafioServiceApl existe.
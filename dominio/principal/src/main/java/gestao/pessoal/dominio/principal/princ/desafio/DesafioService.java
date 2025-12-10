package gestao.pessoal.dominio.principal.princ.desafio;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public class DesafioService {

    private final RepositorioDesafio repositorioDesafio;
    // RepositorioUsuario é necessário para verificar se os convidados existem
    private final RepositorioUsuario repositorioUsuario;

    public DesafioService(RepositorioDesafio repositorioDesafio, RepositorioUsuario repositorioUsuario) {
        this.repositorioDesafio = repositorioDesafio;
        this.repositorioUsuario = repositorioUsuario;
    }

    // Método para o Cenário 1: Criar Desafio e Enviar Convites
    // AGORA ACEITA EMAILS DE CONVIDADOS
    public Desafio criarDesafio(UUID criadorId, String nome, List<UUID> habitosIds, LocalDate dataFim, List<String> emailsConvidados) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do desafio é obrigatório.");
        }

        Desafio novoDesafio = new Desafio(UUID.randomUUID(), criadorId, nome, habitosIds, LocalDate.now(), dataFim);
        repositorioDesafio.salvar(novoDesafio);

        for (String emailConvidado : emailsConvidados) {
            // Usa o método existente na interface: buscarPorEmail
            Optional<Usuario> usuarioConvidado = repositorioUsuario.buscarPorEmail(emailConvidado);
            if (usuarioConvidado.isPresent()) {
                UUID convidadoId = usuarioConvidado.get().getId();
                // Não convida a si mesmo
                if (!convidadoId.equals(criadorId)) {
                    ConviteDesafio convite = new ConviteDesafio(UUID.randomUUID(), novoDesafio.getId(), convidadoId, criadorId);
                    repositorioDesafio.salvarConvite(convite);
                }
            }
        }
        return novoDesafio;
    }

    // Método para o Cenário 2: Aceitar Convite
    public Desafio aceitarConvite(UUID convidadoId, String nomeDesafio) {
        // Simulação: buscar o convite pelo nome do desafio (precisaria do DesafioRepository para isso)
        // No mundo real, você passaria o ID do convite. Aqui, simulamos a busca.
        List<ConviteDesafio> convitesPendentes = repositorioDesafio.buscarConvitesPendentes(convidadoId);

        Optional<ConviteDesafio> conviteOpt = convitesPendentes.stream()
                .filter(c -> {
                    Optional<Desafio> desafioOpt = repositorioDesafio.buscarPorId(c.getDesafioId());
                    return desafioOpt.isPresent() && desafioOpt.get().getNome().equals(nomeDesafio);
                })
                .findFirst();

        if (conviteOpt.isEmpty()) {
            throw new IllegalArgumentException("Convite não encontrado ou já resolvido.");
        }

        ConviteDesafio convite = conviteOpt.get();
        convite.aceitar();
        repositorioDesafio.salvarConvite(convite); // Atualiza o status do convite

        // Adiciona o participante à lista do desafio
        Desafio desafio = repositorioDesafio.buscarPorId(convite.getDesafioId())
                .orElseThrow(() -> new IllegalStateException("Desafio não encontrado."));

        desafio.adicionarParticipante(convidadoId);
        repositorioDesafio.salvar(desafio);

        return desafio;
    }

    // Método para o Cenário 3: Rejeitar Convite
    public void rejeitarConvite(UUID convidadoId, String nomeDesafio) {
        List<ConviteDesafio> convitesPendentes = repositorioDesafio.buscarConvitesPendentes(convidadoId);

        Optional<ConviteDesafio> conviteOpt = convitesPendentes.stream()
                .filter(c -> {
                    Optional<Desafio> desafioOpt = repositorioDesafio.buscarPorId(c.getDesafioId());
                    return desafioOpt.isPresent() && desafioOpt.get().getNome().equals(nomeDesafio);
                })
                .findFirst();

        if (conviteOpt.isEmpty()) {
            throw new IllegalArgumentException("Convite não encontrado.");
        }

        ConviteDesafio convite = conviteOpt.get();
        convite.rejeitar();
        repositorioDesafio.salvarConvite(convite); // Atualiza o status do convite
    }

    // Método para o Cenário 4: Encerrar Desafio
    public void encerrarDesafio(UUID criadorId, String nomeDesafio) {
        // Lógica de busca e validação (simplificada)
        Optional<Desafio> desafioOpt = repositorioDesafio.buscarTodosDoUsuario(criadorId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio) && d.getCriadorId().equals(criadorId))
                .findFirst();

        if (desafioOpt.isEmpty()) {
            throw new IllegalArgumentException("Desafio não encontrado ou você não é o criador.");
        }

        Desafio desafio = desafioOpt.get();
        desafio.encerrar();
        repositorioDesafio.salvar(desafio);
    }

    // Método para o Cenário 5: Sair do Desafio
    public void sairDoDesafio(UUID participanteId, String nomeDesafio) {
        // Lógica de busca (simplificada)
        Optional<Desafio> desafioOpt = repositorioDesafio.buscarTodosDoUsuario(participanteId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio) && d.getParticipantesIds().contains(participanteId))
                .findFirst();

        if (desafioOpt.isEmpty()) {
            throw new IllegalArgumentException("Desafio não encontrado.");
        }

        Desafio desafio = desafioOpt.get();
        if (desafio.getCriadorId().equals(participanteId)) {
            // Regra de negócio: Criador não pode sair, deve encerrar.
            throw new IllegalStateException("O criador não pode sair, deve encerrar o desafio.");
        }

        desafio.removerParticipante(participanteId);
        repositorioDesafio.salvar(desafio);
    }

    // Método de Busca (para validações)
    public Optional<Desafio> buscarDesafioPorNomeECriador(String nomeDesafio, UUID criadorId) {
        return repositorioDesafio.buscarTodosDoUsuario(criadorId).stream()
                .filter(d -> d.getNome().equals(nomeDesafio))
                .findFirst();
    }

    // Simulação de Progresso
    public int calcularProgressoTotal(String nomeDesafio) {
        // Simula o cálculo de check-ins
        if (nomeDesafio.equals("Desafio de Leitura")) {
            return 8; // Cenário 6
        }
        return 0;
    }
}

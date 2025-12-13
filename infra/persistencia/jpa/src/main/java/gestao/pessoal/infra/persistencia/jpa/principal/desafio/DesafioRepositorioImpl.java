package gestao.pessoal.infra.persistencia.jpa.principal.desafio;

import gestao.pessoal.dominio.principal.princ.desafio.Desafio;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio;
import gestao.pessoal.dominio.principal.princ.desafio.RepositorioDesafio;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import gestao.pessoal.dominio.principal.princ.desafio.ConviteDesafio.StatusConvite;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DesafioRepositorioImpl implements RepositorioDesafio {

    private final DesafioJpaRepositorio desafioRepo;
    private final ConviteJpaRepositorio conviteRepo;
    private final JpaMapper mapper;

    public DesafioRepositorioImpl(DesafioJpaRepositorio desafioRepo, ConviteJpaRepositorio conviteRepo, JpaMapper mapper) {
        this.desafioRepo = desafioRepo;
        this.conviteRepo = conviteRepo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Desafio desafio) {
        desafioRepo.save(mapper.map(desafio, DesafioJpa.class));
    }

    @Override
    public Optional<Desafio> buscarPorId(UUID id) {
        return desafioRepo.findById(id).map(jpa -> mapper.map(jpa, Desafio.class));
    }

    @Override
    public List<Desafio> buscarTodosDoUsuario(UUID usuarioId) {
        // Usa o método do JpaRepository
        return desafioRepo.findByParticipantesIdsContaining(usuarioId)
                .stream()
                .map(jpa -> mapper.map(jpa, Desafio.class))
                .collect(Collectors.toList());
    }

    @Override
    public void salvarConvite(ConviteDesafio convite) {
        conviteRepo.save(mapper.map(convite, ConviteDesafioJpa.class));
    }

    @Override
    public Optional<ConviteDesafio> buscarConvitePorDesafioEConvidado(UUID desafioId, UUID convidadoId) {
        // Simulação de busca combinada. Em produção, você adicionaria um método customizado no JpaRepository.
        return conviteRepo.findAll().stream()
                .filter(c -> c.getDesafioId().equals(desafioId) && c.getConvidadoId().equals(convidadoId))
                .findFirst()
                .map(jpa -> mapper.map(jpa, ConviteDesafio.class));
    }

    @Override
    public List<ConviteDesafio> buscarConvitesPendentes(UUID convidadoId) {
        // Usa o método do JpaRepository
        return conviteRepo.findByConvidadoIdAndStatus(convidadoId, StatusConvite.PENDENTE)
                .stream()
                .map(jpa -> mapper.map(jpa, ConviteDesafio.class))
                .collect(Collectors.toList());
    }

    @Override
    public void removerConvite(UUID conviteId) {
        conviteRepo.deleteById(conviteId);
    }

    @Override
    public void remover(UUID desafioId) {
        desafioRepo.deleteById(desafioId);
    }
}
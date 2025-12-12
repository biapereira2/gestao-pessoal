package gestao.pessoal.infra.persistencia.jpa.principal.checkin;

import gestao.pessoal.aplicacao.principal.checkIn.CheckInRepositorioApl;
import gestao.pessoal.dominio.principal.princ.checkIn.CheckIn;
import gestao.pessoal.infra.persistencia.jpa.JpaMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CheckInRepositorioImpl implements CheckInRepositorioApl {

    private final CheckInJpaRepositorio repo;
    private final JpaMapper mapper;

    public CheckInRepositorioImpl(CheckInJpaRepositorio repo, JpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void salvar(CheckIn checkIn) {
        CheckInJpa jpa = mapper.map(checkIn, CheckInJpa.class);
        // Garante que os campos da chave composta estejam populados para o JPA
        jpa.setHabitoId(checkIn.getHabitoId());
        jpa.setData(checkIn.getData());
        jpa.setUsuarioId(checkIn.getUsuarioId());

        repo.save(jpa);
    }

    @Override
    public void remover(UUID habitoId, LocalDate data, UUID usuarioId) {
        repo.deleteByHabitoIdAndDataAndUsuarioId(habitoId, data, usuarioId);
    }

    @Override
    public Optional<CheckIn> buscarPorHabitoEData(UUID habitoId, LocalDate data, UUID usuarioId) {
        return repo.findByHabitoIdAndDataAndUsuarioId(habitoId, data, usuarioId)
                .map(jpa -> mapper.map(jpa, CheckIn.class));
    }

    @Override
    public List<CheckIn> listarPorHabito(UUID habitoId, UUID usuarioId) {
        return repo.findByHabitoIdAndUsuarioIdOrderByDataAsc(habitoId, usuarioId)
                .stream()
                .map(jpa -> mapper.map(jpa, CheckIn.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocalDate> listarDatasPorHabito(UUID habitoId, UUID usuarioId) {
        return repo.findByHabitoIdAndUsuarioIdOrderByDataAsc(habitoId, usuarioId)
                .stream()
                .map(CheckInJpa::getData)
                .collect(Collectors.toList());
    }
}
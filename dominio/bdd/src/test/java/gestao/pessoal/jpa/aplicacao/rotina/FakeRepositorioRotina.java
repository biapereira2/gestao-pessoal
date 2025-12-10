package gestao.pessoal.jpa.aplicacao.rotina;

import gestao.pessoal.dominio.principal.princ.rotina.RepositorioRotina;
import gestao.pessoal.dominio.principal.princ.rotina.Rotina;

import java.util.*;
import java.util.stream.Collectors;

class FakeRepositorioRotina implements RepositorioRotina {

    private final Map<UUID, Rotina> rotinas = new HashMap<>();

    @Override
    public void salvar(Rotina rotina) {
        rotinas.put(rotina.getId(), rotina);
    }

    @Override
    public Optional<Rotina> buscarPorId(UUID rotinaId) {
        return Optional.ofNullable(rotinas.get(rotinaId));
    }

    @Override
    public List<Rotina> listarTodosPorUsuario(UUID usuarioId) {
        return rotinas.values().stream()
                .filter(r -> r.getUsuarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(UUID rotinaId) {
        rotinas.remove(rotinaId);
    }
    public void atualizar(Rotina rotinaAtualizada) {
        if (!rotinas.containsKey(rotinaAtualizada.getId())) {
            throw new IllegalArgumentException("Rotina com ID " + rotinaAtualizada.getId() + " não encontrada.");
        }
        rotinas.put(rotinaAtualizada.getId(), rotinaAtualizada);
    }

    public void limpar() {
        rotinas.clear();
    }

}
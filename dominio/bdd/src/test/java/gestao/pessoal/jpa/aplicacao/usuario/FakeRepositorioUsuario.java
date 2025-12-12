package gestao.pessoal.jpa.aplicacao.usuario;

import gestao.pessoal.dominio.principal.compartilhado.usuario.RepositorioUsuario;
import gestao.pessoal.dominio.principal.compartilhado.usuario.Usuario;

import java.util.*;
import java.util.stream.Collectors;

public class FakeRepositorioUsuario implements RepositorioUsuario {

    // Mapa principal que usa Email como chave
    private final Map<String, Usuario> usuariosPorEmail = new HashMap<>();

    // Mapa auxiliar que usa Nome como chave
    private final Map<String, Usuario> usuariosPorNome = new HashMap<>();


    /**
     * Método utilitário crucial para os testes BDD de Desafios.
     * Busca um usuário pelo nome. Se não existir, o cria.
     */
    public Usuario criarOuBuscarUsuario(String nome) {
        if (usuariosPorNome.containsKey(nome)) {
            return usuariosPorNome.get(nome);
        }

        String emailMock = nome.toLowerCase().replace(" ", "") + "@mock.com";
        String senhaMock = "SenhaSegura123";

        Usuario novoUsuario = new Usuario(nome, emailMock, senhaMock);

        salvar(novoUsuario);
        return novoUsuario;
    }

    @Override
    public void salvar(Usuario usuario) {
        usuariosPorEmail.put(usuario.getEmail(), usuario);
        usuariosPorNome.put(usuario.getNome(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuariosPorEmail.values().stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(usuariosPorEmail.get(email));
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuariosPorEmail.containsKey(email);
    }

    /**
     * 🔥 IMPLEMENTAÇÃO FALTANTE – agora corrigida
     */
    @Override
    public List<Usuario> buscarPorParteDoNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String termo = nome.toLowerCase();

        return usuariosPorNome.values().stream()
                .filter(u -> u.getNome() != null &&
                        u.getNome().toLowerCase().contains(termo))
                .collect(Collectors.toList());
    }

    public Map<String, Usuario> getUsuarios() {
        return usuariosPorEmail;
    }

    public void limpar() {
        usuariosPorEmail.clear();
        usuariosPorNome.clear();
    }
}

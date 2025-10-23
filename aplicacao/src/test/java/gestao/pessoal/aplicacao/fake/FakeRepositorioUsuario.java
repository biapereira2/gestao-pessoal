package gestao.pessoal.aplicacao.fake;

import gestao.pessoal.compartilhado.RepositorioUsuario;
import gestao.pessoal.compartilhado.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeRepositorioUsuario implements RepositorioUsuario {

    // Mapa principal que usa Email como chave (padrão do seu repositório original)
    private final Map<String, Usuario> usuariosPorEmail = new HashMap<>();

    // Mapa auxiliar que usa Nome como chave (para testes BDD e Desafios)
    private final Map<String, Usuario> usuariosPorNome = new HashMap<>();

    /**
     * Método utilitário crucial para os testes BDD de Desafios.
     * Busca um usuário pelo nome. Se não existir, o cria (simulando um cadastro rápido para o teste).
     */
    public Usuario criarOuBuscarUsuario(String nome) {
        if (usuariosPorNome.containsKey(nome)) {
            return usuariosPorNome.get(nome);
        }

        // Simulação: Cria um usuário.
        // Usa o construtor real da Entidade Usuario (nome, email, senha).
        // Fornece um email e senha mockados, já que o construtor exige.
        String emailMock = nome.toLowerCase().replace(" ", "") + "@mock.com";
        String senhaMock = "SenhaSegura123";

        Usuario novoUsuario = new Usuario(nome, emailMock, senhaMock);

        salvar(novoUsuario);
        return novoUsuario;
    }

    @Override
    public void salvar(Usuario usuario) {
        // Salva nos dois mapas
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

    public Map<String, Usuario> getUsuarios() {
        return usuariosPorEmail;
    }

    public void limpar() {
        usuariosPorEmail.clear();
        usuariosPorNome.clear();
    }
}

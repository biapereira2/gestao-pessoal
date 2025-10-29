package gestao.pessoal.aplicacao;

import java.util.UUID;

// Record simples para transportar dados do amigo
public record AmigoDTO(UUID id, String nome) {}
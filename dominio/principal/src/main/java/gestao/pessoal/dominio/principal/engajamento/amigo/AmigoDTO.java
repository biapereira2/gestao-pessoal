package gestao.pessoal.dominio.principal.engajamento.amigo;

import java.util.UUID;

// Record simples para transportar dados do amigo
public record AmigoDTO(UUID id, String nome) {}
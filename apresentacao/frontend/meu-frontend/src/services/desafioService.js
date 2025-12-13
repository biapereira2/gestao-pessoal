// src/services/desafioService.js
const API_URL = "http://localhost:8080/desafios";

export const desafioService = {

  // === 1. CRIAR DESAFIO (POST /desafios) ===
  criar: async (dadosDesafio) => {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dadosDesafio),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Erro ao criar desafio.");
    }
    return await response.json();
  },

  // === 2. LISTAR CONVITES PENDENTES (GET /desafios/convites/pendentes/{usuarioId}) ===
  listarConvitesPendentes: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/convites/pendentes/${usuarioId}`);

      if (!response.ok) {
        console.error("Erro ao listar convites:", response.statusText);
        // Lembre-se: O erro 500 de mapeamento pode ocorrer aqui!
        return [];
      }
      return await response.json();
    } catch (error) {
      console.error("Erro de rede ao listar convites:", error);
      return [];
    }
  },

  // === 3. ACEITAR CONVITE (POST /desafios/convites/{conviteId}/aceitar?usuarioId={usuarioId}) ===
  aceitarConvite: async (conviteId, usuarioId) => {
    const response = await fetch(`${API_URL}/convites/${conviteId}/aceitar?usuarioId=${usuarioId}`, {
      method: "POST",
      // Não precisa de body, apenas o ID na URL
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Erro ao aceitar convite.");
    }
    // Retorna true ou o JSON se o backend retornar algo
    return response.status === 204 ? true : await response.json();
  },

  // === 4. LISTAR MEUS DESAFIOS (GET /desafios/usuario/{usuarioId}) ===
  listarMeusDesafios: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}`);

      if (!response.ok) {
        console.error("Erro ao listar desafios do usuário:", response.statusText);
        return [];
      }
      return await response.json();
    } catch (error) {
      console.error("Erro de rede ao listar desafios:", error);
      return [];
    }
  },

  // === 5. ACOMPANHAR PROGRESSO (GET /desafios/{desafioId}/progresso) ===
  acompanharProgresso: async (desafioId) => {
    try {
      const response = await fetch(`${API_URL}/${desafioId}/progresso`);

      if (!response.ok) {
        console.error("Erro ao carregar progresso:", response.statusText);
        return null;
      }
      return await response.json();
    } catch (error) {
      console.error("Erro de rede ao carregar progresso:", error);
      return null;
    }
  },

    // === 6. ENCERRAR DESAFIO (POST /desafios/{desafioId}/encerrar?criadorId={criadorId}) ===
    encerrarDesafio: async (desafioId, criadorId) => {
      // URL: http://localhost:8080/desafios/{desafioId}/encerrar?criadorId={criadorId}
      const response = await fetch(`${API_URL}/${desafioId}/encerrar?criadorId=${criadorId}`, {
        method: "POST", // A Controller espera um POST
      });

      if (!response.ok) {
        // O backend retorna 400 se o usuário não for o criador, ou se houver outro erro
        const errorText = await response.text();
        // O erro 400 (Bad Request) deve ser capturado e relançado para o Front-end
        throw new Error(errorText || `Erro ${response.status} ao encerrar desafio.`);
      }
      // Retorna true pois a Controller retorna ResponseEntity.ok().build() (que é um 200/204)
      return true;
    },

    // === 7. NOVO: SAIR DO DESAFIO (Para uso futuro) ===
    // Note: O endpoint DELETE é usado para sair do desafio
    sairDoDesafio: async (desafioId, participanteId) => {
      const response = await fetch(`${API_URL}/${desafioId}/sair?participanteId=${participanteId}`, {
          method: "DELETE",
      });

      if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || `Erro ${response.status} ao sair do desafio.`);
      }
      return true;
    }
};
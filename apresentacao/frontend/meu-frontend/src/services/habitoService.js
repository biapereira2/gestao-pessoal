// habitoService.js

const API_URL = "http://localhost:8080/habitos";

export const habitoService = {
  // FUNÇÃO ORIGINAL: Assume-se que esta retorna APENAS os hábitos CONCLUÍDOS hoje
  listarPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}/com-checkin`);
      if (!response.ok) return [];
      // Retorna APENAS os hábitos que fizeram check-in hoje
      return await response.json(); 
    } catch (error) {
      console.error("Erro ao listar hábitos concluídos:", error);
      return [];
    }
  },
  
  // **NOVA FUNÇÃO ADICIONADA:** Para buscar TODOS os hábitos cadastrados pelo usuário
  listarTodosPorUsuario: async (usuarioId) => {
    try {
      // Assumindo um endpoint que retorna TODOS os hábitos do usuário
      const response = await fetch(`${API_URL}/usuario/${usuarioId}`); 
      if (!response.ok) return [];
      return await response.json();
    } catch (error) {
      console.error("Erro ao listar todos os hábitos:", error);
      return [];
    }
  },

  criar: async (habitoDados) => {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(habitoDados),
    });
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
  },

  atualizar: async (id, habitoDados) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(habitoDados),
    });
    if (!response.ok) throw new Error(await response.text() || "Erro ao atualizar hábito");
    return await response.json();
  },

  remover: async (id) => {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) throw new Error("Erro ao remover hábito");
    return true;
  },

  marcarCheckin: async (habitoId, usuarioId, data) => {
    const response = await fetch(`${API_URL}/${habitoId}/checkin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ usuarioId, data }),
    });
    if (!response.ok) throw new Error(await response.text() || "Erro ao marcar check-in.");

    return await response.json(); // Retorna hábito atualizado (provavelmente com fezCheckinHoje: true)
  },
};
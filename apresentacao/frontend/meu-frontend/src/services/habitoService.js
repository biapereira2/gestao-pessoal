const API_URL = "http://localhost:8080/habitos";

export const habitoService = {
  listarPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}/com-checkin`);
      if (!response.ok) return [];
      return await response.json(); 
    } catch (error) {
      console.error("Erro ao listar hábitos concluídos:", error);
      return [];
    }
  },

  listarTodosPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}`);
      if (!response.ok) return [];
      return await response.json();
    } catch (error) {
      console.error("Erro ao listar todos os hábitos:", error);
      return [];
    }
  },

  obterPorId: async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}`);
      if (!response.ok) return null;
      return await response.json();
    } catch (error) {
      console.error(`Erro ao obter hábito ${id}:`, error);
      return null;
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
    return await response.json();
  },

  obterResumoExpandido: async (id) => {
    try {
      const response = await fetch(`${API_URL}/${id}/resumo-expandido`);
      if (!response.ok) throw new Error("Erro ao obter resumo expandido");
      return await response.json();
    } catch (error) {
      console.error(`Erro ao obter resumo expandido do hábito ${id}:`, error);
      return null;
    }
  }
};

const API_URL = "http://localhost:8080/habitos";

export const habitoService = {

  listarPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/usuario/${usuarioId}/expandido`);
      if (!response.ok) return [];
      return await response.json();
    } catch (error) {
      console.error("Erro ao listar hábitos:", error);
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

    if (!response.ok) {
      throw new Error(await response.text() || "Erro ao atualizar hábito");
    }
    return await response.json();
  },

  remover: async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) throw new Error("Erro ao remover hábito");
    return true;
  }
};
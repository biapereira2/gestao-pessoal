const API_URL = "http://localhost:8080/alertas";

export const alertaService = {
  listarPorUsuario: async (usuarioId) => {
    try {
      const response = await fetch(
        `${API_URL}/usuario/${usuarioId}/expandido`
      );
      if (!response.ok) return [];
      return await response.json();
    } catch (error) {
      console.error("Erro ao listar alertas:", error);
      return [];
    }
  },

  criar: async (dados) => {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados)
    });

    if (!response.ok) throw new Error(await response.text());
    return await response.json();
  },

  atualizar: async (id, dados) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dados)
    });

    if (!response.ok) throw new Error(await response.text());
    return await response.json();
  },

  remover: async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE"
    });

    if (!response.ok) throw new Error("Erro ao remover alerta");
    return true;
  }
};

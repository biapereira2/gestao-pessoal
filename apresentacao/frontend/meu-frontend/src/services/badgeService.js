const API_URL = "http://localhost:8080/badges";

export const badgeService = {

  listarPorUsuario: async (usuarioId) => {
    const response = await fetch(`${API_URL}/usuario/${usuarioId}`);
    if (!response.ok) {
      throw new Error("Erro ao buscar badges");
    }
    return await response.json();
  }

};

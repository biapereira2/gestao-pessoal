const API_URL = "http://localhost:8080/api/social";

export const socialService = {


  listarAmigos: async (usuarioId) => {
    try {
      const response = await fetch(`${API_URL}/${usuarioId}/amigos`);
      if (!response.ok) throw new Error("Erro ao buscar amigos");
      return await response.json();
    } catch (error) {
      console.error(error);
      return [];
    }
  },

  adicionarAmigo: async (usuarioId, amigoId) => {
    try {
      const response = await fetch(`${API_URL}/${usuarioId}/amigos`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ amigoId: amigoId }),
      });

      if (!response.ok) {
         const errorText = await response.text();
         throw new Error(errorText || "Erro ao adicionar amigo");
      }
      return true;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  removerAmigo: async (usuarioId, amigoId) => {
    try {
      const response = await fetch(`${API_URL}/${usuarioId}/amigos/${amigoId}`, {
        method: "DELETE",
      });
      if (!response.ok) throw new Error("Erro ao remover amigo");
      return true;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  pesquisarUsuarios: async (termo) => {
      if (!termo) return [];
      const response = await fetch(`${API_URL}/pesquisar?termo=${termo}`);
      if (!response.ok) return [];
      return await response.json();
    }
};
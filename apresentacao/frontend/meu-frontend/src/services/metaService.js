// src/services/metaService.js
const API_URL = "http://localhost:8080/metas";

export const metaService = {

  listarResumosExpandido: async (usuarioId) => {
    try {
      // O endpoint /expandido é usado para obter os dados de progresso
      const response = await fetch(`${API_URL}/usuario/${usuarioId}/expandido`);
      if (!response.ok) {
          // Em caso de erro, retorna array vazio e loga o erro
          console.error("Erro ao listar metas expandidas:", response.statusText);
          return [];
      }
      return await response.json();
    } catch (error) {
      console.error("Erro de rede ao listar metas:", error);
      return [];
    }
  },

  criar: async (metaDados) => {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(metaDados),
    });
    if (!response.ok) throw new Error(await response.text() || "Erro ao criar meta");
    return await response.json();
  },

  atualizar: async (id, metaDados) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(metaDados),
    });

    if (!response.ok) {
      throw new Error(await response.text() || "Erro ao atualizar meta");
    }
    return await response.json();
  },

  remover: async (id) => {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
    });
    if (response.status === 404) throw new Error("Meta não encontrada");
    if (!response.ok) throw new Error("Erro ao remover meta");
    return true;
  }
};
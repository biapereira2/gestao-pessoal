const API_URL = "http://localhost:8080/progresso";

export const progressoUsuarioService = {

  buscarPorUsuario: async (usuarioId) => {
    const response = await fetch(`${API_URL}/usuario/${usuarioId}`);
    if (!response.ok) {
      throw new Error("Erro ao buscar progresso do usuário");
    }
    return await response.json();
  }

};

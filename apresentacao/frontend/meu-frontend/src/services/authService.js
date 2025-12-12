// src/services/authService.js
const API_URL = "http://localhost:8080/usuarios";

export const authService = {

  login: async (email, senha) => {
    const response = await fetch(`${API_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      // Envia email e senha no corpo, conforme esperado pelo UsuarioForm
      body: JSON.stringify({ email, senha }),
    });

    if (response.status === 401) {
      throw new Error("Credenciais inválidas. Verifique seu email e senha.");
    }

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Erro desconhecido ao realizar login.");
    }

    // Retorna o objeto Usuario
    return await response.json();
  },

  cadastro: async (nome, email, senha) => {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      // Envia nome, email e senha no corpo, conforme esperado pelo UsuarioForm
      body: JSON.stringify({ nome, email, senha }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      // Assume-se que 400 Bad Request pode ser por email repetido ou campos inválidos
      throw new Error(errorText || "Falha ao cadastrar. O email pode já estar em uso.");
    }

    // Retorna o objeto Usuario criado
    return await response.json();
  }
};
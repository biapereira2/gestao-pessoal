import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../css/auth.css';
import api from "../services/api";

const Home = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);

  const handleEntrar = async () => {
    if (!email || !senha) {
      alert("Preencha email e senha!");
      return;
    }

    setLoading(true);
    try {
      const response = await api.post("/usuarios/login", {
        email,
        senha
        // nome é ignorado no backend
      });

      console.log("Usuário logado:", response.data);
      alert(`Bem-vindo, ${response.data.nome}!`);
      localStorage.setItem("usuario", JSON.stringify(response.data));
      navigate(`/dashboard/${response.data.id}`);


    } catch (error) {
      if (error.response) {
        if (error.response.status === 401) {
          alert("Email ou senha incorretos!");
        } else {
          alert(`Erro no servidor: ${error.response.status}`);
        }
      } else if (error.request) {
        alert("Não foi possível conectar ao servidor. Verifique se o backend está rodando.");
      } else {
        alert("Erro inesperado: " + error.message);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCadastro = () => {
    navigate("/cadastro");
  };

  return (
    <div className="container">
      <h1>Gerenciador de Hábitos</h1>
      <p>Acompanhe suas metas diárias e semanais de forma simples e eficiente.</p>

      <div className="card">
        <input
          type="email"
          placeholder="Seu email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Sua senha"
          value={senha}
          onChange={(e) => setSenha(e.target.value)}
        />

        <button
          className="login-btn"
          onClick={handleEntrar}
          disabled={loading}
        >
          {loading ? "Entrando..." : "Entrar"}
        </button>
        <button className="signup-btn" onClick={handleCadastro}>
          Criar conta
        </button>
      </div>
    </div>
  );
};

export default Home;

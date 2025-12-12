import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/base.css";

const Home = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");

  const handleEntrar = () => {
    navigate("/dashboard"); // redireciona sem validar
  };

  const handleCadastro = () => {
    navigate("/cadastro"); // redireciona para cadastro
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

        <button className="login-btn" onClick={handleEntrar}>
          Entrar
        </button>
        <button className="signup-btn" onClick={handleCadastro}>
          Criar conta
        </button>
      </div>
    </div>
  );
};

export default Home;

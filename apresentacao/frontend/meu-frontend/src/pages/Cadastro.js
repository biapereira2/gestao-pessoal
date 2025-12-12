import React from "react";
import { useNavigate } from "react-router-dom";
import "../css/base.css";

const Cadastro = () => {
  const navigate = useNavigate();

  return (
    <div className="container">
      <h1>Cadastro</h1>
      <p>Página de cadastro de usuário.</p>
      <button className="login-btn" onClick={() => navigate("/")}>
        Voltar
      </button>
    </div>
  );
};

export default Cadastro;

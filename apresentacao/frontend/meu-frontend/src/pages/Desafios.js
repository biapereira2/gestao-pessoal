import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Desafios = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Desafios</h1>
      <p>Página de desafios do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Desafios;

import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Habitos = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Hábitos</h1>
      <p>Página de hábitos do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Habitos;

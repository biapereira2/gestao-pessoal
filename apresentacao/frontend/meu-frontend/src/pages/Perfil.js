import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Perfil = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Perfil</h1>
      <p>Página de perfil do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard:id")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Perfil;

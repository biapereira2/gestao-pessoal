import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Amigos = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Amigos</h1>
      <p>Página de amigos do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Amigos;

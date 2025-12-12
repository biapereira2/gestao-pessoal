import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Rotinas = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Rotinas</h1>
      <p>Página de rotinas do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard:id")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Rotinas;

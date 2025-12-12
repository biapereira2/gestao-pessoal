import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Metas = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Metas</h1>
      <p>Página de metas do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Metas;

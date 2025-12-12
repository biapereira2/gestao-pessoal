import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Alertas = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Alertas</h1>
      <p>Página de alertas e lembretes do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Alertas;

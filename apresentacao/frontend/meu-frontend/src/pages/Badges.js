import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Badges = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Badges</h1>
      <p>Página de conquistas do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard:id")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Badges;

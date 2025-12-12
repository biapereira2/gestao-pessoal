import React from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Checkins = () => {
  const navigate = useNavigate();

  return (
    <DashboardLayout>
      <h1>Check-ins</h1>
      <p>Página de check-ins do usuário.</p>
      <button className="login-btn" onClick={() => navigate("/dashboard:id")}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Checkins;

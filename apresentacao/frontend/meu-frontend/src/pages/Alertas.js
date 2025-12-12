import React from "react";
import { useNavigate , useParams } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Alertas = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  return (
    <DashboardLayout>
      <h1>Alertas</h1>
      <p>Página de alertas e lembretes do usuário.</p>
      <button className="login-btn" onClick={() => navigate(`/dashboard/${id}`)}>
        Voltar
      </button>
    </DashboardLayout>
  );
};

export default Alertas;

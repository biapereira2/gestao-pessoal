import React from "react";
import { useParams } from "react-router-dom";
import DashboardLayout from "../components/DashboardLayout";

const Dashboard = () => {
  const { id } = useParams(); // Pega o id da URL

  return (
    <DashboardLayout>
      <h1>Bem-vindo ao seu Gerenciador de Hábitos!</h1>
      <p>Aqui você verá suas metas diárias e semanais.</p>
    </DashboardLayout>
  );
};

export default Dashboard;

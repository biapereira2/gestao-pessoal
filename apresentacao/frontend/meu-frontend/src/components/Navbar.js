import React from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import "../css/base.css";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();

  const usuario =
    JSON.parse(localStorage.getItem("usuario")) ||
    { nome: "Visitante", id };

  const isActive = (path) =>
    location.pathname.startsWith(path) ? "active" : "";

  const menuItems = [
    { label: "Rotinas", path: `/rotinas/${usuario.id}` },
    { label: "Metas", path: `/metas/${usuario.id}` },
    { label: "Hábitos", path: `/habitos/${usuario.id}` },
    { label: "Desafios", path: `/desafios/${usuario.id}` },
    { label: "Check-ins", path: `/checkins/${usuario.id}` },
    { label: "Alertas", path: `/alertas/${usuario.id}` },
    { label: "Amigos", path: `/social/${usuario.id}` },

    // 🎮 Gamificação
    { label: "Progresso", path: `/progresso/${usuario.id}` },
  ];

  return (
    <nav className="sidebar">
      <div
        className="logo"
        onClick={() => navigate(`/dashboard/${usuario.id}`)}
      >
        Gestão Pessoal
      </div>

      <ul>
        {menuItems.map((item) => (
          <li
            key={item.path}
            className={isActive(item.path)}
            onClick={() => navigate(item.path)}
          >
            {item.label}
          </li>
        ))}
      </ul>

      {/* Botão de voltar */}
      <div className="back-button" onClick={() => navigate("/")}>
        <div className="back-icon">←</div>
        <div>Voltar</div>
      </div>
    </nav>
  );
};

export default Navbar;

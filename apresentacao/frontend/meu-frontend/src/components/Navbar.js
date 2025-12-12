import React from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../css/base.css";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const usuario = JSON.parse(localStorage.getItem("usuario")) || { nome: "Visitante" };


  const isActive = (path) => location.pathname.startsWith(path) ? "active" : "";

  const menuItems = [
    { label: "Rotinas", path: "/rotinas" },
    { label: "Metas", path: "/metas" },
    { label: "Hábitos", path: "/habitos" },
    { label: "Desafios", path: "/desafios" },
    { label: "Check-ins", path: "/checkins" },
    { label: "Alertas", path: "/alertas" },
    { label: "Amigos", path: "/social" },
    { label: "Badges", path: "/badges" },
  ];

  return (
    <nav className="sidebar">
      <div className="logo" onClick={() => navigate("/dashboard")}>
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

      <div className="profile" onClick={() => navigate("/perfil")}>
        <div style={{
          width: '40px',
          height: '40px',
          borderRadius: '50%',
          backgroundColor: '#2a2938',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontWeight: 'bold'
        }}>
          {usuario.nome.charAt(0).toUpperCase()}
        </div>
        <div>{usuario.nome}</div>
      </div>
    </nav>
  );
};

export default Navbar;
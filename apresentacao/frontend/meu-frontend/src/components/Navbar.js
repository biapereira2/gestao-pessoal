import React from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import "../css/base.css";

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams(); // pega o id da URL atual

  // Se não tiver id, tenta pegar do localStorage
  const usuario = JSON.parse(localStorage.getItem("usuario")) || { nome: "Visitante", id: id };

  const isActive = (path) => location.pathname.startsWith(path) ? "active" : "";

  // Usa o id da URL para todas as rotas
  const menuItems = [
    { label: "Rotinas", path: `/rotinas/${usuario.id}` },
    { label: "Metas", path: `/metas/${usuario.id}` },
    { label: "Hábitos", path: `/habitos/${usuario.id}` },
    { label: "Desafios", path: `/desafios/${usuario.id}` },
    { label: "Check-ins", path: `/checkins/${usuario.id}` },
    { label: "Alertas", path: `/alertas/${usuario.id}` },
    { label: "Amigos", path: `/social/${usuario.id}` },
    { label: "Badges", path: `/badges/${usuario.id}` },
  ];

  return (
    <nav className="sidebar">
      <div className="logo" onClick={() => navigate(`/dashboard/${usuario.id}`)}>
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

      <div className="profile" onClick={() => navigate(`/perfil/${usuario.id}`)}>
        <div style={{
          width: '40px',
          height: '40px',
          borderRadius: '40%',
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

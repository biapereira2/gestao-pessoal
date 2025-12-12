import React from "react";
import { useNavigate } from "react-router-dom";
import "../css/base.css";

const Navbar = () => {
  const navigate = useNavigate();

  const usuario = JSON.parse(localStorage.getItem("usuario")) || { nome: "Perfil" };

  return (
    <div className="sidebar">
      <div className="logo" onClick={() => navigate("/dashboard")}>
        Gerenciador
      </div>

      <ul>
        <li onClick={() => navigate("/rotinas")}>Rotinas</li>
        <li onClick={() => navigate("/metas")}>Metas</li>
        <li onClick={() => navigate("/habitos")}>Hábitos</li>
        <li onClick={() => navigate("/desafios")}>Desafios</li>
        <li onClick={() => navigate("/checkins")}>Check-ins</li>
        <li onClick={() => navigate("/alertas")}>Alertas</li>
        <li onClick={() => navigate("/amigos")}>Amigos</li>
        <li onClick={() => navigate("/badges")}>Badges</li>
      </ul>

      <div className="profile" onClick={() => navigate("/perfil")}>
        <img src="https://via.placeholder.com/50" alt="avatar" />
        <div>{usuario.nome}</div>
      </div>
    </div>
  );
};

export default Navbar;

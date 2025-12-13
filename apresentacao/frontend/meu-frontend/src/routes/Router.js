// frontend/meu-frontend/src/routes/Router.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Home from '../pages/Home';
import Cadastro from '../pages/Cadastro';
import Dashboard from '../pages/Dashboard';

import Rotinas from '../pages/Rotinas';
import Metas from '../pages/Metas';
import Habitos from '../pages/Habitos';
import Desafios from '../pages/Desafios';
import Checkins from '../pages/Checkins';
import Alertas from '../pages/Alertas';
import Amigos from '../pages/Amigos';
import Badges from '../pages/Badges';
import Perfil from '../pages/Perfil';
import Progresso from '../pages/Progresso'; // NOVO

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        {/* Home e Cadastro */}
        <Route path="/" element={<Home />} />
        <Route path="/cadastro" element={<Cadastro />} />

        {/* Dashboard */}
        <Route path="/dashboard/:id" element={<Dashboard />} />

        {/* Páginas do Navbar */}
        <Route path="/rotinas/:id" element={<Rotinas />} />
        <Route path="/metas/:id" element={<Metas />} />
        <Route path="/habitos/:id" element={<Habitos />} />
        <Route path="/desafios/:id" element={<Desafios />} />
        <Route path="/checkins/:id" element={<Checkins />} />
        <Route path="/alertas/:id" element={<Alertas />} />
        <Route path="/social/:id" element={<Amigos />} />

        {/* Gamificação */}
        <Route path="/progresso/:id" element={<Progresso />} /> {/* 🧠 */}
        <Route path="/badges/:id" element={<Badges />} />

        <Route path="/perfil/:id" element={<Perfil />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;

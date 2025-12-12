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

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        {/* Home e Cadastro */}
        <Route path="/" element={<Home />} />
        <Route path="/cadastro" element={<Cadastro />} />

        {/* Dashboard */}
        <Route path="/dashboard" element={<Dashboard />} />

        {/* Páginas do Navbar */}
        <Route path="/rotinas" element={<Rotinas />} />
        <Route path="/metas" element={<Metas />} />
        <Route path="/habitos" element={<Habitos />} />
        <Route path="/desafios" element={<Desafios />} />
        <Route path="/checkins" element={<Checkins />} />
        <Route path="/alertas" element={<Alertas />} />
        <Route path="/amigos" element={<Amigos />} />
        <Route path="/badges" element={<Badges />} />
        <Route path="/perfil" element={<Perfil />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;

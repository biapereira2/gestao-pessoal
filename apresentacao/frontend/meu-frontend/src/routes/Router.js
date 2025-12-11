// frontend/meu-frontend/src/routes/Router.js

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Home from '../pages/Home'
import Cadastro from '../pages/Cadastro';
import PagInicial from '../pages/PagInicial'

const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/cadastro" element={<Cadastro />} />
        <Route path="/" element={<Home />} />
        <Route path="/pag-inicial" element={<PagInicial />} />

      </Routes>
    </Router>
  );
};

export default AppRouter;
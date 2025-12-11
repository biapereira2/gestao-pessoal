// Assumindo que este é o componente que você quer centralizar o botão
// Nome do arquivo: frontend/meu-frontend/src/pages/Home.js
// ou o seu frontend/meu-frontend/src/pages/Login.js

import React from 'react';
import { useNavigate } from 'react-router-dom'; // Importar hook de navegação

const LoginScreen = () => {
  // Hook para navegação segura no react-router-dom v6+
  const navigate = useNavigate();

  const goToCadastro = () => {
    navigate('/cadastro');
  };

  return (
    // 1. O div pai deve ter altura total da viewport (100vh)
    // 2. Usamos display: flex, justify-content: center e align-items: center
    //    para centralizar o conteúdo horizontal e verticalmente.
    <div 
      style={{ 
        display: 'flex', 
        flexDirection: 'column', 
        justifyContent: 'center', 
        alignItems: 'center', 
        height: '100vh', 
        textAlign: 'center' 
      }}
    >
      <h1>Bem-vindo(a)</h1>
      <p>Faça login ou cadastre-se para continuar.</p>
      
      {/* O SEU BOTÃO CENTRALIZADO */}
      <button 
        onClick={goToCadastro}
        style={{ 
          padding: '10px 20px', 
          fontSize: '16px', 
          cursor: 'pointer',
          marginTop: '20px' // Espaçamento se houver outro conteúdo
        }}
      >
        Ir para Cadastro
      </button>

    </div>
  );
};

export default LoginScreen;
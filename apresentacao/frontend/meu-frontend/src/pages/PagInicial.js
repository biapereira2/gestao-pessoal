import React from 'react';
import { useNavigate } from 'react-router-dom';

const LoginScreen = () => {
  const navigate = useNavigate();

  const goToHome = () => {
    navigate('/');
  };

  const goToPagInicial = () => {
      navigate('/pag-inicial');
    };

  return (
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
      <h1>Pagina Inicial</h1>

      <button
        onClick={goToHome}
        style={{
          padding: '10px 20px',
          fontSize: '16px',
          cursor: 'pointer',
          marginTop: '20px'
        }}
      >
        Ir para Login
      </button>

      <button
              onClick={goToPagInicial}
              style={{
                padding: '10px 20px',
                fontSize: '16px',
                cursor: 'pointer',
                marginTop: '20px'
              }}
            >
              Ir para PagInicial
            </button>

    </div>
  );
};

export default LoginScreen;
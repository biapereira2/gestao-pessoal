// src/pages/Cadastro.js
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { authService } from '../services/authService';
import '../css/cadastro.css'; // <-- Use o novo arquivo de teste

const Cadastro = () => {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: ''
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { nome, email, senha, confirmarSenha } = formData;

    if (!nome || !email || !senha || !confirmarSenha) {
      toast.warn("Todos os campos são obrigatórios.");
      return;
    }

    // A validação de min 6 caracteres na senha é feita pelo backend, mas é bom pré-validar
    if (senha.length < 6) {
      toast.error("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (senha !== confirmarSenha) {
      toast.error("As senhas digitadas não são iguais.");
      return;
    }
    
    setLoading(true);
    try {
      await authService.cadastro(nome, email, senha);
      
      toast.success("Cadastro realizado com sucesso! Faça login para começar.");
      // Redireciona para a tela de Login
      navigate('/');

    } catch (error) {
      // Usa a mensagem de erro específica do service (400)
      toast.error(error.message || "Falha ao cadastrar.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page-container">
      <div className="auth-card">
        
        <h1 className="auth-title">Crie sua conta</h1>
        <p className="auth-subtitle">Junte-se a nós para transformar seus hábitos e alcançar seus objetivos.</p>

        <form onSubmit={handleSubmit}>
          <div className="form-group-auth">
            <label htmlFor="nome">Nome Completo</label>
            <input
              type="text"
              id="nome"
              className="input-auth"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
              placeholder="Seu nome"
            />
          </div>

          <div className="form-group-auth">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              className="input-auth"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="seuemail@exemplo.com"
            />
          </div>

          <div className="form-group-auth">
            <label htmlFor="senha">Senha</label>
            <input
              type="password"
              id="senha"
              className="input-auth"
              name="senha"
              value={formData.senha}
              onChange={handleChange}
              placeholder="Crie uma senha forte (mínimo 6 caracteres)"
            />
          </div>

          <div className="form-group-auth">
            <label htmlFor="confirmarSenha">Confirme a Senha</label>
            <input
              type="password"
              id="confirmarSenha"
              className="input-auth"
              name="confirmarSenha"
              value={formData.confirmarSenha}
              onChange={handleChange}
              placeholder="Repita a senha"
            />
          </div>

          <button 
            type="submit" 
            className="btn-primary-auth" 
            disabled={loading}
          >
            {loading ? 'Cadastrando...' : 'Cadastrar'}
          </button>
        </form>

        <Link to="/" className="link-auth">
          Já tem conta? Faça login.
        </Link>
      </div>
    </div>
  );
};

export default Cadastro;
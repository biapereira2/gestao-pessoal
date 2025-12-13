import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import { desafioService } from '../services/desafioService';
import { habitoService } from '../services/habitoService'; // Necessário para carregar hábitos disponíveis
import { toast } from 'react-toastify';
import CriarDesafioModal from '../components/Desafios/CriarDesafioModal';
import ConviteCard from '../components/Desafios/ConviteCard'; // Criar este componente
import DesafioCard from '../components/Desafios/DesafioCard'; // Criar este componente
import '../css/desafios.css'; // Assumindo que você terá um CSS específico

const Desafios = () => {
  const { id } = useParams(); // ID do usuário logado
  const usuarioId = id;

  const [desafiosAtivos, setDesafiosAtivos] = useState([]);
  const [convitesPendentes, setConvitesPendentes] = useState([]);
  const [habitosDisponiveis, setHabitosDisponiveis] = useState([]);
  const [loading, setLoading] = useState(true);

  const [modalCriarAberto, setModalCriarAberto] = useState(false);

  // A função carregarDados é perfeita para ser usada como função de recarga!
  const carregarDados = async () => {
    setLoading(true);
    try {
      // Carregar Convites Pendentes (Teste 2)
      const convites = await desafioService.listarConvitesPendentes(usuarioId);
      setConvitesPendentes(convites);

      // Carregar Desafios Ativos (Teste 4, 5...)
      const desafios = await desafioService.listarMeusDesafios(usuarioId);
      setDesafiosAtivos(desafios);

      // Carregar Hábitos para o modal de criação (Teste 1)
      const habitos = await habitoService.listarPorUsuario(usuarioId);
      setHabitosDisponiveis(habitos);

    } catch (error) {
      console.error("Erro ao carregar dados de desafios:", error);
      toast.error("Erro ao carregar desafios ou convites.");
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    if (usuarioId) {
      carregarDados();
    } else {
      setLoading(false);
    }
  }, [usuarioId]);


  const handleCriarDesafio = async (dadosForm) => {
    try {
      await desafioService.criar({ ...dadosForm, criadorId: usuarioId });
      toast.success("Desafio e convites criados com sucesso!");
      setModalCriarAberto(false);
      carregarDados(); // Recarrega para ver o desafio recém-criado
    } catch (error) {
      toast.error("Erro ao criar desafio: " + error.message);
    }
  };

  const handleAceitarConvite = async (conviteId) => {
    try {
      // Teste 3: Aceitar Convite
      await desafioService.aceitarConvite(conviteId, usuarioId);
      toast.success("Convite aceito! Você se juntou ao desafio.");
      carregarDados(); // Recarrega para mover o desafio para a lista de ativos
    } catch (error) {
      toast.error("Erro ao aceitar convite: " + error.message);
    }
  };

  return (
    <DashboardLayout>
      <div className="desafios-page" style={{ padding: '0 20px' }}>
        <h1 style={{ fontWeight: 800 }}>Desafios e Competições</h1>
        <p style={{ color: 'var(--text-secondary)' }}>Participe de desafios de hábitos com seus amigos.</p>

        {/* === SEÇÃO DE CONVITES PENDENTES === */}
        {convitesPendentes.length > 0 && (
          <div style={{ marginBottom: '30px' }}>
            <h2 style={{ fontSize: '20px', fontWeight: 800, color: 'var(--text-primary)', marginBottom: '15px' }}>
              Convites ({convitesPendentes.length})
            </h2>
            <div className="convites-list">
              {convitesPendentes.map(convite => (
                <ConviteCard
                  key={convite.id}
                  convite={convite}
                  onAceitar={handleAceitarConvite}
                />
              ))}
            </div>
          </div>
        )}

        {/* === SEÇÃO DE DESAFIOS ATIVOS === */}
        <div style={{ marginBottom: '30px' }}>
          <h2 style={{ fontSize: '20px', fontWeight: 800, color: 'var(--text-primary)', marginBottom: '15px' }}>
            Meus Desafios Ativos ({desafiosAtivos.length})
          </h2>
          <button className="btn-primary-habito" onClick={() => setModalCriarAberto(true)}>
            + Criar Novo Desafio
          </button>

          <div className="desafios-list" style={{ marginTop: '20px' }}>
            {loading && <p>Carregando desafios...</p>}
            {!loading && desafiosAtivos.length === 0 && <p>Você não está participando de nenhum desafio. Crie um ou aceite um convite!</p>}

            {desafiosAtivos.map(desafio => (
              <DesafioCard
                key={desafio.id}
                desafio={desafio}
                usuarioId={usuarioId}
                onDesafioEncerrado={carregarDados} // <<< CORREÇÃO PRINCIPAL AQUI!
              />
            ))}
          </div>
        </div>

        {/* MODAL DE CRIAÇÃO */}
        {modalCriarAberto && (
          <CriarDesafioModal
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleCriarDesafio}
            habitosDisponiveis={habitosDisponiveis}
          />
        )}
      </div>
    </DashboardLayout>
  );
};

export default Desafios;
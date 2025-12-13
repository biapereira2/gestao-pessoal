import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import HabitoCard from '../components/Habitos/HabitoCard';
import CriarHabitoModal from '../components/Habitos/CriarHabitoModal';
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import EditarHabitoModal from '../components/Habitos/EditarHabitoModal';
import DetalhesHabitoModal from '../components/Habitos/DetalhesHabitoModal';
import { habitoService } from '../services/habitoService';
import { toast } from 'react-toastify';
import '../css/habitos.css';

const Habitos = () => {
  const { id } = useParams(); // 'id' é o ID do usuário
  const [habitos, setHabitos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');

  const [modalCriarAberto, setModalCriarAberto] = useState(false);
  const [modalExclusao, setModalExclusao] = useState({ show: false, habito: null });
  const [modalEditar, setModalEditar] = useState({ show: false, habito: null });
  const [modalDetalhes, setModalDetalhes] = useState({ show: false, habito: null });

  // =======================================================
  // FUNÇÕES DE CARREGAMENTO E CRUD (REQUERIDAS PELOS MODAIS)
  // =======================================================

  // 💡 USAMOS useCallback para evitar warning no useEffect (agora está correto)
  const carregarHabitos = useCallback(async () => {
    if (!id) {
        setLoading(false);
        return;
    }
    try {
      setLoading(true);
      const data = await habitoService.listarPorUsuario(id);
      setHabitos(data);
    } catch (error) {
      console.error("Erro ao carregar hábitos:", error);
      toast.error("Erro ao carregar hábitos. Verifique a conexão com a API.");
    } finally {
      setLoading(false);
    }
  }, [id]);

  // Função de Criação (handleSalvarHabito)
  const handleSalvarHabito = async (dadosForm) => {
    try {
        await habitoService.criar({...dadosForm, usuarioId: id});
        toast.success("Hábito criado com sucesso!");
        setModalCriarAberto(false);
        carregarHabitos();
    } catch (error) {
        toast.error("Erro ao criar hábito: " + error.message);
    }
  };

  // Função de Atualização (handleAtualizarHabito)
  const handleAtualizarHabito = async (habitoId, dadosForm) => {
    try {
        await habitoService.atualizar(habitoId, dadosForm);
        toast.success("Hábito atualizado!");
        setModalEditar({ show: false, habito: null });
        carregarHabitos();
    } catch (error) {
        toast.error("Erro ao atualizar hábito: " + error.message);
    }
  };

  // Função de Remoção (confirmarRemocao)
  const confirmarRemocao = async () => {
    if (!modalExclusao.habito) return;
    try {
        await habitoService.remover(modalExclusao.habito.id);
        toast.info("Hábito removido.");
        setModalExclusao({ show: false, habito: null });
        carregarHabitos();
    } catch (error) {
        toast.error("Erro ao remover hábito: " + error.message);
    }
  };

  // =======================================================
  // FUNÇÃO PARA LIDAR COM O CHECK-IN (NOVA)
  // =======================================================
  const handleCheckinConcluido = (habitoId, isCheckedIn) => {
      // Esta função não faz nada no momento, apenas loga.
      // A lógica de persistência do Checkin está no HabitoCard
      console.log(`[EVENTO] Hábito ${habitoId} foi ${isCheckedIn ? 'marcado' : 'desmarcado'}.`);
      // Não recarregamos tudo aqui para manter a performance,
      // mas se o check-in afetar a contagem de Metas, você pode chamar a função 'carregarMetas()' aqui.
  };

  // 💡 useEffect agora usa a dependência 'carregarHabitos' corretamente
  useEffect(() => {
    carregarHabitos();
  }, [carregarHabitos]);

  const habitosFiltrados = habitos.filter(h =>
    h.nome.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}> {/* Adicionado padding para visualização */}

        {/* ... (Seção de Título e Input/Botão Criar) */}
        <div style={{ marginBottom: '25px' }}>
          <h1 style={{ fontWeight: 800 }}>Meus Hábitos</h1>
          <p style={{ color: 'var(--text-secondary)' }}>Gerencie sua rotina e acompanhe seu progresso diário.</p>
        </div>

        <div className="habitos-header-row">
          <input
            className="search-input-habito"
            placeholder="Digite o título do hábito..."
            value={busca}
            onChange={e => setBusca(e.target.value)}
          />
          <button className="btn-primary-habito" onClick={() => setModalCriarAberto(true)}>
            + Criar novo hábito
          </button>
        </div>

        <div className="habitos-list">
          {loading && <p>Carregando hábitos...</p>}
          {!loading && habitosFiltrados.length === 0 && <p>Nenhum hábito encontrado.</p>}

          {habitosFiltrados.map(habito => (
            <HabitoCard
              key={habito.id}
              habito={habito}
              usuarioId={id}
              onRemover={(h) => setModalExclusao({ show: true, habito: h })}
              onEditar={(h) => setModalEditar({ show: true, habito: h })}
              onVerDetalhes={(h) => setModalDetalhes({ show: true, habito: h })}
              onCheckinConcluido={handleCheckinConcluido}
            />
          ))}
        </div>

        {/* ======================================================= */}
        {/* MODAIS (QUE PRECISAM DAS FUNÇÕES DE CRUD DEFINIDAS ACIMA) */}
        {/* ======================================================= */}

        {modalCriarAberto && (
          <CriarHabitoModal
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleSalvarHabito} // ✅ Função definida
            usuarioId={id}
          />
        )}

        {modalEditar.show && (
          <EditarHabitoModal
            habito={modalEditar.habito}
            onClose={() => setModalEditar({ show: false, habito: null })}
            onSalvar={handleAtualizarHabito} // ✅ Função definida
          />
        )}

        {modalDetalhes.show && (
          <DetalhesHabitoModal
            habito={modalDetalhes.habito}
            onClose={() => setModalDetalhes({ show: false, habito: null })}
          />
        )}

        <ConfirmacaoModal
          isOpen={modalExclusao.show}
          onClose={() => setModalExclusao({ show: false, habito: null })}
          onConfirm={confirmarRemocao} // ✅ Função definida
          titulo="Excluir hábito?"
          mensagem={`Deseja realmente excluir "${modalExclusao.habito?.nome}"?`}
        />
      </div>
    </DashboardLayout>
  );
};

export default Habitos;
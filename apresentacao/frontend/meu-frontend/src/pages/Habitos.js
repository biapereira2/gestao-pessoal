// Habitos.jsx (COM LÓGICA DE MOVIMENTO IMEDIATO)

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
  const { id } = useParams();
  const [habitos, setHabitos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');

  const [modalCriarAberto, setModalCriarAberto] = useState(false);
  const [modalExclusao, setModalExclusao] = useState({ show: false, habito: null });
  const [modalEditar, setModalEditar] = useState({ show: false, habito: null });
  const [modalDetalhes, setModalDetalhes] = useState({ show: false, habito: null });

  const [loadingCheckinId, setLoadingCheckinId] = useState(null);
  const hoje = new Date().toISOString().split('T')[0];

  const carregarHabitos = useCallback(async () => {
    if (!id) {
        setLoading(false);
        return;
    }
    try {
      setLoading(true);

      // 1. BUSCA HÁBITOS CONCLUÍDOS (seu endpoint /com-checkin)
      const habitosConcluidosHoje = await habitoService.listarPorUsuario(id);
      const idsConcluidos = new Set(habitosConcluidosHoje.map(h => h.id));

      // 2. BUSCA TODOS OS HÁBITOS CADASTRADOS (usando o novo método)
      const todosHabitos = await habitoService.listarTodosPorUsuario(id);

      // 3. CRUZA OS DADOS: Mapeia todos os hábitos e adiciona a propriedade fezCheckinHoje
      const dadosCompletos = todosHabitos.map(habito => ({
          ...habito,
          fezCheckinHoje: idsConcluidos.has(habito.id)
      }));

      setHabitos(dadosCompletos);
    } catch (error) {
      console.error("Erro ao carregar hábitos:", error);
      toast.error("Erro ao carregar hábitos. Verifique a conexão com a API.");
    } finally {
      setLoading(false);
    }
  }, [id]);

  const handleSalvarHabito = async (dadosForm) => {
    try {
        const novoHabito = await habitoService.criar({...dadosForm, usuarioId: id});

        // Novo hábito sempre começa como não feito.
        const habitoComStatus = { ...novoHabito, fezCheckinHoje: false };

        // Atualiza o estado local de forma imutável para aparecer instantaneamente
        setHabitos(prev => [habitoComStatus, ...prev]);

        toast.success("Hábito criado com sucesso!");
        setModalCriarAberto(false);
    } catch (error) {
        toast.error("Erro ao criar hábito: " + error.message);
    }
  };

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

  const confirmarRemocao = async () => {
    if (!modalExclusao.habito) return;
    try {
        await habitoService.remover(modalExclusao.habito.id);
        toast.info("Hábito removido.");
        setModalExclusao({ show: false, habito: null });
        // Otimização: Remove localmente
        setHabitos(prev => prev.filter(h => h.id !== modalExclusao.habito.id));
    } catch (error) {
        toast.error("Erro ao remover hábito: " + error.message);
    }
  };

  const handleToggleCheckin = async (habitoId, fezCheckinHoje) => {
      // Impede check-in duplo
      if (fezCheckinHoje) return;

      setLoadingCheckinId(habitoId);

      try {
          // 1. CHAMA O SERVICE: Registra o check-in no backend
          await habitoService.marcarCheckin(habitoId, id, hoje);

          // 2. ATUALIZA ESTADO LOCAL: Dispara a re-renderização e move o card
          setHabitos(prev =>
            prev.map(h =>
              // Se o ID for o mesmo, cria um NOVO OBJETO com o status alterado
              h.id === habitoId ? { ...h, fezCheckinHoje: true } : h
            )
          );

          toast.success("Hábito concluído!");
      } catch (error) {
          toast.error(error.message || "Erro ao marcar check-in.");
      } finally {
          setLoadingCheckinId(null);
      }
  };

  useEffect(() => {
    carregarHabitos();
  }, [carregarHabitos]);

  // Filtrar busca
  const habitosFiltrados = habitos.filter(h => h.nome.toLowerCase().includes(busca.toLowerCase()));

  // Separação em colunas: Depende do estado 'habitos' ser atualizado no handleToggleCheckin
  const habitosNaoFeitos = habitosFiltrados.filter(h => !h.fezCheckinHoje);
  const habitosFeitos = habitosFiltrados.filter(h => h.fezCheckinHoje);

  return (
    <DashboardLayout>
      <div className="habitos-page" style={{ padding: '0 20px' }}>

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

        {loading && <p>Carregando hábitos...</p>}
        {!loading && habitosFiltrados.length === 0 && <p>Nenhum hábito encontrado.</p>}

        <div className="habitos-list-container">
          <div className="habitos-coluna">
            <h2>Hábitos a fazer ({habitosNaoFeitos.length})</h2>
            {habitosNaoFeitos.map(habito => (
              <HabitoCard
                key={habito.id}
                habito={habito}
                fezCheckinHoje={false}
                onToggleCheckin={handleToggleCheckin}
                isCheckinLoading={loadingCheckinId === habito.id}
                onRemover={(h) => setModalExclusao({ show: true, habito: h })}
                onEditar={(h) => setModalEditar({ show: true, habito: h })}
                onVerDetalhes={(h) => setModalDetalhes({ show: true, habito: h })}
              />
            ))}
          </div>

          <div className="habitos-coluna">
            <h2>Hábitos concluídos ({habitosFeitos.length})</h2>
            {habitosFeitos.map(habito => (
              <HabitoCard
                key={habito.id}
                habito={habito}
                fezCheckinHoje={true}
                onToggleCheckin={handleToggleCheckin}
                isCheckinLoading={loadingCheckinId === habito.id}
                onRemover={(h) => setModalExclusao({ show: true, habito: h })}
                onEditar={(h) => setModalEditar({ show: true, habito: h })}
                onVerDetalhes={(h) => setModalDetalhes({ show: true, habito: h })}
              />
            ))}
          </div>
        </div>

        {/* MODAIS */}
        {modalCriarAberto && (
          <CriarHabitoModal
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleSalvarHabito}
            usuarioId={id}
          />
        )}

        {modalEditar.show && (
          <EditarHabitoModal
            habito={modalEditar.habito}
            onClose={() => setModalEditar({ show: false, habito: null })}
            onSalvar={handleAtualizarHabito}
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
          onConfirm={confirmarRemocao}
          titulo="Excluir hábito?"
          mensagem={`Deseja realmente excluir "${modalExclusao.habito?.nome}"?`}
        />
      </div>
    </DashboardLayout>
  );
};

export default Habitos;
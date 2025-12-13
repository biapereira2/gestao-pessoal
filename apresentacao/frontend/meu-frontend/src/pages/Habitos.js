// Habitos.jsx (Versão com Coluna Única e Ordenação por Status)

import React, { useEffect, useState, useCallback, useMemo } from 'react';
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

      // 1. BUSCA HÁBITOS CONCLUÍDOS HOJE
      const habitosConcluidosHoje = await habitoService.listarPorUsuario(id);
      const idsConcluidos = new Set(habitosConcluidosHoje.map(h => h.id));

      // 2. BUSCA TODOS OS HÁBITOS CADASTRADOS
      const todosHabitos = await habitoService.listarTodosPorUsuario(id);

      // 3. CRUZA OS DADOS: Adiciona a propriedade fezCheckinHoje
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
        const habitoComStatus = { ...novoHabito, fezCheckinHoje: false };
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
        // Atualiza a lista localmente
        setHabitos(prev => prev.map(h =>
          h.id === habitoId ? { ...h, ...dadosForm } : h
        ));
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

          // 2. ATUALIZA ESTADO LOCAL: Marca como feito (a reordenação é automática via useMemo)
          setHabitos(prev =>
            prev.map(h =>
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

  // LÓGICA DE ORDENAÇÃO: Filtrar pela busca e ordenar (Não feito (false) antes de Feito (true))
  const habitosOrdenados = useMemo(() => {
    const filtrados = habitos.filter(h =>
      h.nome.toLowerCase().includes(busca.toLowerCase())
    );

    // Se a.fezCheckinHoje é false (0) e b é true (1), (0 - 1) = -1. 'a' vem antes.
    // Se a.fezCheckinHoje é true (1) e b é false (0), (1 - 0) = 1. 'b' vem antes (a vai para depois).
    return filtrados.sort((a, b) =>
      (a.fezCheckinHoje ? 1 : 0) - (b.fezCheckinHoje ? 1 : 0)
    );
  }, [habitos, busca]);

  const totalNaoFeitos = habitosOrdenados.filter(h => !h.fezCheckinHoje).length;
  const totalFeitos = habitosOrdenados.filter(h => h.fezCheckinHoje).length;

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
        {!loading && habitosOrdenados.length === 0 && <p>Nenhum hábito encontrado.</p>}

        {/* ESTRUTURA DE COLUNA ÚNICA */}
        <div className="habitos-list-container-unica">
          <div className="habitos-coluna-unica">
            <h2>
              Rotina de hoje: {totalNaoFeitos} a fazer / {totalFeitos} concluídos
            </h2>

            {habitosOrdenados.map(habito => (
              <HabitoCard
                key={habito.id}
                habito={habito}
                fezCheckinHoje={habito.fezCheckinHoje}
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
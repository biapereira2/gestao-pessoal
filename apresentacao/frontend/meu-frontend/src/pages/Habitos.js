import React, { useEffect, useState } from 'react';
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

  useEffect(() => {
    carregarHabitos();
  }, [id]);

  const carregarHabitos = async () => {
    try {
      setLoading(true);
      const data = await habitoService.listarPorUsuario(id);
      setHabitos(data);
    } catch (error) {
      toast.error("Erro ao carregar hábitos");
    } finally {
      setLoading(false);
    }
  };

  const handleSalvarHabito = async (dadosForm) => {
    try {
      await habitoService.criar({ ...dadosForm, usuarioId: id });
      toast.success("Hábito criado!");
      setModalCriarAberto(false);
      carregarHabitos();
    } catch (error) {
      toast.error("Erro ao criar: " + error.message);
    }
  };

  const handleAtualizarHabito = async (habitoId, dadosForm) => {
    try {
      await habitoService.atualizar(habitoId, dadosForm);
      toast.success("Hábito atualizado!");
      setModalEditar({ show: false, habito: null });
      carregarHabitos();
    } catch (error) {
      toast.error("Erro ao atualizar: " + error.message);
    }
  };

  const confirmarRemocao = async () => {
    if (!modalExclusao.habito) return;
    try {
      await habitoService.remover(modalExclusao.habito.id);
      setHabitos(habitos.filter(h => h.id !== modalExclusao.habito.id));
      toast.info("Hábito removido.");
      setModalExclusao({ show: false, habito: null });
    } catch (error) {
      toast.error("Erro ao remover.");
    }
  };

  const habitosFiltrados = habitos.filter(h =>
    h.nome.toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <DashboardLayout>
      <div className="habitos-page">
        <div style={{ marginBottom: '25px' }}>
          <h1>Meus Hábitos</h1>
          <p>Gerencie sua rotina e acompanhe seu progresso diário.</p>
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
          {!loading && habitosFiltrados.length === 0 && <p>Nenhum hábito encontrado.</p>}

          {habitosFiltrados.map(habito => (
            <HabitoCard
              key={habito.id}
              habito={habito}
              onRemover={(h) => setModalExclusao({ show: true, habito: h })}
              onEditar={(h) => setModalEditar({ show: true, habito: h })}
              onVerDetalhes={(h) => setModalDetalhes({ show: true, habito: h })}
            />
          ))}
        </div>

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

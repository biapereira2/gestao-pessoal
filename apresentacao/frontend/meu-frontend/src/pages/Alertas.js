// src/pages/Alertas.js
import React, { useEffect, useState, useMemo } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import AlertaCard from '../components/Alertas/AlertaCard';
import CriarAlertaModal from '../components/Alertas/CriarAlertaModal';
import EditarAlertaModal from '../components/Alertas/EditarAlertaModal';
import DetalhesAlertaModal from '../components/Alertas/DetalhesAlertaModal';
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import { alertaService } from '../services/alertaService';
import { toast } from 'react-toastify';
import '../css/alertas.css';

const Alertas = () => {
  const { id } = useParams(); // ID do usuário
  const [alertas, setAlertas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');

  const [categoriaFiltro, setCategoriaFiltro] = useState('Todos');

  const [modalCriarAberto, setModalCriarAberto] = useState(false);
  const [modalEditar, setModalEditar] = useState({ show: false, alerta: null });
  const [modalDetalhes, setModalDetalhes] = useState({ show: false, alerta: null });
  const [modalExclusao, setModalExclusao] = useState({ show: false, alerta: null });

  // Lista base de categorias para garantir que sempre existam
  const categoriasBase = ["Geral", "Aniversário", "Férias", "Trabalho", "Saúde"];

  // NOVO: Gera a lista de categorias disponíveis dinamicamente
  const categoriasDisponiveis = useMemo(() => {
    const lista = new Set(["Todos"]);

    // Adiciona categorias base
    categoriasBase.forEach(cat => lista.add(cat));

    // Adiciona categorias de alertas carregados
    alertas.forEach(alerta => {
      if (alerta.categoria) {
        lista.add(alerta.categoria);
      }
    });

    return Array.from(lista).sort((a, b) => {
        // Coloca "Todos" no topo
        if (a === "Todos") return -1;
        if (b === "Todos") return 1;
        return a.localeCompare(b);
    });

  }, [alertas]);

  useEffect(() => {
    carregarAlertas();
  }, [id]);

  const carregarAlertas = async () => {
    if (!id) {
        console.warn("ID do usuário ausente. Verifique a rota ou o estado de login.");
        return setLoading(false);
    }

    try {
      setLoading(true);
      console.log(`Tentando carregar alertas para o usuário ID: ${id}`);
      const data = await alertaService.listarPorUsuario(id);
      console.log("Alertas carregados:", data);

      if (Array.isArray(data)) {
        setAlertas(data);
      } else {
        console.error("A resposta da API não é um array:", data);
        setAlertas([]);
      }

    } catch (error) {
      console.error("Erro ao carregar alertas:", error);
      toast.error("Erro ao carregar alertas.");
    } finally {
      setLoading(false);
    }
  };

  const handleSalvarAlerta = async (dadosForm) => {
    try {
      await alertaService.criar(dadosForm);
      toast.success("Alerta criado com sucesso!");
      setModalCriarAberto(false);
      carregarAlertas();
    } catch (error) {
      toast.error("Erro ao criar alerta: " + (error.message || "Erro desconhecido"));
    }
  };

  const handleAtualizarAlerta = async (idAlerta, dadosForm) => {
    try {
      await alertaService.atualizar(idAlerta, dadosForm);
      toast.success("Alerta atualizado!");
      setModalEditar({ show: false, alerta: null });
      carregarAlertas();
    } catch (error) {
      toast.error("Erro ao atualizar alerta: " + (error.message || "Erro desconhecido"));
    }
  };

  const confirmarRemocao = async () => {
    if (!modalExclusao.alerta) return;
    try {
      await alertaService.remover(modalExclusao.alerta.id);
      setAlertas(alertas.filter(a => a.id !== modalExclusao.alerta.id));
      toast.info("Alerta removido.");
      setModalExclusao({ show: false, alerta: null });
    } catch (error) {
      toast.error("Erro ao remover alerta.");
    }
  };

  // CORREÇÃO DA LÓGICA DE BUSCA E FILTRAGEM
  const alertasFiltrados = alertas.filter(alerta => {

    const termoBuscaLower = busca.toLowerCase();

    // 1. **BUSCA CORRIGIDA:** Verifica se o termo está no Título OU na Descrição
    const tituloPassou = (alerta.titulo || '').toLowerCase().includes(termoBuscaLower);
    const descricaoPassou = (alerta.descricao || '').toLowerCase().includes(termoBuscaLower);

    // O alerta passa na busca se a palavra estiver no título OU na descrição
    const buscaPassou = tituloPassou || descricaoPassou;

    // 2. Filtragem por categoria
    const categoriaPassou = categoriaFiltro === 'Todos' ||
                           (alerta.categoria && alerta.categoria.toLowerCase() === categoriaFiltro.toLowerCase());

    return buscaPassou && categoriaPassou;
  });

  // Lista de categorias SEM a opção "Todos", para passar aos modais
  const categoriasParaModais = categoriasDisponiveis.filter(c => c !== 'Todos');

  return (
    <DashboardLayout>
      <div className="alertas-page" style={{ padding: '0 20px' }}>
        <div style={{ marginBottom: '30px' }}>
          <h1 style={{ fontWeight: 800 }}>Gerenciador de Alertas</h1>
          <p style={{ color: 'var(--text-secondary)' }}>Gerencie os alertas das suas metas</p>

          <div className="alertas-header-row" style={{ marginBottom: '25px' }}>
            {/* INPUT DE BUSCA */}
            <input
              className="search-input-alerta"
              placeholder="Buscar alerta..."
              value={busca}
              onChange={e => setBusca(e.target.value)}
            />

            {/* DROPDOWN DE FILTRO POR CATEGORIA (Usa a lista dinâmica) */}
            <select
                value={categoriaFiltro}
                onChange={e => setCategoriaFiltro(e.target.value)}
                className="select-filtro-categoria"
                style={{ width: '200px', marginRight: '10px' }}
            >
                {categoriasDisponiveis.map(cat => (
                    <option key={cat} value={cat}>{cat === 'Todos' ? 'Todas as Categorias' : cat}</option>
                ))}
            </select>

            {/* BOTÃO CRIAR ALERTA */}
            <button className="btn-primary-alerta" onClick={() => setModalCriarAberto(true)}>
              + Criar novo alerta
            </button>
          </div>

          <div className="alertas-list">
            {loading && <p>Carregando alertas...</p>}

            {!loading && alertas.length === 0 && busca.length === 0 && (
                <p>Nenhum alerta cadastrado para este usuário. Crie seu primeiro alerta!</p>
            )}

            {!loading && alertas.length > 0 && alertasFiltrados.length === 0 && (
                <p>Nenhum alerta encontrado com os filtros atuais.</p>
            )}

            {alertasFiltrados.map(alerta => (
              <AlertaCard
                key={alerta.id}
                alerta={alerta}
                onRemover={(a) => setModalExclusao({ show: true, alerta: a })}
                onEditar={(a) => setModalEditar({ show: true, alerta: a })}
                onVerDetalhes={(a) => setModalDetalhes({ show: true, alerta: a })}
              />
            ))}
          </div>
        </div>

        {/* MODAIS: PASSANDO A LISTA DINÂMICA DE CATEGORIAS */}
        {modalCriarAberto && (
          <CriarAlertaModal
            usuarioId={id}
            categoriasDisponiveis={categoriasParaModais}
            onClose={() => setModalCriarAberto(false)}
            onSalvar={handleSalvarAlerta}
          />
        )}

        {modalEditar.show && (
          <EditarAlertaModal
            alerta={modalEditar.alerta}
            categoriasDisponiveis={categoriasParaModais}
            onClose={() => setModalEditar({ show: false, alerta: null })}
            onSalvar={handleAtualizarAlerta}
          />
        )}

        {modalDetalhes.show && (
          <DetalhesAlertaModal
            alerta={modalDetalhes.alerta}
            onClose={() => setModalDetalhes({ show: false, alerta: null })}
          />
        )}

        <ConfirmacaoModal
          isOpen={modalExclusao.show}
          onClose={() => setModalExclusao({ show: false, alerta: null })}
          onConfirm={confirmarRemocao}
          titulo="Excluir Alerta?"
          mensagem={`Deseja realmente excluir o alerta "${modalExclusao.alerta?.descricao || 'selecionado'}"?`}
        />
      </div>
    </DashboardLayout>
  );
};

export default Alertas;
// src/pages/Alertas.js
import React, { useEffect, useState, useMemo } from 'react';
import { useParams } from 'react-router-dom';
import DashboardLayout from '../components/DashboardLayout';
import AlertaCard from '../components/Alertas/AlertaCard';
import CriarAlertaModal from '../components/Alertas/CriarAlertaModal';
import EditarAlertaModal from '../components/Alertas/EditarAlertaModal';
import DetalhesAlertaModal from '../components/Alertas/DetalhesAlertaModal';
import GerenciarCategoriasModal from '../components/Alertas/GerenciarCategoriasModal'; 
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import { alertaService } from '../services/alertaService';
import { toast } from 'react-toastify';
import '../css/alertas.css';

// Lista base de categorias FIXAS
const categoriasBaseFixas = ["Geral", "Aniversário", "Férias", "Trabalho", "Saúde"];

const Alertas = () => {
      const { id } = useParams(); // ID do usuário
      const [alertas, setAlertas] = useState([]);
      const [loading, setLoading] = useState(true);
      const [busca, setBusca] = useState('');

      const [categoriaFiltro, setCategoriaFiltro] = useState('Todos');
      
      // NOVO ESTADO: Categorias que o usuário pode adicionar/remover
      const [categoriasPersonalizadas, setCategoriasPersonalizadas] = useState([]);

      const [modalCriarAberto, setModalCriarAberto] = useState(false);
      const [modalEditar, setModalEditar] = useState({ show: false, alerta: null });
      const [modalDetalhes, setModalDetalhes] = useState({ show: false, alerta: null });
      const [modalExclusao, setModalExclusao] = useState({ show: false, alerta: null });
      const [modalGerenciarCategorias, setModalGerenciarCategorias] = useState(false); 

      // Lista COMPLETA de categorias disponíveis para seleção/gestão
      const categoriasDisponiveis = useMemo(() => {
            const lista = new Set(["Todos"]);

            // Combina categorias fixas e personalizadas
            const todasCategorias = [...categoriasBaseFixas, ...categoriasPersonalizadas];
            todasCategorias.forEach(cat => lista.add(cat));

            // Adiciona categorias de alertas carregados (para garantir que categorias antigas sejam exibidas)
            alertas.forEach(alerta => {
                  if (alerta.categoria) {
                        lista.add(alerta.categoria);
                  }
            });

            return Array.from(lista).sort((a, b) => {
                        if (a === "Todos") return -1;
                        if (b === "Todos") return 1;
                        return a.localeCompare(b);
            });

      }, [alertas, categoriasPersonalizadas]);


      useEffect(() => {
            carregarAlertas();
            // Em uma aplicação real, você também carregaria o estado de `categoriasPersonalizadas` aqui
      }, [id]);

      const carregarAlertas = async () => {
            if (!id) {
                        console.warn("ID do usuário ausente. Verifique a rota ou o estado de login.");
                        return setLoading(false);
            }

            try {
                  setLoading(true);
                  const data = await alertaService.listarPorUsuario(id);

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

      // LÓGICA DE GESTÃO DE CATEGORIAS
      const handleAdicionarCategoria = (novaCat) => {
            const todas = [...categoriasBaseFixas, ...categoriasPersonalizadas];
            if (todas.map(c => c.toLowerCase()).includes(novaCat.toLowerCase())) {
                  return toast.error("Esta categoria já existe.");
            }
    
            setCategoriasPersonalizadas(prev => {
                  toast.success(`Categoria '${novaCat}' adicionada.`);
                  return [...prev, novaCat];
            });
            // Em produção: Chamar API para salvar
      };

      const handleRemoverCategoria = (catParaRemover) => {
            if (categoriasBaseFixas.includes(catParaRemover)) {
                  return toast.error("Esta categoria não pode ser removida pois é padrão.");
            }

            // Verifica se há alertas usando a categoria
            const alertasComCategoria = alertas.filter(a => a.categoria === catParaRemover);
            if (alertasComCategoria.length > 0) {
                  return toast.error(`Não é possível remover a categoria, pois ela está sendo usada em ${alertasComCategoria.length} alerta(s).`);
            }

            setCategoriasPersonalizadas(prev => prev.filter(cat => cat !== catParaRemover));
            toast.info(`Categoria '${catParaRemover}' removida.`);

            // Reseta o filtro se a categoria removida estiver selecionada
            if (categoriaFiltro === catParaRemover) {
                  setCategoriaFiltro('Todos');
            }
            // Em produção: Chamar API para remover
      };

      // LÓGICA DE CRIAÇÃO/EDIÇÃO/EXCLUSÃO DE ALERTAS (mantida)

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

      // LÓGICA DE BUSCA E FILTRAGEM (mantida)
      const alertasFiltrados = alertas.filter(alerta => {

            const termoBuscaLower = busca.toLowerCase();

            const tituloPassou = (alerta.titulo || '').toLowerCase().includes(termoBuscaLower);
            const descricaoPassou = (alerta.descricao || '').toLowerCase().includes(termoBuscaLower);

            const buscaPassou = tituloPassou || descricaoPassou;

            const categoriaPassou = categoriaFiltro === 'Todos' ||
                                                                                   (alerta.categoria && alerta.categoria.toLowerCase() === categoriaFiltro.toLowerCase());

            return buscaPassou && categoriaPassou;
      });

      // Lista de categorias SEM a opção "Todos", para passar aos modais
      const categoriasParaModais = categoriasDisponiveis.filter(c => c !== 'Todos');

      // Categorias que PODEM ser removidas/adicionadas no modal de gestão (fixas + personalizadas + as que estão em alertas)
      const categoriasParaGestao = categoriasDisponiveis.filter(c => c !== 'Todos');
    
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

                                    {/* DROPDOWN DE FILTRO POR CATEGORIA */}
                                    <select
                                                value={categoriaFiltro}
                                                onChange={e => setCategoriaFiltro(e.target.value)}
                                                className="select-filtro-categoria"
                                    >
                                                {categoriasDisponiveis.map(cat => (
                                                            <option key={cat} value={cat}>{cat === 'Todos' ? 'Todas as Categorias' : cat}</option>
                                                ))}
                                    </select>
            
            {/* GRUPO DE AÇÕES (CORREÇÃO DE LAYOUT) */}
            <div className="alertas-actions-group">
                {/* BOTÃO GERENCIAR CATEGORIAS */}
                <button 
                    className="btn-outline" 
                    onClick={() => setModalGerenciarCategorias(true)}
                >
                    Gerenciar Categorias
                </button>

                {/* BOTÃO CRIAR ALERTA */}
                <button className="btn-primary-alerta" onClick={() => setModalCriarAberto(true)}>
                    + Criar novo alerta
                </button>
            </div>
            {/* FIM GRUPO DE AÇÕES */}
            
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

                        {/* MODAL GERENCIAR CATEGORIAS */}
                        {modalGerenciarCategorias && (
                              <GerenciarCategoriasModal
                                    categorias={categoriasParaGestao}
                                    onClose={() => setModalGerenciarCategorias(false)}
                                    onAdicionar={handleAdicionarCategoria}
                                    onRemover={handleRemoverCategoria}
                              />
                        )}
        
                        {/* MODAIS CRIAÇÃO */}
                        {modalCriarAberto && (
                              <CriarAlertaModal
                                    usuarioId={id}
                                    categoriasDisponiveis={categoriasParaModais}
                                    onClose={() => setModalCriarAberto(false)}
                                    onSalvar={handleSalvarAlerta}
                              />
                        )}

                        {/* MODAIS EDIÇÃO */}
                        {modalEditar.show && (
                              <EditarAlertaModal
                                    alerta={modalEditar.alerta}
                                    categoriasDisponiveis={categoriasParaModais}
                                    onClose={() => setModalEditar({ show: false, alerta: null })}
                                    onSalvar={handleAtualizarAlerta}
                              />
                        )}

                        {/* MODAIS DETALHES */}
                        {modalDetalhes.show && (
                              <DetalhesAlertaModal
                                    alerta={modalDetalhes.alerta}
                                    onClose={() => setModalDetalhes({ show: false, alerta: null })}
                              />
                        )}

                        {/* MODAL CONFIRMAÇÃO DE EXCLUSÃO */}
                        <ConfirmacaoModal
                              isOpen={modalExclusao.show}
                              onClose={() => setModalExclusao({ show: false, alerta: null })}
                              onConfirm={confirmarRemocao}
                              titulo="Excluir Alerta?"
                              mensagem={`Deseja realmente excluir o alerta "${modalExclusao.alerta?.titulo || 'selecionado'}"?`}
                        />
                  </div>
            </DashboardLayout>
      );
};

export default Alertas;
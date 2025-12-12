import React, { useEffect, useState } from 'react';
import DashboardLayout from '../components/DashboardLayout';
import AmigoCard from '../components/Social/AmigoCard';
import PerfilAmigoModal from '../components/Social/PerfilAmigoModal';
import ConfirmacaoModal from '../components/Social/ConfirmacaoModal';
import { toast } from 'react-toastify';

import { socialService } from '../services/socialService';
import '../css/social.css';

const Amigos = () => {
  const [amigos, setAmigos] = useState([]);
  const [loading, setLoading] = useState(true);

  const [termoBusca, setTermoBusca] = useState('');
  const [resultadosBusca, setResultadosBusca] = useState([]);


  const [amigoSelecionado, setAmigoSelecionado] = useState(null);
  const [modalExclusao, setModalExclusao] = useState({
    show: false,
    idParaRemover: null,
    nomeAmigo: ''
  });

  const usuarioId = "8c155cb1-f588-4fb2-b3f3-aaf341d2dcc4";

  useEffect(() => {
    carregarAmigos();
  }, []);

  const carregarAmigos = async () => {
    try {
      setLoading(true);
      const data = await socialService.listarAmigos(usuarioId);
      setAmigos(data);
    } catch (error) {
      toast.error("Erro ao carregar lista de amigos.");
    } finally {
      setLoading(false);
    }
  };

  const handleBusca = async (e) => {
    const termo = e.target.value;
    setTermoBusca(termo);
    if (termo.length > 2) {
      const resultados = await socialService.pesquisarUsuarios(termo);
      const filtrados = resultados.filter(u =>
        u.id !== usuarioId && !amigos.some(a => a.id === u.id)
      );
      setResultadosBusca(filtrados);
    } else {
      setResultadosBusca([]);
    }
  };

  const handleAdicionar = async (amigoId) => {
    try {
      await socialService.adicionarAmigo(usuarioId, amigoId);

      toast.success("Amigo adicionado com sucesso! 🎉");

      setTermoBusca("");
      setResultadosBusca([]);
      carregarAmigos();
    } catch (e) {
      toast.error("Erro ao adicionar: " + e.message);
    }
  };

  const solicitarRemocao = (amigo) => {
    setModalExclusao({
      show: true,
      idParaRemover: amigo.id,
      nomeAmigo: amigo.nome
    });
  };


  const confirmarRemocao = async () => {
    const id = modalExclusao.idParaRemover;
    try {
      await socialService.removerAmigo(usuarioId, id);

      setAmigos(amigos.filter(a => a.id !== id));
      toast.info("Amizade desfeita.");

      setModalExclusao({ show: false, idParaRemover: null, nomeAmigo: '' });
    } catch (e) {
      toast.error("Erro ao remover amigo.");
    }
  };

  return (
    <DashboardLayout>
      <div className="social-page">
        <h1>Meus Amigos</h1>
        <p>Busque pessoas pelo nome e adicione à sua rede.</p>

        <div className="search-container" style={{ position: 'relative', marginBottom: '30px' }}>
          <div className="social-header" style={{ marginBottom: '5px' }}>
            <input
              className="search-input"
              placeholder="Digite o nome de um amigo..."
              value={termoBusca}
              onChange={handleBusca}
            />
          </div>

          {resultadosBusca.length > 0 && (
            <div className="search-results-dropdown">
              {resultadosBusca.map(usuario => (
                <div key={usuario.id} className="search-result-item">
                  <div className="result-avatar">
                    {usuario.nome.charAt(0).toUpperCase()}
                  </div>
                  <div className="result-info">
                    <span className="result-name">{usuario.nome}</span>
                    <span className="result-email">{usuario.email}</span>
                  </div>
                  <button
                    className="btn-add-small"
                    onClick={() => handleAdicionar(usuario.id)}
                  >
                    Adicionar
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="amigos-grid">
          {loading && <p>Carregando...</p>}
          {!loading && amigos.length === 0 && <p>Você ainda não tem amigos.</p>}

          {amigos.map(amigo => (
            <AmigoCard
              key={amigo.id}
              amigo={amigo}
              // MUDANÇA: Passamos o objeto inteiro para pegar o nome
              onRemover={() => solicitarRemocao(amigo)}
              onVerPerfil={setAmigoSelecionado}
            />
          ))}
        </div>

        {amigoSelecionado && (
          <PerfilAmigoModal
            amigo={amigoSelecionado}
            onClose={() => setAmigoSelecionado(null)}
          />
        )}

        <ConfirmacaoModal
          isOpen={modalExclusao.show}
          onClose={() => setModalExclusao({ ...modalExclusao, show: false })}
          onConfirm={confirmarRemocao}
          titulo="Desfazer amizade?"
          mensagem={`Tem certeza que deseja remover ${modalExclusao.nomeAmigo} da sua lista de amigos?`}
        />

      </div>
    </DashboardLayout>
  );
};

export default Amigos;
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../api/axios.js";
import { useAuth } from "../context/AuthContext.jsx";

const opcoesStatus = ["ABERTO", "EM_ANDAMENTO", "RESOLVIDO", "FECHADO"];

export default function DetalheChamado() {
  const { id } = useParams();
  const [chamado, setChamado] = useState(null);
  const [novoStatus, setNovoStatus] = useState("");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(true);
  const { usuario } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    carregarChamado();
  }, [id]);

  async function carregarChamado() {
    setCarregando(true);
    const resposta = await api.get(`/api/chamados/${id}`);
    setChamado(resposta.data);
    setNovoStatus(resposta.data.status);
    setCarregando(false);
  }

  async function handleAlterarStatus(evento) {
    evento.preventDefault();
    setErro("");

    try {
      const resposta = await api.put(`/api/chamados/${id}/status`, { status: novoStatus });
      setChamado(resposta.data);
    } catch (erroRequisicao) {
      const mensagem = erroRequisicao.response?.data?.message || "Nao foi possivel alterar o status";
      setErro(mensagem);
    }
  }

  if (carregando || !chamado) {
    return (
      <div className="pagina">
        <p>Carregando chamado...</p>
      </div>
    );
  }

  return (
    <div className="pagina">
      <button className="botao-voltar" onClick={() => navigate("/")}>Voltar</button>

      <div className="cartao-detalhe">
        <h1>{chamado.titulo}</h1>
        <p className="descricao-chamado">{chamado.descricao}</p>

        <div className="grade-detalhe">
          <div>
            <span className="rotulo">Categoria</span>
            <p>{chamado.categoria}</p>
          </div>
          <div>
            <span className="rotulo">Prioridade</span>
            <p>{chamado.prioridade}</p>
          </div>
          <div>
            <span className="rotulo">Status</span>
            <p>{chamado.status}</p>
          </div>
          <div>
            <span className="rotulo">Solicitante</span>
            <p>{chamado.nomeSolicitante}</p>
          </div>
          <div>
            <span className="rotulo">Email do solicitante</span>
            <p>{chamado.emailSolicitante}</p>
          </div>
          <div>
            <span className="rotulo">Data de abertura</span>
            <p>{new Date(chamado.dataAbertura).toLocaleString("pt-BR")}</p>
          </div>
        </div>

        {usuario.role === "ROLE_ADMIN" && (
          <form className="formulario-status" onSubmit={handleAlterarStatus}>
            {erro && <div className="mensagem-erro">{erro}</div>}

            <label>Alterar status</label>
            <select value={novoStatus} onChange={(evento) => setNovoStatus(evento.target.value)}>
              {opcoesStatus.map((status) => (
                <option key={status} value={status}>
                  {status}
                </option>
              ))}
            </select>

            <button type="submit">Salvar status</button>
          </form>
        )}
      </div>
    </div>
  );
}
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axios.js";

export default function NovoChamado() {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [categoria, setCategoria] = useState("Hardware");
  const [prioridade, setPrioridade] = useState("MEDIA");
  const [erro, setErro] = useState("");
  const navigate = useNavigate();
  async function handleSubmit(evento) {
    evento.preventDefault();
    setErro("");
    try {
      await api.post("/api/chamados", { titulo, descricao, categoria, prioridade });
      navigate("/");
    } catch (erroRequisicao) {
      const mensagem = erroRequisicao.response?.data?.message || "Nao foi possivel abrir o chamado";
      setErro(mensagem);
    }
  }
  return (
    <div className="pagina">
      <h1>Abrir novo chamado</h1>
      <form className="formulario-largo" onSubmit={handleSubmit}>
        {erro && <div className="mensagem-erro">{erro}</div>}

        <label>Titulo</label>
        <input
          type="text"
          placeholder="Ex: Troca de monitor"
          value={titulo}
          onChange={(evento) => setTitulo(evento.target.value)}
          required
        />
        <label>Descricao</label>
        <textarea
          rows={5}
          placeholder="Descreva o problema com o maximo de detalhes possivel"
          value={descricao}
          onChange={(evento) => setDescricao(evento.target.value)}
          required
        />
        <label>Categoria</label>
        <select value={categoria} onChange={(evento) => setCategoria(evento.target.value)}>
          <option value="Hardware">Hardware</option>
          <option value="Software">Software</option>
          <option value="Rede">Rede</option>
          <option value="Acesso e permissoes">Acesso e permissoes</option>
        </select>
        <label>Prioridade</label>
        <select value={prioridade} onChange={(evento) => setPrioridade(evento.target.value)}>
          <option value="BAIXA">Baixa</option>
          <option value="MEDIA">Media</option>
          <option value="ALTA">Alta</option>
        </select>

        <div className="acoes-formulario">
          <button type="submit">Abrir chamado</button>
          <button type="button" className="botao-secundario" onClick={() => navigate("/")}>
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
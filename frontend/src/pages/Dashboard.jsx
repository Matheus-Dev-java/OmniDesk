import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios.js";
import { useAuth } from "../context/AuthContext.jsx";

const rotuloStatus = {
  ABERTO: "Aberto",
  EM_ANDAMENTO: "Em andamento",
  RESOLVIDO: "Resolvido",
  FECHADO: "Fechado"
};

const rotuloPrioridade = {
  BAIXA: "Baixa",
  MEDIA: "Media",
  ALTA: "Alta"
};

export default function Dashboard() {
  const [chamados, setChamados] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const { usuario, logout } = useAuth();

  useEffect(() => {
    carregarChamados();
  }, []);

  async function carregarChamados() {
    setCarregando(true);
    const resposta = await api.get("/api/chamados");
    setChamados(resposta.data);
    setCarregando(false);
  }

  return (
    <div className="pagina">
      <header className="cabecalho">
        <div>
          <h1>Portal de Chamados Tecnicos</h1>
          <p>Ola, {usuario.nome}</p>
        </div>
        <div className="acoes-cabecalho">
          <Link to="/chamados/novo" className="botao">Abrir chamado</Link>
          <button className="botao-secundario" onClick={logout}>Sair</button>
        </div>
      </header>

      {carregando ? (
        <p>Carregando chamados...</p>
      ) : (
        <table className="tabela">
          <thead>
            <tr>
              <th>ID</th>
              <th>Titulo</th>
              <th>Categoria</th>
              <th>Prioridade</th>
              <th>Status</th>
              <th>Solicitante</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {chamados.map((chamado) => (
              <tr key={chamado.id}>
                <td>{chamado.id}</td>
                <td>{chamado.titulo}</td>
                <td>{chamado.categoria}</td>
                <td>{rotuloPrioridade[chamado.prioridade]}</td>
                <td>
                  <span className={`status status-${chamado.status.toLowerCase()}`}>
                    {rotuloStatus[chamado.status]}
                  </span>
                </td>
                <td>{chamado.nomeSolicitante}</td>
                <td>
                  <Link to={`/chamados/${chamado.id}`}>Detalhes</Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axios.js";

export default function Registro() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(evento) {
    evento.preventDefault();
    setErro("");

    try {
      await api.post("/api/auth/registrar", { nome, email, senha });
      setSucesso(true);
      setTimeout(() => navigate("/login"), 1500);
    } catch (erroRequisicao) {
      const mensagem = erroRequisicao.response?.data?.message || "Nao foi possivel concluir o cadastro";
      setErro(mensagem);
    }
  }
  return (
    <div className="pagina-auth">
      <form className="formulario" onSubmit={handleSubmit}>
        <h1>Criar conta</h1>
        <p className="subtitulo">Cadastro de funcionario</p>

        {erro && <div className="mensagem-erro">{erro}</div>}
        {sucesso && <div className="mensagem-sucesso">Cadastro realizado com sucesso</div>}

        <label>Nome completo</label>
        <input
          type="text"
          value={nome}
          onChange={(evento) => setNome(evento.target.value)}
          required
        />
        <label>Email</label>
        <input
          type="email"
          value={email}
          onChange={(evento) => setEmail(evento.target.value)}
          required
        />
        <label>Senha</label>
        <input
          type="password"
          value={senha}
          onChange={(evento) => setSenha(evento.target.value)}
          minLength={6}
          required
        />
        <button type="submit">Cadastrar</button>

        <p className="texto-rodape">
          Ja tem uma conta? <Link to="/login">Entrar</Link>
        </p>
      </form>
    </div>
  );
}
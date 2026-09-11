import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(evento) {
    evento.preventDefault();
    setErro("");

    try {
      await login(email, senha);
      navigate("/");
    } catch (erroRequisicao) {
      const mensagem = erroRequisicao.response?.data?.message || "Nao foi possivel realizar o login";
      setErro(mensagem);
    }
  }
  return (
    <div className="pagina-auth">
      <form className="formulario" onSubmit={handleSubmit}>
        <h1>Portal de Chamados Tecnicos</h1>
        <p className="subtitulo">Acesse sua conta</p>

        {erro && <div className="mensagem-erro">{erro}</div>}

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
          required
        />
        <button type="submit">Entrar</button>
        <p className="texto-rodape">
          Nao tem uma conta? <Link to="/registro">Cadastre-se</Link>
        </p>
      </form>
    </div>
  );
}
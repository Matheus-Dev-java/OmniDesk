import React, { createContext, useContext, useEffect, useState } from "react";
import api from "../api/axios.js";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    const dados = localStorage.getItem("helpdesk_usuario");
    if (dados) {
      setUsuario(JSON.parse(dados));
    }
    setCarregando(false);
  }, []);
  async function login(email, senha) {
    const resposta = await api.post("/api/auth/login", { email, senha });
    const dados = resposta.data;
    const usuarioLogado = {
      nome: dados.nome,
      email: dados.email,
      role: dados.role
    };
    localStorage.setItem("helpdesk_token", dados.token);
    localStorage.setItem("helpdesk_usuario", JSON.stringify(usuarioLogado));
    setUsuario(usuarioLogado);
  }
  function logout() {
    localStorage.removeItem("helpdesk_token");
    localStorage.removeItem("helpdesk_usuario");
    setUsuario(null);
  }
  return (
    <AuthContext.Provider value={{ usuario, login, logout, carregando }}>
      {children}
    </AuthContext.Provider>
  );
}
export function useAuth() {
  return useContext(AuthContext);
}
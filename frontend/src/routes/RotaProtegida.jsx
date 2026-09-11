import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function RotaProtegida({ children, somenteAdmin = false }) {
  const { usuario, carregando } = useAuth();

  if (carregando) {
    return null;
  }
  if (!usuario) {
    return <Navigate to="/login" replace />;
  }
  if (somenteAdmin && usuario.role !== "ROLE_ADMIN") {
    return <Navigate to="/" replace />;
  }
  return children;
}
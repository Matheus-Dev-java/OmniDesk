import React from "react";
import { Route, Routes } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Registro from "./pages/Registro.jsx";
import Dashboard from "./pages/Dashboard.jsx";
import NovoChamado from "./pages/NovoChamado.jsx";
import DetalheChamado from "./pages/DetalheChamado.jsx";
import RotaProtegida from "./routes/RotaProtegida.jsx";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/registro" element={<Registro />} />
      <Route
        path="/"
        element={
          <RotaProtegida>
            <Dashboard />
          </RotaProtegida>
        }
      />
      <Route
        path="/chamados/novo"
        element={
          <RotaProtegida>
            <NovoChamado />
          </RotaProtegida>
        }
      />
      <Route
        path="/chamados/:id"
        element={
          <RotaProtegida>
            <DetalheChamado />
          </RotaProtegida>
        }
      />
    </Routes>
  );
}
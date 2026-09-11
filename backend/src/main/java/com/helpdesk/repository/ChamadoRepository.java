package com.helpdesk.repository;

import com.helpdesk.model.Chamado;
import com.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findBySolicitante(Usuario solicitante);
}
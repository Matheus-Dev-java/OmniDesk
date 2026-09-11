package com.helpdesk.dto;

import com.helpdesk.model.Chamado;
import com.helpdesk.model.PrioridadeChamado;
import com.helpdesk.model.StatusChamado;

import java.time.LocalDateTime;

public class ChamadoResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private PrioridadeChamado prioridade;
    private StatusChamado status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private String nomeSolicitante;
    private String emailSolicitante;

    public ChamadoResponse(Chamado chamado) {
        this.id = chamado.getId();
        this.titulo = chamado.getTitulo();
        this.descricao = chamado.getDescricao();
        this.categoria = chamado.getCategoria();
        this.prioridade = chamado.getPrioridade();
        this.status = chamado.getStatus();
        this.dataAbertura = chamado.getDataAbertura();
        this.dataFechamento = chamado.getDataFechamento();
        this.nomeSolicitante = chamado.getSolicitante().getNome();
        this.emailSolicitante = chamado.getSolicitante().getEmail();
    }
    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getCategoria() {
        return categoria;
    }
    public PrioridadeChamado getPrioridade() {
        return prioridade;
    }
    public StatusChamado getStatus() {
        return status;
    }
    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }
    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }
    public String getNomeSolicitante() {
        return nomeSolicitante;
    }
    public String getEmailSolicitante() {
        return emailSolicitante;
    }
}
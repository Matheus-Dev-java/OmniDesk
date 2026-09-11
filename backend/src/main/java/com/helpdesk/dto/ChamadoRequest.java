package com.helpdesk.dto;

import com.helpdesk.model.PrioridadeChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotBlank
    private String categoria;
    @NotNull
    private PrioridadeChamado prioridade;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public PrioridadeChamado getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(PrioridadeChamado prioridade) {
        this.prioridade = prioridade;
    }
}
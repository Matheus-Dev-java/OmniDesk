package com.helpdesk.dto;

import com.helpdesk.model.StatusChamado;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull
    private StatusChamado status;

    public StatusChamado getStatus() {
        return status;
    }
    public void setStatus(StatusChamado status) {
        this.status = status;
    }
}
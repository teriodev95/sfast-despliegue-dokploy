package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class StatusSolicitudDTO {
    private String createdByUser;
    private String creationDate;
    private String status;
    private String note;
}

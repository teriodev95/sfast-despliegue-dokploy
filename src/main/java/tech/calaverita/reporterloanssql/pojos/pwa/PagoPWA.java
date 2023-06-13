package tech.calaverita.reporterloanssql.pojos.pwa;

import lombok.Data;

@Data
public class PagoPWA {
    private String pagoId;
    private Double monto;
    private Boolean esPrimerPago;
    private Double abreCon;
    private Double cierraCon;
    private String tipo;
    private String creadoDesde;
    private String fechaPago;
    private Double lat;
    private Double lng;
    private String comentario;
    private String datosMigracion;
    private String createdAt;
    private String updatedAt;
    private String log;
    private String quienPago;
}

package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reportes_call_center_v2")
public class ReporteCallCenterLiteModel {
    @Id
    private Integer id;
    @Column(name = "prestamoid")
    private String prestamoId;
    @Column(name = "nombrecliente")
    private String nombreCliente;
    @Column(name = "nombreaval")
    private String nombreAval;
    private String agencia;
    @Column(name = "gerenciasfast")
    private String gerencia;
    @Column(name = "numllamadascliente")
    private Integer numLlamadasCliente;
    @Column(name = "observacionescliente")
    private String observacionesCliente;
    @Column(name = "nombreatiendecliente")
    private String nombreAtiendeCliente;
    @Column(name = "preguntascliente")
    private String preguntasCliente;
    @Column(name = "numllamadasaval")
    private Integer numLlamadasAval;
    @Column(name = "observacionesaval")
    private String observacionesAval;
    @Column(name = "nombreatiendeaval")
    private String nombreAtiendeAval;
    @Column(name = "preguntasaval")
    private String preguntas_aval;
    @Column(name = "statusllamadacliente")
    private String statusLlamadaCliente;
    @Column(name = "statusllamadaaval")
    private String statusLlamadaAval;
    @Column(name = "urlllamadacliente")
    private String urlLlamadaCliente;
    @Column(name = "urlllamadaaval")
    private String urlLlamadaAval;
    @Column(name = "reportarseguridad")
    private Boolean reportarSeguridad;
    private Integer anio;
    private Integer Semana;
    @Column(name = "tienevisitas")
    private Boolean tieneVisitas;
}

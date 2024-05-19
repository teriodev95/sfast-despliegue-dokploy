package tech.calaverita.reporterloanssql.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reportes_call_center")
public class ReporteCallCenterModel {
    @Id
    private String prestamo;
    @Column(name = "nombrecliente")
    private String nombreCliente;
    @Column(name = "telefonocliente")
    private String telefonoCliente;
    @Column(name = "montootorgado")
    private Integer montoOtorgado;
    @Column(name = "nombreaval")
    private String nombreAval;
    @Column(name = "telefonoaval")
    private String telefonoAval;
    @Column(name = "tipodecredito")
    private String tipoDeCredito;
    private Integer plazo;
    private Double tarifa;
    private String gerencia;
    private String agencia;
    @Column(name = "sucursalid")
    private String sucursalId;
    private String seguridad;
    @Column(name = "gerenteenturno")
    private String gerenteEnTurno;
    @Column(name = "registrocallcenterid")
    private Integer registroCallCenterId;
    @Column(name = "prestamoid")
    private String prestamoId;
    @Column(name = "numllamadascliente")
    private Integer numLlamadasCliente;
    @Column(name = "fechallamadacliente")
    private String fechaLlamadaCliente;
    @Column(name = "observacionescliente")
    private String observacionesCliente;
    @Column(name = "nombreatiendecliente")
    private String nombreAtiendeCliente;
    @Column(name = "relacionatiendecliente")
    private String relacionAtiendeCliente;
    @Column(name = "preguntascliente")
    private String preguntasCliente;
    @Column(name = "numllamadasaval")
    private Integer numLlamadasAval;
    @Column(name = "fechallamadaaval")
    private String fechaLlamadaAval;
    @Column(name = "observacionesaval")
    private String observacionesAval;
    @Column(name = "nombreatiendeaval")
    private String nombreAtiendeAval;
    @Column(name = "relacionatiendeaval")
    private String relacionAtiendeAval;
    @Column(name = "preguntasaval")
    private String preguntas_aval;
    @Column(name = "statusregistrocallcenter")
    private String statusRegistroCallCenter;
    @Column(name = "statusllamadacliente")
    private String statusLlamadaCliente;
    @Column(name = "statusllamadaaval")
    private String statusLlamadaAval;
    private String usuario;
    @Column(name = "reportarseguridad")
    private Boolean reportarSeguridad;
    @Column(name = "fechacreacion")
    private String fechaCreacion;
    @Column(name = "fechamodificacion")
    private String fechaModificacion;
    @Column(name = "urlllamadacliente")
    private String urlLlamadaCliente;
    @Column(name = "urlllamadaaval")
    private String urlLlamadaAval;
    @Column(name = "nombreagente")
    private String nombreAgente;
}

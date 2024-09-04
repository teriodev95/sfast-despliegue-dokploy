package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;

@Data
@Entity
@Table(name = "pagos_v3")
public class PagoModel {
    @Id
    @Column(name = "pagoid")
    private String pagoId;
    @Column(name = "prestamoid")
    private String prestamoId;
    private String prestamo;
    private Double monto;
    private Integer semana;
    private Integer anio;
    @Column(name = "esprimerpago")
    private Boolean esPrimerPago;
    @Column(name = "abrecon")
    private Double abreCon;
    @Column(name = "cierracon")
    private Double cierraCon;
    private Double tarifa;
    private String cliente;
    private String agente;
    private String tipo;
    private String creadoDesde;
    private String identificador;
    private String fechaPago;
    private Double lat;
    private Double lng;
    private String comentario;
    private String datosMigracion;
    private String createdAt;
    private String updatedAt;
    private String log;
    private String quienPago;

    public PagoModel() {

    }

    public PagoModel(PagoDynamicModel pagoDynamicModel) {
        this.prestamoId = pagoDynamicModel.getPrestamoId();
        this.prestamo = pagoDynamicModel.getPrestamo();
        this.cliente = pagoDynamicModel.getCliente();
        this.monto = pagoDynamicModel.getMonto();
        this.semana = pagoDynamicModel.getSemana();
        this.anio = pagoDynamicModel.getAnio();
        this.esPrimerPago = pagoDynamicModel.getEsPrimerPago();
        this.abreCon = pagoDynamicModel.getAbreCon();
        this.cierraCon = pagoDynamicModel.getCierraCon();
        this.tarifa = pagoDynamicModel.getTarifa();
        this.agente = pagoDynamicModel.getAgencia();
        this.tipo = pagoDynamicModel.getTipo();
        this.fechaPago = pagoDynamicModel.getFechaPago();
        this.identificador = pagoDynamicModel.getIdentificador();
    }
}

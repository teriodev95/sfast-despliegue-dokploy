package tech.calaverita.sfast_xpress.models.mariaDB.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.EstadoAgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
@Entity
@Table(name = "pagos_dynamic")
public class PagoDynamicModel {
    @Id
    private String pagoId;
    private String prestamoId;
    private String prestamo;
    private String cliente;
    private Double monto;
    private Integer semana;
    private Integer anio;
    private Boolean esPrimerPago;
    private Double abreCon;
    private Double cierraCon;
    private Double tarifa;
    private String agencia;
    private String tipo;
    private String fechaPago;
    private String identificador;
    private String quienPago;
    private String comentario;
    private Double lat;
    private Double lng;
    private String recuperadoPor;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamoId", insertable = false, updatable = false)
    private PrestamoViewModel prestamoViewModel;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia", insertable = false, updatable = false)
    private EstadoAgenciaModel estadoAgenciaModel;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia", insertable = false, updatable = false)
    private AgenciaModel agenciaModel;
}

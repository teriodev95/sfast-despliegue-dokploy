package tech.calaverita.sfast_xpress.models.mariaDB.views;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;

@Data
@Entity
@Table(name = "prestamos_view")
public class PrestamoViewModel {
    @Id
    private String prestamoId;
    private String clienteId;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    private String noExterior;
    private String noInterior;
    private String colonia;
    private String codigoPostal;
    private String municipio;
    private String estado;
    private String noDeContrato;
    private String agencia;
    private String gerencia;
    private String sucursal;
    private Integer semana;
    private Integer anio;
    private Integer plazo;
    private Double montoOtorgado;
    private Double cargo;
    @Column(name = "total_a_pagar")
    private Double totalAPagar;
    private Double primerPago;
    private Double tarifa;
    private String saldosMigrados;
    private String wkDescu;
    private Double descuento;
    private Double porcentaje;
    private Double multas;
    private String wkRefi;
    private String refin;
    private String externo;
    private Double saldo;
    private Double cobrado;
    private String tipoDeCredito;
    private String aclaracion;
    private String nombresAval;
    private String apellidoPaternoAval;
    private String apellidoMaternoAval;
    private String direccionAval;
    private String noExteriorAval;
    private String noInteriorAval;
    private String coloniaAval;
    private String codigoPostalAval;
    private String poblacionAval;
    private String estadoAval;
    private String telefonoAval;
    private String telefonoCliente;
    private String diaDePago;
    private String gerenteEnTurno;
    private String agente;
    private String status;
    private String capturista;
    private String noServicio;
    private String tipoDeCliente;
    private String identificadorCredito;
    private String seguridad;
    private String depuracion;
    private String folioDePagare;
    private Double saldoAlIniciarSemana;
    private Integer excelIndex;
    private String clientePersonaId;
    private String avalPersonaId;
    @Transient
    private Double porcentajeCobrado;
    @JsonIgnore
    @OneToMany(mappedBy = "prestamoViewModel", fetch = FetchType.LAZY)
    List<PagoDynamicModel> pagoDynamicModels;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agencia", insertable = false, updatable = false)
    private AgenciaModel agenciaModel;
}

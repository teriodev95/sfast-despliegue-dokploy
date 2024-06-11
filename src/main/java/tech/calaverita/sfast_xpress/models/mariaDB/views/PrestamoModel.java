package tech.calaverita.sfast_xpress.models.mariaDB.views;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prestamos_saldo_y_cobrado_calculados")
public class PrestamoModel {
    @Id
    @Column(name = "prestamoid")
    private String prestamoId;
    private String clienteId;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String direccion;
    @Column(name = "noexterior")
    private String noExterior;
    @Column(name = "nointerior")
    private String noInterior;
    private String colonia;
    private String codigoPostal;
    private String municipio;
    private String estado;
    private String noDeContrato;
    private String agente;
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
    private String agente2;
    private String status;
    private String capturista;
    @Column(name = "noservicio")
    private String noServicio;
    private String tipoDeCliente;
    private String identificadorCredito;
    private String seguridad;
    private String depuracion;
    private String folioDePagare;
    private Double saldoAlIniciarSemana;
    @Transient
    private Double porcentajeCobrado;
}

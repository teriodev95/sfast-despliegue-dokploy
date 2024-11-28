package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "prestamos_v2_historial")
public class PrestamoHistorialModel {
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
    @Column(name = "agente")
    private String agencia;
    private String gerencia;
    @Column(name = "sucursalid")
    private String sucursalId;
    private Integer semana;
    private Integer anio;
    private Integer plazo;
    private Integer montoOtorgado;
    private Double cargo;
    @Column(name = "total_a_pagar")
    private Double totalAPagar;
    private Double primer_pago;
    private Double tarifa;
    private String saldosMigrados;
    private String wkDescu;
    private Double descuento;
    private Double porcentaje;
    private Double multas;
    private String wk_refi;
    private String refin;
    private String externo;
    private Double saldo;
    private Double cobrado;
    private String TipoDeCredito;
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
    @Column(name = "agente2")
    private String agente;
    private String status;
    private String capturista;
    @Column(name = "noservicio")
    private String noServicio;
    private String tipoDeCliente;
    private String identificadorCredito;
    private String seguridad;
    private String depuracion;
    private String folioDePagare;
    private Integer excelIndex;
    private String avalPersonaId;
    private String clientePersonaId;
    private String clienteXpressId;
}

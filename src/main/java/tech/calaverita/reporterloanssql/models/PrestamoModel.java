package tech.calaverita.reporterloanssql.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Primary;

@Data
@Entity
@Table(name = "prestamos_v2")
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
    private String semana;
    private String anio;
    private String plazo;
    private double montoOtorgado;
    private double cargo;
    @Column(name = "total_a_pagar")
    private double totalAPagar;
    private double primerPago;
    private double tarifa;
    private double saldosMigrados;
    private String wkDescu;
    private double descuento;
    private double porcentaje;
    private double multas;
    private String wkRefi;
    private String refin;
    private String externo;
    private double saldo;
    private double cobrado;
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

}

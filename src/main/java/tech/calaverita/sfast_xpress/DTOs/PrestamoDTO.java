package tech.calaverita.sfast_xpress.DTOs;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;

@Data
public class PrestamoDTO {
    private String prestamoId;
    private String clienteId;
    private String noDeContrato;
    private String agencia;
    private String gerencia;
    private String sucursal;
    private Integer semana;
    private Integer anio;
    private Integer plazo;
    private Double montoOtorgado;
    private Double cargo;
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

    public PrestamoDTO() {

    }

    public PrestamoDTO(PrestamoViewModel prestamoViewModel) {
        this.prestamoId = prestamoViewModel.getPrestamoId();
        this.clienteId = prestamoViewModel.getClienteId();
        this.noDeContrato = prestamoViewModel.getNoDeContrato();
        this.agencia = prestamoViewModel.getAgencia();
        this.gerencia = prestamoViewModel.getGerencia();
        this.sucursal = prestamoViewModel.getSucursal();
        this.semana = prestamoViewModel.getSemana();
        this.anio = prestamoViewModel.getAnio();
        this.plazo = prestamoViewModel.getPlazo();
        this.montoOtorgado = prestamoViewModel.getMontoOtorgado();
        this.cargo = prestamoViewModel.getCargo();
        this.totalAPagar = prestamoViewModel.getTotalAPagar();
        this.primerPago = prestamoViewModel.getPrimerPago();
        this.tarifa = prestamoViewModel.getTarifa();
        this.saldosMigrados = prestamoViewModel.getSaldosMigrados();
        this.wkDescu = prestamoViewModel.getWkDescu();
        this.descuento = prestamoViewModel.getDescuento();
        this.porcentaje = prestamoViewModel.getPorcentaje();
        this.multas = prestamoViewModel.getMultas();
        this.wkRefi = prestamoViewModel.getWkRefi();
        this.refin = prestamoViewModel.getRefin();
        this.externo = prestamoViewModel.getExterno();
        this.saldo = prestamoViewModel.getSaldo();
        this.cobrado = prestamoViewModel.getCobrado();
        this.tipoDeCredito = prestamoViewModel.getTipoDeCredito();
        this.aclaracion = prestamoViewModel.getAclaracion();
        this.diaDePago = prestamoViewModel.getDiaDePago();
        this.gerenteEnTurno = prestamoViewModel.getGerenteEnTurno();
        this.agente = prestamoViewModel.getAgente();
        this.status = prestamoViewModel.getStatus();
        this.capturista = prestamoViewModel.getCapturista();
        this.noServicio = prestamoViewModel.getNoServicio();
        this.tipoDeCliente = prestamoViewModel.getTipoDeCliente();
        this.identificadorCredito = prestamoViewModel.getIdentificadorCredito();
        this.seguridad = prestamoViewModel.getSeguridad();
        this.depuracion = prestamoViewModel.getDepuracion();
        this.folioDePagare = prestamoViewModel.getFolioDePagare();
        this.saldoAlIniciarSemana = prestamoViewModel.getSaldoAlIniciarSemana();
        this.excelIndex = prestamoViewModel.getExcelIndex();
    }
}

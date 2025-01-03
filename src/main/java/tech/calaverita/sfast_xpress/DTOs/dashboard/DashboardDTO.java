package tech.calaverita.sfast_xpress.DTOs.dashboard;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.cobranza.DebitosCobranzaDTO;
import tech.calaverita.sfast_xpress.DTOs.cobranza.InfoCobranzaDTO;
import tech.calaverita.sfast_xpress.models.VentaModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardDTO {
        private String gerencia;
        private String agencia;
        private Integer anio;
        private Integer semana;
        private Integer clientes;
        private Integer clientesCobrados;
        private Integer noPagos;
        private Integer numeroLiquidaciones;
        private Integer pagosReducidos;
        private Double debitoMiercoles;
        private Double debitoJueves;
        private Double debitoViernes;
        private Double debitoTotal;
        private Double rendimiento;
        private Double totalDeDescuento;
        private Double totalCobranzaPura;
        private Double montoExcedente;
        private Double multas;
        private Double liquidaciones;
        private Double cobranzaTotal;
        private Double montoDeDebitoFaltante;
        private Double efectivoEnCampo;
        private String statusAgencia;
        private Boolean isCerrada;
        private Integer numeroVentas;
        private Double ventas;

        public DashboardDTO() {
                dashboardWithEmptyValues();
        }

        public DashboardDTO(InfoCobranzaDTO infoCobranzaDTO,
                        PagosDashboardDTO pagosDashboardDTO, LiquidacionesDashboardDTO liquidacionesDashboardDTO,
                        DebitosCobranzaDTO debitosCobranzaDTO, CierreDashboardDTO cierreDashboardDTO,
                        ArrayList<VentaModel> ventaModels) {
                this.gerencia = infoCobranzaDTO.getGerencia();
                this.agencia = infoCobranzaDTO.getAgencia();
                this.anio = infoCobranzaDTO.getAnio();
                this.semana = infoCobranzaDTO.getSemana();
                this.clientes = infoCobranzaDTO.getClientes();
                this.clientesCobrados = pagosDashboardDTO.getClientesCobrados();
                this.noPagos = pagosDashboardDTO.getNoPagos();
                this.numeroLiquidaciones = pagosDashboardDTO.getNumeroLiquidaciones();
                this.pagosReducidos = pagosDashboardDTO.getPagosReducidos();
                this.debitoMiercoles = debitosCobranzaDTO.getDebitoMiercoles();
                this.debitoJueves = debitosCobranzaDTO.getDebitoJueves();
                this.debitoViernes = debitosCobranzaDTO.getDebitoViernes();
                this.debitoTotal = debitosCobranzaDTO.getDebitoTotal();
                this.rendimiento = cierreDashboardDTO.getRendimiento();
                this.totalDeDescuento = liquidacionesDashboardDTO.getTotalDeDescuento();
                this.totalCobranzaPura = pagosDashboardDTO.getTotalCobranzaPura();
                this.montoExcedente = pagosDashboardDTO.getMontoExcedente();
                this.multas = pagosDashboardDTO.getMultas();
                this.liquidaciones = liquidacionesDashboardDTO.getLiquidaciones();
                this.cobranzaTotal = pagosDashboardDTO.getCobranzaTotal();
                this.montoDeDebitoFaltante = pagosDashboardDTO.getMontoDeDebitoFaltante();
                this.efectivoEnCampo = cierreDashboardDTO.getEfectivoEnCampo();
                this.statusAgencia = cierreDashboardDTO.getStatusAgencia();
                this.numeroVentas = ventaModels.size();
                this.ventas = ventaModels.stream().mapToDouble(VentaModel::getMonto).sum();
        }

        public DashboardDTO(ArrayList<DashboardDTO> dashboardDTOs) {
                dashboardWithEmptyValues();

                for (DashboardDTO dashboard : dashboardDTOs) {
                        if (this.getGerencia() == null) {
                                this.setGerencia(dashboard.getGerencia());
                                this.setAnio(dashboard.getAnio());
                                this.setSemana(dashboard.getSemana());
                        }

                        this.setClientes(this.getClientes() + dashboard.getClientes());
                        this.setClientesCobrados(this.getClientesCobrados() + dashboard
                                        .getClientesCobrados());
                        this.setNoPagos(this.getNoPagos() + dashboard.getNoPagos());
                        this.setNumeroLiquidaciones(this.getNumeroLiquidaciones() + dashboard
                                        .getNumeroLiquidaciones());
                        this.setPagosReducidos(this.getPagosReducidos() + dashboard
                                        .getPagosReducidos());
                        this.setDebitoMiercoles(this.getDebitoMiercoles() + dashboard
                                        .getDebitoMiercoles());
                        this.setDebitoJueves(this.getDebitoJueves() + dashboard.getDebitoJueves());
                        this.setDebitoViernes(this.getDebitoViernes() + dashboard.getDebitoViernes());
                        this.setDebitoTotal(this.getDebitoTotal() + dashboard.getDebitoTotal());
                        this.setTotalDeDescuento(this.getTotalDeDescuento() + dashboard
                                        .getTotalDeDescuento());
                        this.setTotalCobranzaPura(this.getTotalCobranzaPura() + dashboard
                                        .getTotalCobranzaPura());
                        this.setMontoExcedente(this.getMontoExcedente() + dashboard
                                        .getMontoExcedente());
                        this.setMultas(this.getMultas() + dashboard.getMultas());
                        this.setLiquidaciones(this.getLiquidaciones() + dashboard.getLiquidaciones());
                        this.setCobranzaTotal(this.getCobranzaTotal() + dashboard.getCobranzaTotal());
                        this.setMontoDeDebitoFaltante(this.getMontoDeDebitoFaltante() + dashboard
                                        .getMontoDeDebitoFaltante());
                        this.setNumeroVentas(this.numeroVentas + dashboard.getNumeroVentas());
                        this.setVentas(this.ventas + dashboard.getVentas());
                }

                this.setRendimiento(Math.round(this.getTotalCobranzaPura() / this
                                .getDebitoTotal() * 100.0) / 100.0 * 100.0);

                dashboardFormatDoubles();
        }

        private void dashboardWithEmptyValues() {
                this.setClientes(0);
                this.setClientesCobrados(0);
                this.setNoPagos(0);
                this.setNumeroLiquidaciones(0);
                this.setPagosReducidos(0);
                this.setDebitoMiercoles(0.0);
                this.setDebitoJueves(0.0);
                this.setDebitoViernes(0.0);
                this.setDebitoTotal(0.0);
                this.setRendimiento(0.0);
                this.setTotalDeDescuento(0.0);
                this.setTotalCobranzaPura(0.0);
                this.setMontoExcedente(0.0);
                this.setMultas(0.0);
                this.setLiquidaciones(0.0);
                this.setCobranzaTotal(0.0);
                this.setMontoDeDebitoFaltante(0.0);
                this.setNumeroVentas(0);
                this.setVentas(0.0);
        }

        private void dashboardFormatDoubles() {
                this.setDebitoMiercoles(MyUtil.getDouble(this.debitoMiercoles));
                this.setDebitoJueves(MyUtil.getDouble(this.debitoJueves));
                this.setDebitoViernes(MyUtil.getDouble(this.debitoViernes));
                this.setDebitoTotal(MyUtil.getDouble(this.debitoTotal));
                this.setRendimiento(MyUtil.getDouble(this.rendimiento));
                this.setTotalDeDescuento(MyUtil.getDouble(this.totalDeDescuento));
                this.setTotalCobranzaPura(MyUtil.getDouble(this.totalCobranzaPura));
                this.setMontoExcedente(MyUtil.getDouble(this.montoExcedente));
                this.setMultas(MyUtil.getDouble(this.multas));
                this.setLiquidaciones(MyUtil.getDouble(this.liquidaciones));
                this.setCobranzaTotal(MyUtil.getDouble(this.cobranzaTotal));
                this.setMontoDeDebitoFaltante(MyUtil.getDouble(this.montoDeDebitoFaltante));
                this.setVentas(MyUtil.getDouble(this.ventas));
        }
}

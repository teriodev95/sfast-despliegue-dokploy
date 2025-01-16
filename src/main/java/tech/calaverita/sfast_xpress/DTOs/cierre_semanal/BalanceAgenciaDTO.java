package tech.calaverita.sfast_xpress.DTOs.cierre_semanal;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;

@Data
public class BalanceAgenciaDTO {
    private String zona;
    private String gerente;
    private String agencia;
    private String agente;
    private Double rendimiento;
    private String nivel;
    private String nivelCalculado;
    private Integer clientes;
    private Integer pagosReducidos;
    private Integer noPagos;
    private Integer liquidaciones;

    public BalanceAgenciaDTO() {

    }

    public BalanceAgenciaDTO(CierreSemanalModel cierreSemanalModel) {
        this.setZona(cierreSemanalModel.getGerencia());
        this.setAgencia(cierreSemanalModel.getAgencia());
        this.setRendimiento(cierreSemanalModel.getRendimiento());
        this.setNivel(cierreSemanalModel.getNivel());
        this.setClientes(cierreSemanalModel.getClientes());
        this.setPagosReducidos(cierreSemanalModel.getPagosReducidos());
        this.setNoPagos(cierreSemanalModel.getNoPagos());
        this.setLiquidaciones(cierreSemanalModel.getClientesLiquidados());
    }

    public BalanceAgenciaDTO(CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model,
            ComisionModel comisionModel) {
        this.setAgente(cierreSemanalConsolidadoV2Model.getAgente());
        this.setGerente(cierreSemanalConsolidadoV2Model.getGerente());
        this.setZona(cierreSemanalConsolidadoV2Model.getGerencia());
        this.setAgencia(cierreSemanalConsolidadoV2Model.getAgencia());
        if (comisionModel != null) {
            this.setRendimiento(comisionModel.getRendimiento());
            this.setNivelCalculado(comisionModel.getNivel());
        }
        this.setClientes(cierreSemanalConsolidadoV2Model.getClientes());
        this.setPagosReducidos(cierreSemanalConsolidadoV2Model.getPagosReducidos());
        this.setNoPagos(cierreSemanalConsolidadoV2Model.getNoPagos());
        this.setLiquidaciones(cierreSemanalConsolidadoV2Model.getClientesLiquidados());
    }
}

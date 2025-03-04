package tech.calaverita.sfast_xpress.models.mariaDB;

import com.google.gson.Gson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal.DetalleCierreSemanalDto;
import tech.calaverita.sfast_xpress.DTOs.flujo_efectivo.FlujoEfectivoDto;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;

@Data
@Entity
@Table(name = "cierres_gerencias")
public class CierreGerenciaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer tabuladorId;
    private Integer creadoPor;
    private Integer semana;
    private Integer anio;
    private Integer numClientes;
    private Integer numNoPagos;
    private Integer numReducidos;
    private Integer numClientesLiquidacion;
    private Double descuentoPorLiquidacion;
    private Double debito;
    private Double pura;
    private Double faltante;
    private Double eficiencia;
    private String gerencia;
    private String detalleCierre;
    private String flujoEfectivo;

    public CierreGerenciaModel() {
    }

    public CierreGerenciaModel(CobranzaAgencia cobranzaAgencia, DetalleCierreSemanalDto detalleCierreSemanalDto,
            FlujoEfectivoDto flujoEfectivoDto, int tabuladorId, int creadoPor) {
        this.tabuladorId = tabuladorId;
        this.creadoPor = creadoPor;
        this.semana = cobranzaAgencia.getSemana();
        this.anio = cobranzaAgencia.getAnio();
        this.numClientes = cobranzaAgencia.getClientes();
        this.numNoPagos = cobranzaAgencia.getNoPagos();
        this.numReducidos = cobranzaAgencia.getPagosReducidos();
        this.numClientesLiquidacion = cobranzaAgencia.getClientesLiquidados();
        this.descuentoPorLiquidacion = cobranzaAgencia.getDescuentoPorLiquidacion();
        this.debito = cobranzaAgencia.getDebito();
        this.pura = cobranzaAgencia.getCobranzaPura();
        this.faltante = cobranzaAgencia.getFaltante();
        this.eficiencia = cobranzaAgencia.getEficiencia();
        this.gerencia = cobranzaAgencia.getGerencia();
        this.detalleCierre = new Gson().toJson(detalleCierreSemanalDto);
        this.flujoEfectivo = new Gson().toJson(flujoEfectivoDto);
    }
}

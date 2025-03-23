package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlujoEfectivoDto {
    @JsonProperty(value = "asignaciones")
    private FlujoEfectivoAsignacionesDto asignacionesFlujoEfectivoDto;
    @JsonProperty(value = "otros")
    private FlujoEfectivoOtrosDto otrosFlujoEfectivoDto;
    @JsonProperty(value = "gastosSemanales")
    private FlujoEfectivoGastosFlujoEfectivoDto gastosFlujoEfectivoDto;
    @JsonProperty(value = "ventas")
    private HashMap<String, Object> ventasFlujoEfectivoHm;
    @JsonProperty(value = "primerosPagos")
    private HashMap<String, Object> primerosPagosFlujoEfectivoHm;
    @JsonProperty(value = "resumen")
    private HashMap<String, Double> resumenFlujoEfectivoHm;
    @JsonProperty(value = "cobranzaAgenciasVacantes")
    private HashMap<String, Double> cobranzaAgenciasVacantesHm;

    public FlujoEfectivoDto() {
        initializeVentasFlujoEfectivoHm();
        initializePrimerosPagosFlujoEfectivoHm();
        initializeResumenFlujoEfectivoHm();
        initializeCobranzaAgenciasVacantesHm();
    }

    @SuppressWarnings("unchecked")
    public FlujoEfectivoDto(AlmacenObjects almacenObjects, String gerencia) {
        this();

        List<AsignacionModel> recibioAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("recibioAsignacionModels");
        List<AsignacionModel> entregoAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("entregoAsignacionModels");
        List<GastoModel> gastoModels = (List<GastoModel>) almacenObjects
                .getObject("gastoModels");
        List<IncidenteReposicionModel> incidenteReposicionModels = (List<IncidenteReposicionModel>) almacenObjects
                .getObject("incidenteReposicionModels");
        List<PagoDynamicModel> pagoDynamicModels = (List<PagoDynamicModel>) almacenObjects
                .getObject("pagoDynamicModels");
        List<VentaModel> ventaModels = (List<VentaModel>) almacenObjects
                .getObject("ventaModels");

        this.asignacionesFlujoEfectivoDto = new FlujoEfectivoAsignacionesDto(recibioAsignacionModels,
                entregoAsignacionModels, gerencia);
        this.otrosFlujoEfectivoDto = new FlujoEfectivoOtrosDto(incidenteReposicionModels);
        this.gastosFlujoEfectivoDto = new FlujoEfectivoGastosFlujoEfectivoDto(gastoModels);
        setVentasYPrimerosPagos(ventaModels);
        setResumenFlujoEfectivoHm();
        setCobranzaAgenciasVacantes(pagoDynamicModels);
    }

    @SuppressWarnings("unchecked")
    public FlujoEfectivoDto(AlmacenObjects almacenObjects, int usuarioId) {
        initializeResumenFlujoEfectivoHm();

        List<AsignacionModel> recibioAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("recibioAsignacionModels");
        List<AsignacionModel> entregoAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("entregoAsignacionModels");
        List<GastoModel> gastoModels = (List<GastoModel>) almacenObjects
                .getObject("gastoModels");
        List<IncidenteReposicionModel> incidenteReposicionModels = (List<IncidenteReposicionModel>) almacenObjects
                .getObject("incidenteReposicionModels");

        this.asignacionesFlujoEfectivoDto = new FlujoEfectivoAsignacionesDto(recibioAsignacionModels,
                entregoAsignacionModels, usuarioId);
        this.otrosFlujoEfectivoDto = new FlujoEfectivoOtrosDto(incidenteReposicionModels);
        this.gastosFlujoEfectivoDto = new FlujoEfectivoGastosFlujoEfectivoDto(gastoModels);
        setResumenFlujoEfectivoUsuarioIdHm();
    }

    private void initializeVentasFlujoEfectivoHm() {
        this.ventasFlujoEfectivoHm = new HashMap<>();
        this.ventasFlujoEfectivoHm.put("tipo", "Egreso");
        this.ventasFlujoEfectivoHm.put("total", 0D);
        this.ventasFlujoEfectivoHm.put("cantidad", 0);
    }

    private void initializePrimerosPagosFlujoEfectivoHm() {
        this.primerosPagosFlujoEfectivoHm = new HashMap<>();
        this.primerosPagosFlujoEfectivoHm.put("tipo", "Ingreso");
        this.primerosPagosFlujoEfectivoHm.put("total", 0D);
        this.primerosPagosFlujoEfectivoHm.put("cantidad", 0);
    }

    private void initializeResumenFlujoEfectivoHm() {
        this.resumenFlujoEfectivoHm = new HashMap<>();
        this.resumenFlujoEfectivoHm.put("ingresos", 0D);
        this.resumenFlujoEfectivoHm.put("egresos", 0D);
        this.resumenFlujoEfectivoHm.put("diferencia", 0D);
    }

    private void initializeCobranzaAgenciasVacantesHm() {
        this.cobranzaAgenciasVacantesHm = new HashMap<>();
        this.cobranzaAgenciasVacantesHm.put("totalIngresos", 0D);
    }

    private void setVentasYPrimerosPagos(List<VentaModel> ventaModels) {
        double totalVentas = 0D;
        double totalPrimerosPagos = 0D;

        for (VentaModel ventaModel : ventaModels) {
            totalVentas += ventaModel.getMonto();
            totalPrimerosPagos += ventaModel.getPrimerPago();
        }

        this.ventasFlujoEfectivoHm.put("total", MyUtil.getDouble(totalVentas));
        this.ventasFlujoEfectivoHm.put("cantidad", ventaModels.size());
        this.primerosPagosFlujoEfectivoHm.put("total", MyUtil.getDouble(totalPrimerosPagos));
        this.primerosPagosFlujoEfectivoHm.put("cantidad", ventaModels.size());
    }

    private void setResumenFlujoEfectivoHm() {
        double ingresos = 0D;
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAgenteDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAdministracionDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesSeguridadDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesOperacionDto().getSubTotalIngresos();
        ingresos += this.otrosFlujoEfectivoDto.getSubTotalIngresos();
        ingresos += (Double) this.primerosPagosFlujoEfectivoHm.get("total");

        double egresos = 0D;
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAdministracionDto().getSubTotalEgresos();
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesSeguridadDto().getSubTotalEgresos();
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesOperacionDto().getSubTotalEgresos();
        egresos += this.otrosFlujoEfectivoDto.getSubTotalEgresos();
        egresos += this.gastosFlujoEfectivoDto.getSubTotalGastos();
        egresos += (Double) this.ventasFlujoEfectivoHm.get("total");

        this.resumenFlujoEfectivoHm.put("ingresos", MyUtil.getDouble(ingresos));
        this.resumenFlujoEfectivoHm.put("egresos", MyUtil.getDouble(egresos));
        this.resumenFlujoEfectivoHm.put("diferencia", MyUtil.getDouble(ingresos - egresos));
    }

    private void setResumenFlujoEfectivoUsuarioIdHm() {
        double ingresos = 0D;
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAgenteDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAdministracionDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesSeguridadDto().getSubTotalIngresos();
        ingresos += this.asignacionesFlujoEfectivoDto.getAsignacionesOperacionDto().getSubTotalIngresos();
        ingresos += this.otrosFlujoEfectivoDto.getSubTotalIngresos();

        double egresos = 0D;
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesAdministracionDto().getSubTotalEgresos();
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesSeguridadDto().getSubTotalEgresos();
        egresos += this.asignacionesFlujoEfectivoDto.getAsignacionesOperacionDto().getSubTotalEgresos();
        egresos += this.otrosFlujoEfectivoDto.getSubTotalEgresos();
        egresos += this.gastosFlujoEfectivoDto.getSubTotalGastos();

        this.resumenFlujoEfectivoHm.put("ingresos", MyUtil.getDouble(ingresos));
        this.resumenFlujoEfectivoHm.put("egresos", MyUtil.getDouble(egresos));
        this.resumenFlujoEfectivoHm.put("diferencia", MyUtil.getDouble(ingresos - egresos));
    }

    private void setCobranzaAgenciasVacantes(List<PagoDynamicModel> pagoDynamicModels) {
        double cobranzaAgenciasVacantes = 0D;
        for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
            cobranzaAgenciasVacantes += pagoDynamicModel.getMonto();
        }

        this.cobranzaAgenciasVacantesHm.put("totalIngresos", cobranzaAgenciasVacantes);
    }
}

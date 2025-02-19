package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
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

    public FlujoEfectivoDto() {
        initializeVentasFlujoEfectivoHm();
        initializePrimerosPagosFlujoEfectivoHm();
        initializeResumenFlujoEfectivoHm();
    }

    @SuppressWarnings("unchecked")
    public FlujoEfectivoDto(AlmacenObjects almacenObjects, String gerencia) {
        this();

        List<AsignacionModel> recibioAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("recibioAsignacionModels");
        List<AsignacionModel> entregoAsignacionModels = (List<AsignacionModel>) almacenObjects
                .getObject("entregoAsignacionModels");
        List<IncidenteReposicionModel> incidenteReposicionModels = (List<IncidenteReposicionModel>) almacenObjects
                .getObject("incidenteReposicionModels");
        List<GastoModel> gastoModels = (List<GastoModel>) almacenObjects
                .getObject("gastoModels");
        List<VentaModel> ventaModels = (List<VentaModel>) almacenObjects
                .getObject("ventaModels");

        this.asignacionesFlujoEfectivoDto = new FlujoEfectivoAsignacionesDto(recibioAsignacionModels,
                entregoAsignacionModels, gerencia);
        this.otrosFlujoEfectivoDto = new FlujoEfectivoOtrosDto(incidenteReposicionModels);
        this.gastosFlujoEfectivoDto = new FlujoEfectivoGastosFlujoEfectivoDto(gastoModels);
        setVentasYPrimerosPagos(ventaModels);
        setResumenFlujoEfectivoHm();
    }

    public void initializeVentasFlujoEfectivoHm() {
        this.ventasFlujoEfectivoHm = new HashMap<>();
        this.ventasFlujoEfectivoHm.put("tipo", "Egreso");
        this.ventasFlujoEfectivoHm.put("total", 0D);
        this.ventasFlujoEfectivoHm.put("cantidad", 0);
    }

    public void initializePrimerosPagosFlujoEfectivoHm() {
        this.primerosPagosFlujoEfectivoHm = new HashMap<>();
        this.primerosPagosFlujoEfectivoHm.put("tipo", "Ingreso");
        this.primerosPagosFlujoEfectivoHm.put("total", 0D);
        this.primerosPagosFlujoEfectivoHm.put("cantidad", 0);
    }

    public void initializeResumenFlujoEfectivoHm() {
        this.resumenFlujoEfectivoHm = new HashMap<>();
        this.resumenFlujoEfectivoHm.put("ingresos", 0D);
        this.resumenFlujoEfectivoHm.put("egresos", 0D);
        this.resumenFlujoEfectivoHm.put("diferencia", 0D);
    }

    public void setVentasYPrimerosPagos(List<VentaModel> ventaModels) {
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

    public void setResumenFlujoEfectivoHm() {
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
        egresos += this.otrosFlujoEfectivoDto.getSubTotalIngresos();
        egresos += this.gastosFlujoEfectivoDto.getSubTotalGastos();
        egresos += (Double) this.ventasFlujoEfectivoHm.get("total");

        this.resumenFlujoEfectivoHm.put("ingresos", MyUtil.getDouble(ingresos));
        this.resumenFlujoEfectivoHm.put("egresos", MyUtil.getDouble(egresos));
        this.resumenFlujoEfectivoHm.put("diferencia", MyUtil.getDouble(ingresos - egresos));
    }
}

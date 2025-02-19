package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;

@Data
public class FlujoEfectivoAsignacionesDto {
    @JsonProperty(value = "agentes")
    private FlujoEfectivoAsignacionesAgentesDto asignacionesAgenteDto;
    @JsonProperty(value = "administracion")
    private FlujoEfectivoAsignacionDto asignacionesAdministracionDto;
    @JsonProperty(value = "seguridad")
    private FlujoEfectivoAsignacionDto asignacionesSeguridadDto;
    @JsonProperty(value = "operaciones")
    private FlujoEfectivoAsignacionDto asignacionesOperacionDto;

    public FlujoEfectivoAsignacionesDto() {

    }

    public FlujoEfectivoAsignacionesDto(List<AsignacionModel> recibioAsignacionModels,
            List<AsignacionModel> entregoAsignacionModels, String gerencia) {
        List<AsignacionModel> asignacionesAgenteAsignacionModel = new ArrayList<>();
        List<AsignacionModel> asignacionesAdministracionAsignacionModel = new ArrayList<>();
        List<AsignacionModel> asignacionesSeguridadAsignacionModel = new ArrayList<>();
        List<AsignacionModel> asignacionesOperacionAsignacionModel = new ArrayList<>();

        AlmacenObjects almacenObjects = new AlmacenObjects();
        almacenObjects.addObject("asignacionesAgenteAsignacionModel", asignacionesAgenteAsignacionModel);
        almacenObjects.addObject("asignacionesAdministracionAsignacionModel",
                asignacionesAdministracionAsignacionModel);
        almacenObjects.addObject("asignacionesSeguridadAsignacionModel", asignacionesSeguridadAsignacionModel);
        almacenObjects.addObject("asignacionesOperacionAsignacionModel", asignacionesOperacionAsignacionModel);

        setAsignaciones(recibioAsignacionModels, almacenObjects, true);
        setAsignaciones(entregoAsignacionModels, almacenObjects, false);

        this.asignacionesAgenteDto = new FlujoEfectivoAsignacionesAgentesDto(asignacionesAgenteAsignacionModel);
        this.asignacionesAdministracionDto = new FlujoEfectivoAsignacionDto(
                asignacionesAdministracionAsignacionModel, gerencia);
        this.asignacionesSeguridadDto = new FlujoEfectivoAsignacionDto(asignacionesSeguridadAsignacionModel, gerencia);
        this.asignacionesOperacionDto = new FlujoEfectivoAsignacionDto(asignacionesOperacionAsignacionModel, gerencia);
    }

    @SuppressWarnings("unchecked")
    public void setAsignaciones(List<AsignacionModel> asignacionModels, AlmacenObjects almacenObjects,
            boolean recibio) {
        // To easy code
        List<AsignacionModel> asignacionesAgenteAsignacionModel = (List<AsignacionModel>) almacenObjects
                .getObject("asignacionesAgenteAsignacionModel");
        List<AsignacionModel> asignacionesAdministracionAsignacionModel = (List<AsignacionModel>) almacenObjects
                .getObject("asignacionesAdministracionAsignacionModel");
        List<AsignacionModel> asignacionesSeguridadAsignacionModel = (List<AsignacionModel>) almacenObjects
                .getObject("asignacionesSeguridadAsignacionModel");
        List<AsignacionModel> asignacionesOperacionAsignacionModel = (List<AsignacionModel>) almacenObjects
                .getObject("asignacionesOperacionAsignacionModel");

        for (AsignacionModel asignacionModel : asignacionModels) {
            // To easy code
            String tipo = recibio ? asignacionModel.getEntregoUsuarioModel().getTipo()
                    : asignacionModel
                            .getRecibioUsuarioModel().getTipo();

            switch (tipo) {
                case "Agente":
                    asignacionesAgenteAsignacionModel.add(asignacionModel);
                    break;
                case "Jefe de Admin":
                    asignacionesAdministracionAsignacionModel.add(asignacionModel);
                    break;
                case "Seguridad":
                    asignacionesSeguridadAsignacionModel.add(asignacionModel);
                    break;
                case "Gerente":
                    asignacionesOperacionAsignacionModel.add(asignacionModel);
                    break;
                default:
                    break;
            }
        }
    }

    public FlujoEfectivoAsignacionesDto(FlujoEfectivoAsignacionesAgentesDto asignacionesAgentes,
            FlujoEfectivoAsignacionDto asignacionesAdministracionDto,
            FlujoEfectivoAsignacionDto asignacionesSeguridadDto,
            FlujoEfectivoAsignacionDto asignacionesOperacionesDto) {
        this.asignacionesAgenteDto = asignacionesAgentes;
        this.asignacionesAdministracionDto = asignacionesAdministracionDto;
        this.asignacionesSeguridadDto = asignacionesSeguridadDto;
        this.asignacionesOperacionDto = asignacionesOperacionesDto;
    }
}

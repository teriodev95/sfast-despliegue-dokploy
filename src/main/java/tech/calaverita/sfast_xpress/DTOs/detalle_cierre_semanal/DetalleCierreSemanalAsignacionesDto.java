package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalAsignacionesDto {
    private Double cajaChica;
    private Double seguridad;
    private Double operacion;
    private Double regional;
    private Double incidente;
    private Double total;

    public DetalleCierreSemanalAsignacionesDto() {
        this.cajaChica = 0D;
        this.seguridad = 0D;
        this.operacion = 0D;
        this.regional = 0D;
        this.incidente = 0D;
        this.total = 0D;
    }

    public DetalleCierreSemanalAsignacionesDto(List<AsignacionModel> asignacionModels,
            List<IncidenteReposicionModel> incidenteReposicionModels, String tipoAsignaciones) {
        this();

        for (AsignacionModel asignacionModel : asignacionModels) {
            // To easy code
            UsuarioModel usuarioModel = tipoAsignaciones.equals("ingreso") ? asignacionModel.getEntregoUsuarioModel()
                    : asignacionModel.getRecibioUsuarioModel();
            String tipoUsuario = usuarioModel.getTipo();
            Double monto = asignacionModel.getMonto();

            switch (tipoUsuario) {
                case "Jefe de Admin":
                    this.cajaChica += monto;
                    break;
                case "Seguridad":
                    this.seguridad += monto;
                    break;
                // case "Agente":
                case "Gerente":
                    this.operacion += monto;
                    break;
                case "Regional":
                    this.regional += monto;
                    break;
                default:
                    break;
            }

            if (!tipoUsuario.equals("Agente")) {
                this.total += monto;
            }
        }

        for (IncidenteReposicionModel incidenteReposicionModel : incidenteReposicionModels) {
            if (tipoAsignaciones.equals("ingreso")) {
                if (incidenteReposicionModel.getTipo().equals("ingreso")) {
                    this.incidente += incidenteReposicionModel.getMonto();
                    this.total += incidenteReposicionModel.getMonto();
                }
            } else {
                if (incidenteReposicionModel.getTipo().equals("egreso")) {
                    this.incidente += incidenteReposicionModel.getMonto();
                    this.total += incidenteReposicionModel.getMonto();
                }
            }
        }

        formatDoubles();
    }

    private void formatDoubles() {
        this.cajaChica = MyUtil.getDouble(this.cajaChica);
        this.seguridad = MyUtil.getDouble(this.seguridad);
        this.operacion = MyUtil.getDouble(this.operacion);
        this.regional = MyUtil.getDouble(this.regional);
        this.incidente = MyUtil.getDouble(this.incidente);
        this.total = MyUtil.getDouble(this.total);
    }
}

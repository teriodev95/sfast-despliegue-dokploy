package tech.calaverita.sfast_xpress.validations;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;

@Service
public class AsignacionValidationService {
    private final AsignacionService asignacionService;
    private final UsuarioService usuarioService;

    public AsignacionValidationService(AsignacionService asignacionService, UsuarioService usuarioService) {
        this.asignacionService = asignacionService;
        this.usuarioService = usuarioService;
    }

    public String validateAsignacion(AsignacionModel asignacionModel) {
        String resultadoValidacion = "";

        if (this.asignacionService.existById(asignacionModel.getAsignacionId())) {
            resultadoValidacion = "La Asignación Ya Existe";
        }

        UsuarioModel optusuarMod = this.usuarioService.findById(asignacionModel.getQuienRecibioUsuarioId());

        if (optusuarMod == null) {
            resultadoValidacion = "Debe ingresar un quienRecibioUsuarioId válido";
        }

        optusuarMod = this.usuarioService.findById(asignacionModel.getQuienEntregoUsuarioId());

        if (optusuarMod == null) {
            resultadoValidacion = "Debe ingresar un quienEntregoUsuarioId válido";
        }

        if (!asignacionModel.getLog().contains("{")) {
            resultadoValidacion = "Debe ingresar un log con formato json";
        }

        if (!asignacionModel.getLog().contains("}")) {
            resultadoValidacion = "Debe ingresar un log con formato json";
        }

        return resultadoValidacion;
    }
}

package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.repositories.AsignacionRepository;
import tech.calaverita.sfast_xpress.utils.pwa.PWAUtil;

@Service
public class AsignacionService {
    private final AsignacionRepository asignacionRespository;
    private final CalendarioService calendarioService;
    private final UsuarioService usuarioService;

    public AsignacionService(AsignacionRepository asignacionRepository, CalendarioService calendarioService,
            UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        this.calendarioService = calendarioService;
        this.asignacionRespository = asignacionRepository;
    }

    public HashMap<String, Object> getByGerencia(String gerencia) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        return getByGerenciaAnioSemana(gerencia, anio, semana);
    }

    public HashMap<String, Object> getByGerenciaAnioSemana(String gerencia, int anio, int semana) {
        UsuarioModel gerente = this.usuarioService.findByGerenciaTipoAndStatus(gerencia, "Gerente", true);
        HashMap<String, Object> responseHM = new HashMap<>();

        responseHM.put("ingresos",
                findByQuienRecibioUsuarioIdAnioAndSemana(gerente.getUsuarioId(), anio, semana));
        responseHM.put("egresos",
                findByGerenciaAnioSemana(gerencia, anio, semana));

        responseHM = PWAUtil.asignacionModelPwa(responseHM);

        return responseHM;
    }

    public boolean existById(String id) {
        return this.asignacionRespository.existsById(id);
    }

    public AsignacionModel save(AsignacionModel asignacionModel) {
        return this.asignacionRespository.save(asignacionModel);
    }

    public AsignacionModel findById(String id) {
        return this.asignacionRespository.findById(id).orElse(null);
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioAndSemana(Integer quienRecibioUsuarioId, int anio,
            int semana) {
        return this.asignacionRespository.findByQuienRecibioUsuarioIdAndAnioAndSemana(quienRecibioUsuarioId, anio,
                semana);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioAndSemana(Integer quienEntregoUsuarioId, int anio,
            int semana) {
        return this.asignacionRespository.findByQuienEntregoUsuarioIdAndAnioAndSemana(quienEntregoUsuarioId, anio,
                semana);
    }

    public ArrayList<AsignacionModel> findByGerenciaAnioSemana(String gerencia, int anio,
            int semana) {
        return this.asignacionRespository.findEgresosByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdInAnioAndSemana(Integer[] quienEntregoUsuarioIds,
            int anio,
            int semana) {
        return this.asignacionRespository.findByQuienEntregoUsuarioIdInAndAnioAndSemana(quienEntregoUsuarioIds, anio,
                semana);
    }

    public ArrayList<AsignacionModel> findAll() {
        return (ArrayList<AsignacionModel>) this.asignacionRespository.findAll();
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienRecibioUsuarioId, int anio,
            int semana, String tipo) {
        return this.asignacionRespository.findByQuienRecibioUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(
                quienRecibioUsuarioId,
                anio,
                semana, tipo);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienEntregoUsuarioId, int anio,
            int semana, String tipo) {
        return this.asignacionRespository.findByQuienEntregoUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(
                quienEntregoUsuarioId,
                anio,
                semana, tipo);
    }

    public List<AsignacionModel> findByAgenciaAnioSemana(
            String agencia, int anio, int semana) {
        return this.asignacionRespository.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Double> findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
            Integer quienEntregoUsuarioId, int anio, int semana) {
        return CompletableFuture
                .completedFuture(this.asignacionRespository
                        .findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(quienEntregoUsuarioId, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<AsignacionModel>> findAsignacionesIngresoByGerenciaAnioSemanaAsync(
            String gerencia, int anio, int semana) {
        ArrayList<AsignacionModel> asignacionModels = this.asignacionRespository
                .findAsignacionesIngresoByGerenciaAndAnioAndSemana(gerencia, anio, semana);

        return CompletableFuture.completedFuture(asignacionModels);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<AsignacionModel>> findAsignacionesEgresoByGerenciaAnioSemanaAsync(
            String gerencia, int anio, int semana) {
        ArrayList<AsignacionModel> asignacionModels = this.asignacionRespository
                .findAsignacionesEgresoByGerenciaAndAnioAndSemana(gerencia, anio, semana);
        return CompletableFuture.completedFuture(asignacionModels);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<AsignacionModel>> findAsignacionesEgresoAgentesByGerenciaAnioSemanaAsync(
            String gerencia, int anio, int semana) {
        ArrayList<AsignacionModel> asignacionModels = this.asignacionRespository
                .findAsignacionesEgresoAgentesByGerenciaAndAnioAndSemana(gerencia, anio, semana);
        return CompletableFuture.completedFuture(asignacionModels);
    }

    public String getTipo(UsuarioModel entregoUsuarioModel, UsuarioModel recibioUsuarioModel) {
        String tipo = "";

        // To easy code
        String tipoUsuarioEntrego = entregoUsuarioModel.getTipo();
        String tipoUsuarioRecibio = recibioUsuarioModel.getTipo();

        if (tipoUsuarioEntrego.equals("Agente") && !tipoUsuarioRecibio.equals("Regional")) {
            tipo = "Agente";
        } else if ((tipoUsuarioEntrego.equals("Seguridad") && !tipoUsuarioRecibio.equals("Regional")
                && !tipoUsuarioRecibio.equals("Jefe de Admin"))
                || tipoUsuarioEntrego.equals("Gerente") && tipoUsuarioRecibio.equals("Seguridad")) {
            tipo = "Seguridad";
        } else if ((tipoUsuarioEntrego.equals("Gerente") && !tipoUsuarioRecibio.equals("Regional")
                && !tipoUsuarioRecibio.equals("Jefe de Admin"))
                || (tipoUsuarioEntrego.equals("Regional") && tipoUsuarioRecibio.equals("Gerente"))) {
            tipo = "Operación";
        } else if (tipoUsuarioRecibio.equals("Regional")) {
            tipo = "Regional";
        } else {
            tipo = "Admin";
        }

        return tipo;
    }
}

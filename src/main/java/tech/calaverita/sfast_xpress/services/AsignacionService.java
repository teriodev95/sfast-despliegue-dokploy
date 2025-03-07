package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.repositories.AsignacionRepository;

@Service
public class AsignacionService {
    private final AsignacionRepository repo;

    public AsignacionService(AsignacionRepository repo) {
        this.repo = repo;
    }

    public boolean existById(String id) {
        return this.repo.existsById(id);
    }

    public AsignacionModel save(AsignacionModel asignacionModel) {
        return this.repo.save(asignacionModel);
    }

    public AsignacionModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioAndSemana(Integer quienRecibioUsuarioId, int anio,
            int semana) {
        return this.repo.findByQuienRecibioUsuarioIdAndAnioAndSemana(quienRecibioUsuarioId, anio, semana);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioAndSemana(Integer quienEntregoUsuarioId, int anio,
            int semana) {
        return this.repo.findByQuienEntregoUsuarioIdAndAnioAndSemana(quienEntregoUsuarioId, anio, semana);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdInAnioAndSemana(Integer[] quienEntregoUsuarioIds,
            int anio,
            int semana) {
        return this.repo.findByQuienEntregoUsuarioIdInAndAnioAndSemana(quienEntregoUsuarioIds, anio, semana);
    }

    public ArrayList<AsignacionModel> findAll() {
        return (ArrayList<AsignacionModel>) this.repo.findAll();
    }

    public ArrayList<AsignacionModel> findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienRecibioUsuarioId, int anio,
            int semana, String tipo) {
        return this.repo.findByQuienRecibioUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(quienRecibioUsuarioId,
                anio,
                semana, tipo);
    }

    public ArrayList<AsignacionModel> findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
            Integer quienEntregoUsuarioId, int anio,
            int semana, String tipo) {
        return this.repo.findByQuienEntregoUsuarioIdAndAnioAndSemanaAndTipoInnerJoinUsuarioModel(quienEntregoUsuarioId,
                anio,
                semana, tipo);
    }

    public List<AsignacionModel> findByAgenciaAnioSemana(
            String agencia, int anio, int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Double> findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
            Integer quienEntregoUsuarioId, int anio, int semana) {
        return CompletableFuture
                .completedFuture(this.repo
                        .findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(quienEntregoUsuarioId, anio, semana));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<AsignacionModel>> findAsignacionesIngresoByGerenciaAnioSemanaAsync(
            String gerencia, int anio, int semana) {
        ArrayList<AsignacionModel> asignacionModels = this.repo
                .findAsignacionesIngresoByGerenciaAndAnioAndSemana(gerencia, anio, semana);

        return CompletableFuture.completedFuture(asignacionModels);
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<AsignacionModel>> findAsignacionesEgresoByGerenciaAnioSemanaAsync(
            String gerencia, int anio, int semana) {
        ArrayList<AsignacionModel> asignacionModels = this.repo
                .findAsignacionesEgresoByGerenciaAndAnioAndSemana(gerencia, anio, semana);
        return CompletableFuture.completedFuture(asignacionModels);
    }

    public String getTipo(UsuarioModel entregoUsuarioModel, UsuarioModel recibioUsuarioModel) {
        String tipo = "";

        // To easy code
        String tipoUsuarioEntrego = entregoUsuarioModel.getTipo();
        String tipoUsuarioRecibio = recibioUsuarioModel.getTipo();

        if (tipoUsuarioEntrego.equals("Agente")) {
            tipo = "Agente";
        } else if (tipoUsuarioEntrego.equals("Seguridad") && !tipoUsuarioRecibio.equals("Jefe de Admin")) {
            tipo = "Seguridad";
        } else if (tipoUsuarioEntrego.equals("Gerente") && !tipoUsuarioRecibio.equals("Jefe de Admin")) {
            tipo = "Operación";
        } else {
            tipo = "Admin";
        }

        return tipo;
    }

    public HashMap<String, String> getAgenciaAndGerencia(UsuarioModel entregoUsuarioModel,
            UsuarioModel recibioUsuarioModel) {
        HashMap<String, String> agenciaYGerencia = new HashMap<>();
        agenciaYGerencia.put("agencia", "");
        agenciaYGerencia.put("gerencia", "");

        // To easy code
        String tipoEntrego = entregoUsuarioModel.getTipo();
        String tipoRecibio = recibioUsuarioModel.getTipo();

        if (!(tipoEntrego.equals("Gerente") && tipoRecibio.equals("Gerente"))) {
            setAgenciaYGerencia(agenciaYGerencia, entregoUsuarioModel);

            if (agenciaYGerencia.get("gerencia").isEmpty()) {
                setAgenciaYGerencia(agenciaYGerencia, recibioUsuarioModel);
            }
        }

        return agenciaYGerencia;
    }

    private void setAgenciaYGerencia(HashMap<String, String> agenciaYGerencia, UsuarioModel usuarioModel) {
        switch (usuarioModel.getTipo()) {
            case "Agente":
                agenciaYGerencia.put("agencia", usuarioModel.getAgencia());
                agenciaYGerencia.put("gerencia", usuarioModel.getGerencia());
                break;
            case "Gerente":
                agenciaYGerencia.put("gerencia", usuarioModel.getGerencia());
                break;
            default:
                break;
        }
    }
}

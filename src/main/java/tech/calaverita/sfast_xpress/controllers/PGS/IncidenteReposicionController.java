package tech.calaverita.sfast_xpress.controllers.PGS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.repositories.IncidenteReposicionRepository;
import tech.calaverita.sfast_xpress.services.IncidenteReposicionService;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/xpress/v1/pwa/incidentes-reposiciones")
@CrossOrigin
public class IncidenteReposicionController {
    private final IncidenteReposicionRepository incidenteReposicionRepository;
    private final IncidenteReposicionService incidenteReposicionService;

    public IncidenteReposicionController(IncidenteReposicionRepository incidenteReposicionRepository,
            IncidenteReposicionService incidenteReposicionService) {
        this.incidenteReposicionRepository = incidenteReposicionRepository;
        this.incidenteReposicionService = incidenteReposicionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<IncidenteReposicionModel>> getAll() {
        HttpStatus httpStatus = HttpStatus.OK;

        List<IncidenteReposicionModel> incidentes = (List<IncidenteReposicionModel>) incidenteReposicionRepository
                .findAll();

        if (incidentes.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(incidentes, httpStatus);
    }

    @GetMapping("/gerencia/{gerencia}/anio/{anio}/semana/{semana}")
    public ResponseEntity<List<IncidenteReposicionModel>> filterByGerenciaAnioSemana(
            @PathVariable String gerencia,
            @PathVariable int anio,
            @PathVariable int semana) {

        try {
            List<IncidenteReposicionModel> resultados = incidenteReposicionService
                    .findByGerenciaAnioSemanaAsync(gerencia, anio, semana)
                    .get();
            return ResponseEntity.ok(resultados);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/usuarioId/{usuarioId}/anio/{anio}/semana/{semana}")
    public ResponseEntity<List<IncidenteReposicionModel>> filterByGerenciaAnioSemana(
            @PathVariable int usuarioId,
            @PathVariable int anio,
            @PathVariable int semana) {

        try {
            List<IncidenteReposicionModel> resultados = this.incidenteReposicionService
                    .findByUsuarioIdAnioSemanaAsync(usuarioId, anio, semana)
                    .get();
            return ResponseEntity.ok(resultados);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create-one")
    public ResponseEntity<Object> create(@RequestBody IncidenteReposicionModel incidente) {
        try {
            // Validación simple (puedes agregar más con @Valid)
            if (incidente.getCategoria() == null || incidente.getCategoria().length() == 0
                    || incidente.getTipo() == null || incidente.getTipo().length() == 0
                    || incidente.getMonto() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Los campos 'categoria', 'tipo' y 'monto' son obligatorios"));
            }

            if (incidente.getMonto() == 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", "El monto no puede ser 0"));
            }

            // incidente.setFecha(LocalDate.now(ZoneId.of("America/Mexico_City")));

            IncidenteReposicionModel savedIncidente = incidenteReposicionRepository.save(incidente);
            return ResponseEntity.ok(savedIncidente);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Error al guardar el incidente: " + e.getMessage()));
        }

    }

}

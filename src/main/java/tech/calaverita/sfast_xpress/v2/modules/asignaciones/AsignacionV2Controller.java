package tech.calaverita.sfast_xpress.v2.modules.asignaciones;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController("asignacionV2Controller")
@RequestMapping("/api/v2/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionV2Controller {
    private final AsignacionV2Service asignacionService;

    public AsignacionV2Controller(AsignacionV2Service asignacionService) {
        this.asignacionService = asignacionService;
    }

    @GetMapping
    public ResponseEntity<List<AsignacionV2Model>> getAllAsignaciones() {
        List<AsignacionV2Model> asignaciones = asignacionService.findAll();
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionV2Model> getAsignacionById(@PathVariable String id) {
        return asignacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/agencia/{agencia}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByAgencia(@PathVariable String agencia) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByAgencia(agencia);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/gerencia-entrega/{gerencia}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByGerenciaEntrega(@PathVariable String gerencia) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByGerenciaEntrega(gerencia);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/gerencia-recibe/{gerencia}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByGerenciaRecibe(@PathVariable String gerencia) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByGerenciaRecibe(gerencia);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/quien-entrego/{usuarioId}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByQuienEntrego(@PathVariable Integer usuarioId) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByQuienEntrego(usuarioId);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/quien-recibio/{usuarioId}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByQuienRecibio(@PathVariable Integer usuarioId) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByQuienRecibio(usuarioId);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/periodo/{anio}/{semana}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByPeriodo(
            @PathVariable Integer anio,
            @PathVariable Integer semana) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByAnioAndSemana(anio, semana);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping("/agencia/{agencia}/periodo/{anio}/{semana}")
    public ResponseEntity<List<AsignacionV2Model>> getAsignacionesByAgenciaAndPeriodo(
            @PathVariable String agencia,
            @PathVariable Integer anio,
            @PathVariable Integer semana) {
        List<AsignacionV2Model> asignaciones = asignacionService.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
        return ResponseEntity.ok(asignaciones);
    }

    @PostMapping
    public ResponseEntity<AsignacionV2Model> createAsignacion(@RequestBody AsignacionV2Model asignacion) {
        AsignacionV2Model savedAsignacion = asignacionService.save(asignacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAsignacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionV2Model> updateAsignacion(
            @PathVariable String id,
            @RequestBody AsignacionV2Model asignacion) {
        if (!asignacionService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        asignacion.setId(id);
        AsignacionV2Model updatedAsignacion = asignacionService.save(asignacion);
        return ResponseEntity.ok(updatedAsignacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignacion(@PathVariable String id) {
        if (!asignacionService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        asignacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 
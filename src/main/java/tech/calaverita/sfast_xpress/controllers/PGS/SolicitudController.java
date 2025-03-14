package tech.calaverita.sfast_xpress.controllers.PGS;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.solicitud.SolicitudDTO;
import tech.calaverita.sfast_xpress.mappers.SolicitudMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.SolicitudModel;
import tech.calaverita.sfast_xpress.services.SolicitudService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/solicitudes")
public final class SolicitudController {
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @PostMapping("/create-one")
    public ResponseEntity<String> postSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        String response = "Solicitud creada con exito";
        HttpStatus httpStatus = HttpStatus.CREATED;

        if (this.solicitudService.existsById(solicitudDTO.getSolicitudId())) {
            response = "El ID de la solicitud ya existe";
            httpStatus = HttpStatus.CONFLICT;
        } else {
            SolicitudModel solicitudModelResponse = this.solicitudService.save(new SolicitudMapper()
                    .mapIn(solicitudDTO));
            if (solicitudModelResponse == null) {
                response = "Verifica que todos los campos sean correctos";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/{filtro}")
    public ResponseEntity<ArrayList<SolicitudDTO>> getSolicitudByGerenciaOrAgencia(@PathVariable String filtro) {
        ArrayList<SolicitudDTO> solicitudDTOS = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        if (filtro.startsWith("GER")) {
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByGerencia(filtro));
        } else if (filtro.startsWith("AG")) {
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByAgencia(filtro));
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        if (httpStatus.equals(HttpStatus.OK) && solicitudDTOS.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(solicitudDTOS, httpStatus);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<SolicitudDTO> getSolicitudById(@PathVariable String id) {
        HttpStatus httpStatus = HttpStatus.OK;

        SolicitudDTO solicitudDTO = new SolicitudMapper().mapOut(this.solicitudService.findById(id));

        if (solicitudDTO == null) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(solicitudDTO, httpStatus);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> putStatusSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        String response = "Solicitud actualizada con exito";
        HttpStatus httpStatus = HttpStatus.CREATED;

        SolicitudModel solicitudModelResponse = null;

        if(solicitudDTO.getSolicitudId() != null){
            solicitudModelResponse = this.solicitudService.save(new SolicitudMapper().mapIn(solicitudDTO));
        }

        if (solicitudModelResponse == null) {
            response = "Verifica que todos los campos sean correctos" + "\n" + solicitudDTO.toString();
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @GetMapping("/{agencia}/{anio}/{semana}")
    public ResponseEntity<Object> getSolicitudesByAgenciaAnioY3Semanas(
        @PathVariable String agencia,
        @PathVariable Integer anio,
        @PathVariable Integer semana,
        @RequestParam String status ) {
            
        ArrayList<SolicitudDTO> solicitudDTOS = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;
        Integer semanaAnterior = semana - 2;
        try {
            if (semana < 3) {
                semanaAnterior = 1;
            }
            if (status == null) {
                status = "";
            }
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByAgenciaAnioYEntre3Semanas(agencia, anio, semana, semanaAnterior, status));
            
    
            if (httpStatus.equals(HttpStatus.OK) && solicitudDTOS.isEmpty()) {
                httpStatus = HttpStatus.NO_CONTENT;
            }
    
            return new ResponseEntity<>(solicitudDTOS, httpStatus);


        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", httpStatus,
                    "error", "Internal Server Error",
                    "message", e.getMessage()));
        }

        
    }

    @GetMapping("/status/{agencia}/{anio}/{semana}")
    public ResponseEntity<Object> getSolicitudesByAgenciaAnioSemanaYStatus(
        @PathVariable String agencia,
        @PathVariable Integer anio,
        @PathVariable Integer semana,
        @RequestParam String status ) {
            
        ArrayList<SolicitudDTO> solicitudDTOS = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if (status == null) {
                status = "";
            }
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByAgenciaAnioSemanasYStatus(agencia, anio, semana, status));
            
    
            if (httpStatus.equals(HttpStatus.OK) && solicitudDTOS.isEmpty()) {
                httpStatus = HttpStatus.NO_CONTENT;
            }
    
            return new ResponseEntity<>(solicitudDTOS, httpStatus);


        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", httpStatus,
                    "error", "Internal Server Error",
                    "message", e.getMessage()));
        }

        
    }
    
    
}

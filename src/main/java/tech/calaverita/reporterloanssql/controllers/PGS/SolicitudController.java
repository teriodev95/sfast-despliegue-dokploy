package tech.calaverita.reporterloanssql.controllers.PGS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.dto.solicitud.SolicitudDTO;
import tech.calaverita.reporterloanssql.mappers.SolicitudMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;
import tech.calaverita.reporterloanssql.services.SolicitudService;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/solicitudes")
public class SolicitudController {
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
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

    @GetMapping("/{gerenciaOrAgencia}")
    public ResponseEntity<ArrayList<SolicitudDTO>> getSolicitudByGerenciaOrAgencia(
            @PathVariable("gerenciaOrAgencia") String gerenciaOrAgencia) {
        ArrayList<SolicitudDTO> solicitudDTOS = new ArrayList<>();
        HttpStatus httpStatus = HttpStatus.OK;

        if (gerenciaOrAgencia.startsWith("GER")) {
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByGerencia(gerenciaOrAgencia));
        } else if (gerenciaOrAgencia.startsWith("AG")) {
            solicitudDTOS = new SolicitudMapper().mapOuts(this.solicitudService.findByAgencia(gerenciaOrAgencia));
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        if (httpStatus.equals(HttpStatus.OK) && solicitudDTOS.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(solicitudDTOS, httpStatus);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> putStatusSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        String response = "Solicitud actualizada con exito";
        HttpStatus httpStatus = HttpStatus.CREATED;

        SolicitudModel solicitudModelResponse = this.solicitudService.save(new SolicitudMapper().mapIn(solicitudDTO));

        if (solicitudModelResponse == null) {
            response = "Verifica que todos los campos sean correctos";
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}

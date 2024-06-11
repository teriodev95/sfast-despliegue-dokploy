package tech.calaverita.sfast_xpress.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.DTOs.VisitaDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.VisitaModel;
import tech.calaverita.sfast_xpress.services.VisitaService;

@Component
public final class VisitaUtil {
    private static VisitaService visitaService;

    private VisitaUtil(VisitaService visitaService) {
        VisitaUtil.visitaService = visitaService;
    }

    public static boolean blnHasVisita(
            String visitaId_I
    ) {
        VisitaModel visitaModel = VisitaUtil.visitaService.findById(visitaId_I);

        return visitaModel != null;
    }

    public static ResponseEntity<String> restrCheckVisitaModelByVisitaId(
            String visitaId_I
    ) {
        ResponseEntity<String> responseEntity;

        if (blnHasVisita(visitaId_I)) {
            responseEntity = new ResponseEntity<>("La visita ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("La visita no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    public static ResponseEntity<String> restrCheckVisit(VisitaDTO visitaDTO) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        if (((visitaDTO.getVisitaId() == null) || (visitaDTO.getVisitaId().isEmpty()))) {
            responseEntity = new ResponseEntity<>("Debe ingresar un visitaId válido", HttpStatus.BAD_REQUEST);
        } else if (VisitaUtil.blnHasVisita(visitaDTO.getVisitaId())) {
            responseEntity = VisitaUtil.restrCheckVisitaModelByVisitaId(visitaDTO.getVisitaId());
        }

        if (((visitaDTO.getPrestamoId() == null) || (visitaDTO.getPrestamoId().isEmpty()))) {
            responseEntity = new ResponseEntity<>("Debe ingresar un prestamoId válido", HttpStatus.BAD_REQUEST);
        } else if (!PrestamoUtil.blnIsPrestamo(visitaDTO.getPrestamoId())) {
            responseEntity = PrestamoUtil.restrCheckPrestamoByPrestamoId(visitaDTO.getPrestamoId());
        }

        if (((visitaDTO.getSemana() == null) || (visitaDTO.getSemana() == 0))) {
            responseEntity = new ResponseEntity<>("Debe ingresar la semana", HttpStatus.BAD_REQUEST);
        }

        if ((visitaDTO.getAnio() == null) || (visitaDTO.getAnio() == 0)) {
            responseEntity = new ResponseEntity<>("Debe ingresar el año", HttpStatus.BAD_REQUEST);
        }

        if (((visitaDTO.getFecha() == null) || (visitaDTO.getFecha().isEmpty()))) {
            responseEntity = new ResponseEntity<>("Debe ingresar una fecha", HttpStatus.BAD_REQUEST);
        }

        if (((visitaDTO.getCliente() == null) || (visitaDTO.getCliente().isEmpty()))) {
            responseEntity = new ResponseEntity<>("Debe ingresar el cliente", HttpStatus.BAD_REQUEST);
        }

        if (((visitaDTO.getAgente() == null) || (visitaDTO.getAgente().isEmpty()))) {
            responseEntity = new ResponseEntity<>("Debe ingresar el agente", HttpStatus.BAD_REQUEST);
        }

        if (visitaDTO.getLat() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lat", HttpStatus.BAD_REQUEST);
        }

        if (visitaDTO.getLng() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lng", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}

package tech.calaverita.reporterloanssql.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.services.VisitaService;

@Component
public class VisitaUtil {
    public static boolean isVisita(String visitaId) {
        VisitaModel visitaModel = VisitaService.getVisitaModelByVisitaId(visitaId);

        return visitaModel != null;
    }

    public static ResponseEntity<String> checkVisitaModelByVisitaId(String visitaId) {
        ResponseEntity<String> responseEntity;

        if (isVisita(visitaId)) {
            responseEntity = new ResponseEntity<>("La visita ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("La visita no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    public static ResponseEntity<String> checkVisit(VisitaModel visitaModel) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        if (visitaModel.getVisitaId() == null || visitaModel.getVisitaId().equals("")) {
            responseEntity = new ResponseEntity<>("Debe ingresar un visitaId válido", HttpStatus.BAD_REQUEST);
        } else if (VisitaUtil.isVisita(visitaModel.getVisitaId())) {
            responseEntity = VisitaUtil.checkVisitaModelByVisitaId(visitaModel.getVisitaId());
        }

        if (visitaModel.getPrestamoId() == null || visitaModel.getPrestamoId().equals("")) {
            responseEntity = new ResponseEntity<>("Debe ingresar un prestamoId válido", HttpStatus.BAD_REQUEST);
        } else if (!PrestamoUtil.isPrestamo(visitaModel.getPrestamoId())) {
            responseEntity = PrestamoUtil.checkPrestamoByPrestamoId(visitaModel.getPrestamoId());
        }

        if (visitaModel.getSemana() == null || visitaModel.getSemana() == 0) {
            responseEntity = new ResponseEntity<>("Debe ingresar la semana", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getAnio() == null || visitaModel.getAnio() == 0) {
            responseEntity = new ResponseEntity<>("Debe ingresar el año", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getFecha() == null || visitaModel.getFecha().equals("")) {
            responseEntity = new ResponseEntity<>("Debe ingresar una fecha", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getCliente() == null || visitaModel.getCliente().equals("")) {
            responseEntity = new ResponseEntity<>("Debe ingresar el cliente", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getAgente() == null || visitaModel.getAgente().equals("")) {
            responseEntity = new ResponseEntity<>("Debe ingresar el agente", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getLat() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lat", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel.getLng() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lng", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}

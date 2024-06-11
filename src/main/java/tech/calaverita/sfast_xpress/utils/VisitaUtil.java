package tech.calaverita.sfast_xpress.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

    public static ResponseEntity<String> restrCheckVisit(VisitaModel visitaModel_I) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        if (((visitaModel_I.getVisitaId() == null) || (visitaModel_I.getVisitaId().equals("")))) {
            responseEntity = new ResponseEntity<>("Debe ingresar un visitaId válido", HttpStatus.BAD_REQUEST);
        } else if (VisitaUtil.blnHasVisita(visitaModel_I.getVisitaId())) {
            responseEntity = VisitaUtil.restrCheckVisitaModelByVisitaId(visitaModel_I.getVisitaId());
        }

        if (((visitaModel_I.getPrestamoId() == null) || (visitaModel_I.getPrestamoId().equals("")))) {
            responseEntity = new ResponseEntity<>("Debe ingresar un prestamoId válido", HttpStatus.BAD_REQUEST);
        } else if (!PrestamoUtil.blnIsPrestamo(visitaModel_I.getPrestamoId())) {
            responseEntity = PrestamoUtil.restrCheckPrestamoByPrestamoId(visitaModel_I.getPrestamoId());
        }

        if (((visitaModel_I.getSemana() == null) || (visitaModel_I.getSemana() == 0))) {
            responseEntity = new ResponseEntity<>("Debe ingresar la semana", HttpStatus.BAD_REQUEST);
        }

        if ((visitaModel_I.getAnio() == null) || (visitaModel_I.getAnio() == 0)) {
            responseEntity = new ResponseEntity<>("Debe ingresar el año", HttpStatus.BAD_REQUEST);
        }

        if (((visitaModel_I.getFecha() == null) || (visitaModel_I.getFecha().equals("")))) {
            responseEntity = new ResponseEntity<>("Debe ingresar una fecha", HttpStatus.BAD_REQUEST);
        }

        if (((visitaModel_I.getCliente() == null) || (visitaModel_I.getCliente().equals("")))) {
            responseEntity = new ResponseEntity<>("Debe ingresar el cliente", HttpStatus.BAD_REQUEST);
        }

        if (((visitaModel_I.getAgente() == null) || (visitaModel_I.getAgente().equals("")))) {
            responseEntity = new ResponseEntity<>("Debe ingresar el agente", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel_I.getLat() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lat", HttpStatus.BAD_REQUEST);
        }

        if (visitaModel_I.getLng() == null) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lng", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}

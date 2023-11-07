package tech.calaverita.reporterloanssql.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.entities.VisitaEntity;
import tech.calaverita.reporterloanssql.services.VisitaService;

@Component
public final class VisitaUtil {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private static VisitaService visServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private VisitaUtil(
            VisitaService visServ_S
    ) {
        VisitaUtil.visServ = visServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public static boolean blnHasVisita(
            String visitaId_I
    ) {
        VisitaEntity visitaEntity = VisitaUtil.visServ.visModFindByVisitaId(visitaId_I);

        return visitaEntity != null;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static ResponseEntity<String> restrCheckVisitaModelByVisitaId(
            String visitaId_I
    ) {
        ResponseEntity<String> responseEntity;

        if (
                blnHasVisita(visitaId_I)
        ) {
            responseEntity = new ResponseEntity<>("La visita ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("La visita no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static ResponseEntity<String> restrCheckVisit(
            VisitaEntity visitaEntity_I
    ) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        if (
                ((visitaEntity_I.getVisitaId() == null) || (visitaEntity_I.getVisitaId().equals("")))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar un visitaId válido", HttpStatus.BAD_REQUEST);
        } //
        else if (
                VisitaUtil.blnHasVisita(visitaEntity_I.getVisitaId())
        ) {
            responseEntity = VisitaUtil.restrCheckVisitaModelByVisitaId(visitaEntity_I.getVisitaId());
        }

        if (
                ((visitaEntity_I.getPrestamoId() == null) || (visitaEntity_I.getPrestamoId().equals("")))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar un prestamoId válido", HttpStatus.BAD_REQUEST);
        } //
        else if (
                !PrestamoUtil.blnIsPrestamo(visitaEntity_I.getPrestamoId())
        ) {
            responseEntity = PrestamoUtil.restrCheckPrestamoByPrestamoId(visitaEntity_I.getPrestamoId());
        }

        if (
                ((visitaEntity_I.getSemana() == null) || (visitaEntity_I.getSemana() == 0))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar la semana", HttpStatus.BAD_REQUEST);
        }

        if (
                (visitaEntity_I.getAnio() == null) || (visitaEntity_I.getAnio() == 0)
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar el año", HttpStatus.BAD_REQUEST);
        }

        if (
                ((visitaEntity_I.getFecha() == null) || (visitaEntity_I.getFecha().equals("")))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar una fecha", HttpStatus.BAD_REQUEST);
        }

        if (
                ((visitaEntity_I.getCliente() == null) || (visitaEntity_I.getCliente().equals("")))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar el cliente", HttpStatus.BAD_REQUEST);
        }

        if (
                ((visitaEntity_I.getAgente() == null) || (visitaEntity_I.getAgente().equals("")))
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar el agente", HttpStatus.BAD_REQUEST);
        }

        if (
                visitaEntity_I.getLat() == null
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lat", HttpStatus.BAD_REQUEST);
        }

        if (
                visitaEntity_I.getLng() == null
        ) {
            responseEntity = new ResponseEntity<>("Debe ingresar la lng", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}

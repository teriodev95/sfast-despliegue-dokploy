package tech.calaverita.reporterloanssql.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.services.PrestamoService;

@Component
public class PrestamoUtil {
    private static PrestamoService prestamoService;

    @Autowired
    private PrestamoUtil(PrestamoService prestamoService) {
        PrestamoUtil.prestamoService = prestamoService;
    }

    public static boolean isPrestamo(String prestamoId) {
        PrestamoModel prestamoModel = prestamoService.getPrestamoModelByPrestamoId(prestamoId);

        return prestamoModel != null;
    }

    public static ResponseEntity<String> checkPrestamoByPrestamoId(String prestamoId) {
        ResponseEntity<String> responseEntity;

        if (isPrestamo(prestamoId)) {
            responseEntity = new ResponseEntity<>("El prestamo ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("El prestamo no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

}

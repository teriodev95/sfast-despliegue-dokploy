package tech.calaverita.sfast_xpress.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;

@Component
public class PrestamoUtil {
    private static PrestamoService prestamoService;

    private PrestamoUtil(PrestamoService prestamoService) {
        PrestamoUtil.prestamoService = prestamoService;
    }

    public static boolean blnIsPrestamo(String prestamoId_I) {
        PrestamoModel prestamoModel = prestamoService.prestModFindByPrestamoId(prestamoId_I);

        return prestamoModel != null;
    }

    public static ResponseEntity<String> restrCheckPrestamoByPrestamoId(String prestamoId_I) {
        ResponseEntity<String> responseEntity;

        if (blnIsPrestamo(prestamoId_I)) {
            responseEntity = new ResponseEntity<>("El prestamo ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("El prestamo no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    public static PrestamoModel prestamoModelAsignarPorcentajeCobrado(PrestamoModel prestamoModel_M) {
        Double dblTotalAPagar = prestamoModel_M.getTotalAPagar();
        Double dblCobrado = prestamoModel_M.getCobrado();

        prestamoModel_M.setPorcentajeCobrado(Math.round(dblCobrado / dblTotalAPagar * 100.0) / 100.0 * 100);

        return prestamoModel_M;
    }
}

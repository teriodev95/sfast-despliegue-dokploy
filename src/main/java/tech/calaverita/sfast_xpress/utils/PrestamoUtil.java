package tech.calaverita.sfast_xpress.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;

@Component
public class PrestamoUtil {
    private static PrestamoViewService prestamoViewService;

    private PrestamoUtil(PrestamoViewService prestamoViewService) {
        PrestamoUtil.prestamoViewService = prestamoViewService;
    }

    public static boolean blnIsPrestamo(String prestamoId_I) {
        PrestamoViewModel prestamoViewModel = prestamoViewService.findById(prestamoId_I);

        return prestamoViewModel != null;
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

    public static PrestamoViewModel prestamoModelAsignarPorcentajeCobrado(PrestamoViewModel prestamoModel_M) {
        Double dblTotalAPagar = prestamoModel_M.getTotalAPagar();
        Double dblCobrado = prestamoModel_M.getCobrado();

        prestamoModel_M.setPorcentajeCobrado(Math.round(dblCobrado / dblTotalAPagar * 100.0) / 100.0 * 100);

        return prestamoModel_M;
    }
}

package tech.calaverita.reporterloanssql.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;

@Component
public class PrestamoUtil {
    private static PrestamoService prestamoService;

    @Autowired
    private PrestamoUtil(
            PrestamoService prestamoService_S
    ) {
        PrestamoUtil.prestamoService = prestamoService_S;
    }

    public static boolean blnIsPrestamo(
            String prestamoId_I
    ) {
        PrestamoEntity prestamoEntity = prestamoService.prestModFindByPrestamoId(prestamoId_I);

        return prestamoEntity != null;
    }

    public static ResponseEntity<String> restrCheckPrestamoByPrestamoId(
            String prestamoId_I
    ) {
        ResponseEntity<String> responseEntity;

        if (
                blnIsPrestamo(prestamoId_I)
        ) {
            responseEntity = new ResponseEntity<>("El prestamo ya existe", HttpStatus.CONFLICT);
        } else {
            responseEntity = new ResponseEntity<>("El prestamo no existe", HttpStatus.CONFLICT);
        }

        return responseEntity;
    }

    public static PrestamoEntity prestamoModelAsignarPorcentajeCobrado(
            PrestamoEntity prestamoEntity_M
    ) {
        Double dblTotalAPagar = prestamoEntity_M.getTotalAPagar();
        Double dblCobrado = prestamoEntity_M.getCobrado();

        prestamoEntity_M.setPorcentajeCobrado(Math.round(dblCobrado / dblTotalAPagar * 100.0) / 100.0 * 100);

        return prestamoEntity_M;
    }
}

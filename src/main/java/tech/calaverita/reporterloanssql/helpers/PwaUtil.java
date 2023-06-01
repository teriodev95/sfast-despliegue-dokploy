package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PrestamoCobranzaPwa;
import tech.calaverita.reporterloanssql.services.PrestamoService;
import tech.calaverita.reporterloanssql.threads.CobranzaPwaThread;

import java.util.ArrayList;

public class PwaUtil {
    public static ArrayList<PrestamoCobranzaPwa> getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(String agencia, int anio, int semana) {
        ArrayList<PrestamoModel> prestamoModels = PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToDashboard(agencia, anio, semana);
        ArrayList<PrestamoCobranzaPwa> prestamoCobranzaPwas = new ArrayList<>();

        Thread[] threads = new Thread[prestamoModels.size()];
        int indice = 0;

        for (PrestamoModel prestamoModel : prestamoModels) {
            PrestamoCobranzaPwa prestamoCobranzaPwa = new PrestamoCobranzaPwa();

            prestamoCobranzaPwas.add(prestamoCobranzaPwa);

            threads[indice] = new Thread(new CobranzaPwaThread(prestamoModel, prestamoCobranzaPwa, anio, semana));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return prestamoCobranzaPwas;
    }
}

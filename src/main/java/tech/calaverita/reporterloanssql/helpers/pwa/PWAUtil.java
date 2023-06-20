package tech.calaverita.reporterloanssql.helpers.pwa;

import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PagoVistaModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.models.VisitaModel;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.PrestamoService;
import tech.calaverita.reporterloanssql.services.VisitaService;
import tech.calaverita.reporterloanssql.threads.pwa.CobranzaPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoHistoricoPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoPWAThread;

import java.util.ArrayList;

public class PWAUtil {
    public static ArrayList<PrestamoCobranzaPWA> getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(String agencia, int anio, int semana) {
        ArrayList<PrestamoModel> prestamoModels = PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranza(agencia, anio, semana);
        ArrayList<PrestamoCobranzaPWA> prestamoCobranzaPWAs = new ArrayList<>();

        Thread[] threads = new Thread[prestamoModels.size()];
        int indice = 0;

        for (PrestamoModel prestamoModel : prestamoModels) {
            PrestamoCobranzaPWA prestamoCobranzaPwa = new PrestamoCobranzaPWA();

            prestamoCobranzaPWAs.add(prestamoCobranzaPwa);

            threads[indice] = new Thread(new CobranzaPWAThread(prestamoModel, prestamoCobranzaPwa, anio, semana));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return prestamoCobranzaPWAs;
    }

    public static ArrayList<PagoPWA> getPagoPWAsFromPagoModels(ArrayList<PagoModel> pagoModels) {
        ArrayList<PagoPWA> pagoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagoModels.size()];
        int indice = 0;

        for (PagoModel pagoModel : pagoModels) {
            PagoPWA pagoPWA = new PagoPWA();

            pagoPWAs.add(pagoPWA);

            threads[indice] = new Thread(new PagoPWAThread(pagoModel, pagoPWA));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return pagoPWAs;
    }

    public static ArrayList<PagoHistoricoPWA> getPagoHistoricoPWAsFromPagoVistaModels(String agencia, int anio, int semana) {
        ArrayList<PagoVistaModel> pagoVistaModels = PagoService.getPagoVistaModelsByAgenciaAnioAndSemana(agencia, anio, semana);
        ArrayList<PagoHistoricoPWA> pagoHistoricoPWAs = new ArrayList<>();
        ArrayList<PagoModel> pagoModels = PagoService.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);
        ArrayList<VisitaModel> visitaModels = VisitaService.findVisitaModelsByAgenciaAnioAndSemana(agencia, anio, semana);

        Thread[] threads = new Thread[pagoVistaModels.size()];
        int indice = 0;

        for (PagoVistaModel pagoVistaModel : pagoVistaModels) {
            PagoHistoricoPWA pagoHistoricoPWA = new PagoHistoricoPWA();

            pagoHistoricoPWAs.add(pagoHistoricoPWA);

            threads[indice] = new Thread(new PagoHistoricoPWAThread(pagoVistaModel, pagoHistoricoPWA, pagoModels, visitaModels));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return pagoHistoricoPWAs;
    }
}

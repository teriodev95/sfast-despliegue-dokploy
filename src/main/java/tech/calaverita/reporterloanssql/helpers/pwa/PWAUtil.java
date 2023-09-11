package tech.calaverita.reporterloanssql.helpers.pwa;

import tech.calaverita.reporterloanssql.models.*;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.PrestamoService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.threads.pwa.CobranzaPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoHistoricoPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoPWAThread;

import java.util.ArrayList;
import java.util.HashMap;

public class PWAUtil {
    public static ArrayList<PrestamoCobranzaPWA> getPrestamoCobranzaPwasFromPrestamoModelsAndPagoModels(String agencia, int anio, int semana) {
        ArrayList<PrestamoModel> prestamoModels = PrestamoService.getPrestamoModelsByAgenciaAnioAndSemanaToCobranzaPGS(agencia, anio, semana);
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

    public static ArrayList<PagoHistoricoPWA> getPagoHistoricoPWAsFromPagoVistaModelsByPrestamoId(String prestamoId) {
        ArrayList<PagoAgrupadoModel> pagoAgrupadoModels = PagoService.findPagoVistaModelsByPrestamoId(prestamoId);
        ArrayList<PagoHistoricoPWA> pagoHistoricoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagoAgrupadoModels.size()];
        int indice = 0;

        for (PagoAgrupadoModel pagoAgrupadoModel : pagoAgrupadoModels) {
            PagoHistoricoPWA pagoHistoricoPWA = new PagoHistoricoPWA();

            pagoHistoricoPWAs.add(pagoHistoricoPWA);

            threads[indice] = new Thread(new PagoHistoricoPWAThread(pagoAgrupadoModel, pagoHistoricoPWA));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return pagoHistoricoPWAs;
    }

    public static ArrayList<HashMap<String, Object>> getAsignacionModelsPWA(ArrayList<AsignacionModel> asignacionModels) {
        ArrayList<HashMap<String, Object>> asignacionModelsPWA = new ArrayList<>();

        for (AsignacionModel asignacionModel : asignacionModels) {
            UsuarioModel usuarioModel = UsuarioService.findOneByUsuarioId(asignacionModel.getQuienRecibioUsuarioId());

            HashMap<String, Object> recibioHashMap = new HashMap<>();
            recibioHashMap.put("usuario", usuarioModel.getUsuario());
            recibioHashMap.put("tipo", usuarioModel.getTipo());

            HashMap<String, Object> responseHashMap = new HashMap<>();
            responseHashMap.put("asignacion", asignacionModel);
            responseHashMap.put("recibio", recibioHashMap);

            asignacionModelsPWA.add(responseHashMap);
        }

        return asignacionModelsPWA;
    }
}

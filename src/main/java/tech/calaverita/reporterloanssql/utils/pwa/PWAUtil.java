package tech.calaverita.reporterloanssql.utils.pwa;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.models.mariaDB.AsignacionModel;
import tech.calaverita.reporterloanssql.models.mariaDB.CalendarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.models.view.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.pojos.PWA.PagoPWA;
import tech.calaverita.reporterloanssql.pojos.PWA.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.*;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;
import tech.calaverita.reporterloanssql.threads.pwa.CobranzaPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoHistoricoPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoPWAThread;
import tech.calaverita.reporterloanssql.utils.CobranzaUtil;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public final class PWAUtil {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private static PagoService pagServ;
    private static PrestamoService prestServ;
    private static UsuarioService usuarServ;
    private static CierreSemanalService cierreSemanalService;
    private static BalanceAgenciaService balanceAgenciaService;

    private static EgresosAgenteService egresosAgenteService;

    private static EgresosGerenteService egresosGerenteService;

    private static IngresosAgenteService ingresosAgenteService;


    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PWAUtil(
            PagoService pagServ,
            PrestamoService prestServ,
            UsuarioService usuarServ,
            CierreSemanalService cierreSemanalService,
            BalanceAgenciaService balanceAgenciaService,
            EgresosAgenteService egresosAgenteService,
            EgresosGerenteService egresosGerenteService,
            IngresosAgenteService ingresosAgenteService
    ) {
        PWAUtil.pagServ = pagServ;
        PWAUtil.prestServ = prestServ;
        PWAUtil.usuarServ = usuarServ;
        PWAUtil.cierreSemanalService = cierreSemanalService;
        PWAUtil.balanceAgenciaService = balanceAgenciaService;
        PWAUtil.egresosAgenteService = egresosAgenteService;
        PWAUtil.egresosGerenteService = egresosGerenteService;
        PWAUtil.ingresosAgenteService = ingresosAgenteService;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public static ArrayList<PrestamoCobranzaPWA> darrprestamoCobranzaPwaFromPrestamoModelsAndPagoModels(
            String agencia, int anio, int semana
    ) {
        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        CobranzaUtil.funSemanaAnterior(calendarioModel);

        ArrayList<PrestamoModel> prestEntPrestamoEntities = PWAUtil.prestServ
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(
                        agencia, calendarioModel.getAnio(), calendarioModel.getSemana());
        ArrayList<PrestamoCobranzaPWA> prestamoCobranzaPWAs = new ArrayList<>();

        Thread[] threads = new Thread[prestEntPrestamoEntities.size()];
        int indice = 0;

        for (PrestamoModel prestamoModel : prestEntPrestamoEntities) {
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static ArrayList<PagoPWA> darrpagoPwaFromPagoModels(
            ArrayList<PagoModel> pagEntPagoEntities
    ) {
        ArrayList<PagoPWA> pagoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagEntPagoEntities.size()];
        int indice = 0;

        for (PagoModel pagoModel : pagEntPagoEntities) {
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static ArrayList<PagoHistoricoPWA> darrpagoHistoricoPwaFromPagoVistaModelsByPrestamoId(
            String prestamoId
    ) {
        ArrayList<PagoAgrupadoModel> pagAgrEntPagoAgrupadoEntities = PWAUtil.pagServ
                .darrpagAgrModFindByPrestamoId(prestamoId);
        ArrayList<PagoHistoricoPWA> pagoHistoricoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagAgrEntPagoAgrupadoEntities.size()];
        int indice = 0;

        for (PagoAgrupadoModel pagoAgrupadoModel : pagAgrEntPagoAgrupadoEntities) {
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static ArrayList<HashMap<String, Object>> darrdicasignacionModelPwa(
            ArrayList<AsignacionModel> darrasignacionModelAsigModAsignEnt
    ) {
        ArrayList<HashMap<String, Object>> darrHshMpAsgMdlPwa = new ArrayList<>();

        for (AsignacionModel asignacionModel : darrasignacionModelAsigModAsignEnt) {
            UsuarioModel usuarioModel = PWAUtil.usuarServ.findById(asignacionModel
                    .getQuienRecibioUsuarioId());

            HashMap<String, Object> recibioHashMap = new HashMap<>();
            recibioHashMap.put("usuario", usuarioModel.getUsuario());
            recibioHashMap.put("tipo", usuarioModel.getTipo());

            HashMap<String, Object> responseHashMap = new HashMap<>();
            responseHashMap.put("asignacion", asignacionModel);
            responseHashMap.put("recibio", recibioHashMap);

            darrHshMpAsgMdlPwa.add(responseHashMap);
        }

        return darrHshMpAsgMdlPwa;
    }
}

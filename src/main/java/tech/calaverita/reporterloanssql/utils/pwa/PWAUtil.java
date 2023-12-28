package tech.calaverita.reporterloanssql.utils.pwa;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
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
        CalendarioEntity calendarioEntity = new CalendarioEntity();
        calendarioEntity.setAnio(anio);
        calendarioEntity.setSemana(semana);
        CobranzaUtil.funSemanaAnterior(calendarioEntity);

        ArrayList<PrestamoEntity> prestEntPrestamoEntities = PWAUtil.prestServ
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(
                        agencia, calendarioEntity.getAnio(), calendarioEntity.getSemana());
        ArrayList<PrestamoCobranzaPWA> prestamoCobranzaPWAs = new ArrayList<>();

        Thread[] threads = new Thread[prestEntPrestamoEntities.size()];
        int indice = 0;

        for (PrestamoEntity prestamoEntity : prestEntPrestamoEntities) {
            PrestamoCobranzaPWA prestamoCobranzaPwa = new PrestamoCobranzaPWA();

            prestamoCobranzaPWAs.add(prestamoCobranzaPwa);

            threads[indice] = new Thread(new CobranzaPWAThread(prestamoEntity, prestamoCobranzaPwa, anio, semana));
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
            ArrayList<PagoEntity> pagEntPagoEntities
    ) {
        ArrayList<PagoPWA> pagoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagEntPagoEntities.size()];
        int indice = 0;

        for (PagoEntity pagoEntity : pagEntPagoEntities) {
            PagoPWA pagoPWA = new PagoPWA();

            pagoPWAs.add(pagoPWA);

            threads[indice] = new Thread(new PagoPWAThread(pagoEntity, pagoPWA));
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
        ArrayList<PagoAgrupadoEntity> pagAgrEntPagoAgrupadoEntities = PWAUtil.pagServ
                .darrpagAgrModFindByPrestamoId(prestamoId);
        ArrayList<PagoHistoricoPWA> pagoHistoricoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagAgrEntPagoAgrupadoEntities.size()];
        int indice = 0;

        for (PagoAgrupadoEntity pagoAgrupadoEntity : pagAgrEntPagoAgrupadoEntities) {
            PagoHistoricoPWA pagoHistoricoPWA = new PagoHistoricoPWA();

            pagoHistoricoPWAs.add(pagoHistoricoPWA);

            threads[indice] = new Thread(new PagoHistoricoPWAThread(pagoAgrupadoEntity, pagoHistoricoPWA));
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
            ArrayList<AsignacionEntity> darrasignacionEntityAsigModAsignEnt
    ) {
        ArrayList<HashMap<String, Object>> darrHshMpAsgMdlPwa = new ArrayList<>();

        for (AsignacionEntity asignacionEntity : darrasignacionEntityAsigModAsignEnt) {
            UsuarioEntity usuarioEntity = PWAUtil.usuarServ.usuarModFindByUsuarioId(asignacionEntity
                    .getQuienRecibioUsuarioId());

            HashMap<String, Object> recibioHashMap = new HashMap<>();
            recibioHashMap.put("usuario", usuarioEntity.getUsuario());
            recibioHashMap.put("tipo", usuarioEntity.getTipo());

            HashMap<String, Object> responseHashMap = new HashMap<>();
            responseHashMap.put("asignacion", asignacionEntity);
            responseHashMap.put("recibio", recibioHashMap);

            darrHshMpAsgMdlPwa.add(responseHashMap);
        }

        return darrHshMpAsgMdlPwa;
    }
}

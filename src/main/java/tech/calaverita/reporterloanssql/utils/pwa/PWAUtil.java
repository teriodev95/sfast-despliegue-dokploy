package tech.calaverita.reporterloanssql.utils.pwa;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.*;
import tech.calaverita.reporterloanssql.persistence.entities.AsignacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoHistoricoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PagoPWA;
import tech.calaverita.reporterloanssql.pojos.pwa.PrestamoCobranzaPWA;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;
import tech.calaverita.reporterloanssql.threads.pwa.CobranzaPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoHistoricoPWAThread;
import tech.calaverita.reporterloanssql.threads.pwa.PagoPWAThread;
import tech.calaverita.reporterloanssql.utils.BalanceAgenciaUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public final class PWAUtil {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private static PagoService pagServ;
    private static PrestamoService prestServ;
    private static UsuarioService usuarServ;
    private static CierreSemanalService cierreSemanalService;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PWAUtil(
            PagoService pagServ,
            PrestamoService prestServ,
            UsuarioService usuarServ,
            CierreSemanalService cierreSemanalService
    ) {
        PWAUtil.pagServ = pagServ;
        PWAUtil.prestServ = prestServ;
        PWAUtil.usuarServ = usuarServ;
        PWAUtil.cierreSemanalService = cierreSemanalService;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public static ArrayList<PrestamoCobranzaPWA> darrprestamoCobranzaPwaFromPrestamoModelsAndPagoModels(
            String agencia, int anio, int semana
    ) {
        ArrayList<PrestamoEntity> prestEntPrestamoEntities = PWAUtil.prestServ
                .darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(
                        agencia, anio, semana);
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

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static CierreSemanalDTO getCierreSemanalPWA(
            Dashboard dashboard,
            List<UsuarioEntity> darrusuarEnt,
            double asignaciones
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> clientePagoCompleto = PWAUtil.pagServ
                .getClientesPagoCompletoByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());
        CompletableFuture<Double> cobranzaTotal = PWAUtil.pagServ
                .getCobranzaTotalByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());

        // To easy code
        UsuarioEntity agenteUsuarioEntity = darrusuarEnt.get(0);
        UsuarioEntity gerenteUsuarioEntity = darrusuarEnt.get(1);
        String nombreAgente = agenteUsuarioEntity.getNombre() + " " + agenteUsuarioEntity.getApellidoPaterno() + " "
                + agenteUsuarioEntity.getApellidoMaterno();
        String nombreGerente = gerenteUsuarioEntity.getNombre() + " " + gerenteUsuarioEntity.getApellidoPaterno() + " "
                + gerenteUsuarioEntity.getApellidoMaterno();

        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        {
            balanceAgenciaDTO.setZona(dashboard.getGerencia());
            balanceAgenciaDTO.setGerente(nombreGerente);
            balanceAgenciaDTO.setAgencia(dashboard.getAgencia());
            balanceAgenciaDTO.setAgente(nombreAgente);
            balanceAgenciaDTO.setRendimiento(dashboard.getRendimiento());
            balanceAgenciaDTO.setNivel(BalanceAgenciaUtil.getNivelAgente(dashboard.getClientes(),
                    dashboard.getRendimiento() / 100, agenteUsuarioEntity));
            balanceAgenciaDTO.setClientes(dashboard.getClientes());
            balanceAgenciaDTO.setPagosReducidos(dashboard.getPagosReducidos());
            balanceAgenciaDTO.setNoPagos(dashboard.getNoPagos());
            balanceAgenciaDTO.setLiquidaciones(dashboard.getNumeroLiquidaciones());
        }

        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        {
            egresosAgenteDTO.setAsignaciones(asignaciones);

            cobranzaTotal.join();

            egresosAgenteDTO.setEfectivoEntregadoCierre(cobranzaTotal.get() - egresosAgenteDTO.getAsignaciones());
        }

        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        {
            egresosGerenteDTO.setPorcentajeComisionCobranza(BalanceAgenciaUtil
                    .getPorcentajeComisionCobranza(balanceAgenciaDTO.getNivel()));

            clientePagoCompleto.join();

            // To easy code
            int porcentajeBonoMensual = BalanceAgenciaUtil.getPorcentajeBonoMensual(clientePagoCompleto.get(),
                    dashboard.getRendimiento(), agenteUsuarioEntity);

            egresosGerenteDTO.setPorcentajeBonoMensual(porcentajeBonoMensual);

            cobranzaTotal.join();

            egresosGerenteDTO.setPagoComisionCobranza(cobranzaTotal.get() / 100
                    * egresosGerenteDTO.getPorcentajeComisionCobranza());
            egresosGerenteDTO.setBonos(cobranzaTotal.get() / 100 * egresosGerenteDTO.getPorcentajeBonoMensual());
        }

        IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
        {
            ingresosAgenteDTO.setCobranzaPura(dashboard.getTotalCobranzaPura());
            ingresosAgenteDTO.setMontoExcedente(dashboard.getMontoExcedente());
            ingresosAgenteDTO.setLiquidaciones(dashboard.getLiquidaciones());
            ingresosAgenteDTO.setMultas(dashboard.getMultas());
        }

        CierreSemanalDTO cierreSemanalDTO_O = new CierreSemanalDTO();
        {
            cierreSemanalDTO_O.setSemana(dashboard.getSemana());
            cierreSemanalDTO_O.setAnio(dashboard.getAnio());
            cierreSemanalDTO_O.setBalanceAgencia(balanceAgenciaDTO);
            cierreSemanalDTO_O.setEgresosAgente(egresosAgenteDTO);
            cierreSemanalDTO_O.setEgresosGerente(egresosGerenteDTO);
            cierreSemanalDTO_O.setIngresosAgente(ingresosAgenteDTO);
            cierreSemanalDTO_O.setPinAgente(agenteUsuarioEntity.getPin());

            // To easy code
            String id = dashboard.getGerencia() + '-' + dashboard.getAgencia() + '-' + dashboard.getAnio() + '-' + dashboard.getSemana();

            cierreSemanalDTO_O.setIsAgenciaCerrada(PWAUtil.cierreSemanalService.existsById(id));
        }

        return cierreSemanalDTO_O;
    }
}

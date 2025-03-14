package tech.calaverita.sfast_xpress.utils.pwa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import tech.calaverita.sfast_xpress.controllers.UsuarioController;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.PWA.PagoHistoricoPWA;
import tech.calaverita.sfast_xpress.pojos.PWA.PagoPWA;
import tech.calaverita.sfast_xpress.pojos.PWA.PrestamoCobranzaPWA;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.threads.pwa.CobranzaPWAThread;
import tech.calaverita.sfast_xpress.threads.pwa.PagoHistoricoPWAThread;
import tech.calaverita.sfast_xpress.threads.pwa.PagoPWAThread;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Component
public final class PWAUtil {

    private final UsuarioController usuarioController;
    private static PrestamoViewService prestamoViewService;
    private static UsuarioService usuarioService;
    private static PagoService pagoService;
    private static CalendarioService calendarioService;
    private static PagoDynamicService pagoDynamicService;

    public PWAUtil(PrestamoViewService prestamoViewService, UsuarioService usuarioService,
            PagoService pagoService, CalendarioService calendarioService, PagoDynamicService pagoDynamicService,
            UsuarioController usuarioController) {
        PWAUtil.prestamoViewService = prestamoViewService;
        PWAUtil.usuarioService = usuarioService;
        PWAUtil.pagoService = pagoService;
        PWAUtil.calendarioService = calendarioService;
        PWAUtil.pagoDynamicService = pagoDynamicService;
        this.usuarioController = usuarioController;
    }

    public static ArrayList<PrestamoCobranzaPWA> darrprestamoCobranzaPwaFromPrestamoModelsAndPagoModels(String agencia,
            int anio,
            int semana) {
        CalendarioModel calendarioModel = new CalendarioModel();
        calendarioModel.setAnio(anio);
        calendarioModel.setSemana(semana);
        MyUtil.funSemanaAnterior(calendarioModel);

        ArrayList<PrestamoViewModel> prestamoViewModels = PWAUtil.prestamoViewService
                .findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
                        agencia, 0D, anio, semana)
                .join();
        ArrayList<PrestamoCobranzaPWA> prestamoCobranzaPWAs = new ArrayList<>();

        Thread[] threads = new Thread[prestamoViewModels.size()];
        int indice = 0;

        for (PrestamoViewModel prestamoModel : prestamoViewModels) {
            PrestamoCobranzaPWA prestamoCobranzaPwa = new PrestamoCobranzaPWA();

            prestamoCobranzaPWAs.add(prestamoCobranzaPwa);

            threads[indice] = new Thread(new CobranzaPWAThread(prestamoModel,
                    prestamoCobranzaPwa, anio, semana));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return prestamoCobranzaPWAs;
    }

    public static ArrayList<PagoPWA> darrpagoPwaFromPagoModels(ArrayList<PagoModel> pagEntPagoEntities) {
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

    public static ArrayList<PagoHistoricoPWA> darrpagoHistoricoPwaFromPagoVistaModelsByPrestamoId(String prestamoId) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioModel calendarioModel = PWAUtil.calendarioService.findByFechaActual(fechaActual);

        ArrayList<PagoModel> pagAgrEntPagoAgrupadoEntities = PWAUtil.pagoService
                .findByPrestamoIdAnioNotAndSemanaNotOrderByAnioAscSemanaAsc(prestamoId, calendarioModel.getAnio(),
                        calendarioModel.getSemana());

        // To easy code
        PagoDynamicModel pagoDynamicModel = pagoDynamicService.findByPrestamoIdAnioAndSemana(prestamoId,
                calendarioModel.getAnio(), calendarioModel.getSemana());

        if (pagoDynamicModel != null) {
            pagAgrEntPagoAgrupadoEntities.add(new PagoModel(pagoDynamicModel));
        }

        ArrayList<PagoHistoricoPWA> pagoHistoricoPWAs = new ArrayList<>();

        Thread[] threads = new Thread[pagAgrEntPagoAgrupadoEntities.size()];
        int indice = 0;

        for (PagoModel pagoModel : pagAgrEntPagoAgrupadoEntities) {
            PagoHistoricoPWA pagoHistoricoPWA = new PagoHistoricoPWA();

            pagoHistoricoPWAs.add(pagoHistoricoPWA);

            threads[indice] = new Thread(new PagoHistoricoPWAThread(pagoModel, pagoHistoricoPWA));
            threads[indice].start();
            indice++;
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {
            }
        }

        return pagoHistoricoPWAs;
    }

    public static ArrayList<HashMap<String, Object>> darrdicasignacionModelPwa(
            ArrayList<AsignacionModel> darrasignacionModelAsigModAsignEnt) {
        ArrayList<HashMap<String, Object>> darrHshMpAsgMdlPwa = new ArrayList<>();

        for (AsignacionModel asignacionModel : darrasignacionModelAsigModAsignEnt) {
            UsuarioModel usuarioModel = PWAUtil.usuarioService.findById(asignacionModel
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

    public static HashMap<String, Object> asignacionModelPwa(HashMap<String, Object> responseHM) {
        HashMap<String, Object> darrHshMpAsgMdlPwa = new HashMap<>();

        ArrayList<Object> ingresosHM = new ArrayList<>();
        for (AsignacionModel asignacionModel : (ArrayList<AsignacionModel>) responseHM.get("ingresos")) {
            UsuarioModel recibioUsuarioModel = asignacionModel.getRecibioUsuarioModel();
            UsuarioModel entregoUsuarioModel = asignacionModel.getEntregoUsuarioModel();

            HashMap<String, Object> recibioHashMap = new HashMap<>();
            recibioHashMap.put("usuario", recibioUsuarioModel.getUsuario());
            recibioHashMap.put("tipo", recibioUsuarioModel.getTipo());
            recibioHashMap.put("nombre", recibioUsuarioModel.getNombre());

            HashMap<String, Object> entregoHashMap = new HashMap<>();
            entregoHashMap.put("usuario", entregoUsuarioModel.getUsuario());
            entregoHashMap.put("tipo", entregoUsuarioModel.getTipo());
            entregoHashMap.put("nombre", entregoUsuarioModel.getNombre());

            HashMap<String, Object> responseHashMap = new HashMap<>();
            responseHashMap.put("asignacion", asignacionModel);
            responseHashMap.put("recibio", recibioHashMap);
            responseHashMap.put("entrego", entregoHashMap);

            ingresosHM.add(responseHashMap);
        }
        darrHshMpAsgMdlPwa.put("ingresos", ingresosHM);

        ArrayList<Object> egresosHM = new ArrayList<>();
        for (AsignacionModel asignacionModel : (ArrayList<AsignacionModel>) responseHM.get("egresos")) {
            UsuarioModel recibioUsuarioModel = asignacionModel.getRecibioUsuarioModel();
            UsuarioModel entregoUsuarioModel = asignacionModel.getEntregoUsuarioModel();

            HashMap<String, Object> recibioHashMap = new HashMap<>();
            recibioHashMap.put("usuario", recibioUsuarioModel.getUsuario());
            recibioHashMap.put("tipo", recibioUsuarioModel.getTipo());
            recibioHashMap.put("nombre", recibioUsuarioModel.getNombre());

            HashMap<String, Object> entregoHashMap = new HashMap<>();
            entregoHashMap.put("usuario", entregoUsuarioModel.getUsuario());
            entregoHashMap.put("tipo", entregoUsuarioModel.getTipo());
            entregoHashMap.put("nombre", entregoUsuarioModel.getNombre());

            HashMap<String, Object> responseHashMap = new HashMap<>();
            responseHashMap.put("asignacion", asignacionModel);
            responseHashMap.put("recibio", recibioHashMap);
            responseHashMap.put("entrego", entregoHashMap);

            egresosHM.add(responseHashMap);
        }
        darrHshMpAsgMdlPwa.put("egresos", egresosHM);

        return darrHshMpAsgMdlPwa;
    }
}

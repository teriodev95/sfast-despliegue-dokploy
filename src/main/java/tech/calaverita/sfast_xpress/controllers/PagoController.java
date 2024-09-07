package tech.calaverita.sfast_xpress.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VisitaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.ModelValidation;
import tech.calaverita.sfast_xpress.pojos.PagoConLiquidacion;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VisitaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.relation.UsuarioGerenciaService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.PagoUtil;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public final class PagoController {
    private final PagoService pagoService;
    private final PrestamoViewService prestamoViewService;
    private final UsuarioGerenciaService usuarioGerenciaService;
    private final UsuarioService usuarioService;
    private final VisitaService visitaService;
    private final AgenciaService agenciaService;
    private final CalendarioService calendarioService;
    private final PagoDynamicService pagoDynamicService;

    public PagoController(PagoService pagoService, PrestamoViewService prestamoViewService,
            UsuarioGerenciaService usuarioGerenciaService, UsuarioService usuarioService,
            VisitaService visitaService, AgenciaService agenciaService, CalendarioService calendarioService,
            PagoDynamicService pagoDynamicService) {
        this.pagoService = pagoService;
        this.prestamoViewService = prestamoViewService;
        this.usuarioGerenciaService = usuarioGerenciaService;
        this.usuarioService = usuarioService;
        this.visitaService = visitaService;
        this.agenciaService = agenciaService;
        this.calendarioService = calendarioService;
        this.pagoDynamicService = pagoDynamicService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @CrossOrigin
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> getPagoModelsByAgenciaAnioAndSemana(
            @PathVariable String agencia, @PathVariable int anio, @PathVariable int semana) {
        boolean esPrimerPago = false;
        ArrayList<PagoModel> pagoModels = this.pagoService.findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio,
                semana, esPrimerPago);

        HttpStatus httpStatus = HttpStatus.OK;

        if (pagoModels.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(pagoModels, httpStatus);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> restrCreatePagMod(
            // Dentro de este endpoint se puede recibir el pago con la liquidación, si el
            // pago mandado cuenta con
            // liquidacion se hará el proceso correspondiente para guardar dicha liquidacion
            // y ajustar la propiedad
            // cierraCon de pago, si se manda solo el pago solo se realizará el proceso para
            // guardar el pago.

            @RequestBody PagoConLiquidacion pagoConLiquidacion) {
        ModelValidation modVal;
        PrestamoViewModel prestMod = this.prestamoViewService
                .findById(pagoConLiquidacion.getPrestamoId());
        modVal = PagoUtil.modValPagoModelValidation(pagoConLiquidacion, prestMod);

        if (modVal.isBoolIsOnline()) {
            PagoUtil.subProcessPayment(modVal, pagoConLiquidacion, prestMod);
        }

        return new ResponseEntity<>(modVal.getStrResponse(), modVal.getHttpStatus());
    }

    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> redarrdicobjectCreatePagMod(
            @RequestBody ArrayList<PagoConLiquidacion> darrpagConLiq_I) {
        ArrayList<HashMap<String, Object>> darrdicpagMod_O = new ArrayList<>();
        HttpStatus httpStatus_O = HttpStatus.CREATED;

        for (PagoConLiquidacion pagConLiq : darrpagConLiq_I) {
            HashMap<String, Object> dirobjeto = new HashMap<>();

            ModelValidation modVal;
            PrestamoViewModel prestMod = this.prestamoViewService.findById(pagConLiq.getPrestamoId());
            modVal = PagoUtil.modValPagoModelValidation(pagConLiq, prestMod);

            if (modVal.isBoolIsOnline()) {
                PagoUtil.subProcessPayment(modVal, pagConLiq, prestMod);
            }

            dirobjeto.put("id", pagConLiq.getPagoId());
            dirobjeto.put("isOnline", modVal.isBoolIsOnline());
            dirobjeto.put("msg", modVal.getStrResponse());
            darrdicpagMod_O.add(dirobjeto);
        }

        return new ResponseEntity<>(darrdicpagMod_O, httpStatus_O);
    }

    @CrossOrigin
    @GetMapping(path = "/history/{id}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> redarrpagAgrModGetHistory(
            @PathVariable String id) {
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(fechaActual);

        ArrayList<PagoModel> pagoModels = new ArrayList<>();

        // To easy code
        PagoDynamicModel pagoDynamicModel = this.pagoDynamicService.findByPrestamoIdAnioAndSemana(id,
                calendarioModel.getAnio(),
                calendarioModel.getSemana());

        pagoModels.addAll(this.pagoService
                .findByPrestamoIdAnioNotAndSemanaNotOrderByAnioAscSemanaAsc(id, calendarioModel.getAnio(),
                        calendarioModel.getSemana()));
        if(pagoDynamicModel != null){
            pagoModels.add(new PagoModel(pagoDynamicModel));
        }

        HttpStatus httpStatus_O = HttpStatus.OK;

        if (pagoModels.isEmpty()) {
            httpStatus_O = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(pagoModels, httpStatus_O);
    }

    @CrossOrigin
    @GetMapping(path = "/noPagosWithVisitas/{usuario}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> getNoPagosConVisitasByUsuarioAnioAndSemana(
            @PathVariable String usuario, @PathVariable int anio, @PathVariable int semana)
            throws ExecutionException, InterruptedException {
        UsuarioModel usuarioModel = this.usuarioService.findByUsuario(usuario);
        ArrayList<String> gerenciaIds = this.usuarioGerenciaService
                .darrstrGerenciaIdFindByUsuarioId(usuarioModel.getUsuarioId());

        CompletableFuture<ArrayList<PagoModel>> pagoEntitiesCF = this.pagoService
                .findByGerenciasAnioSemanaAndTipoAsync(gerenciaIds, anio, semana);
        CompletableFuture<ArrayList<VisitaModel>> visitaEntitiesCF = this.visitaService
                .findByGerenciasAnioAndSemanaAsync(gerenciaIds, anio, semana);

        CompletableFuture.allOf(pagoEntitiesCF, visitaEntitiesCF);
        ArrayList<PagoModel> pagoEntities = pagoEntitiesCF.get();
        ArrayList<VisitaModel> visitaEntities = visitaEntitiesCF.get();

        ArrayList<HashMap<String, Object>> noPagosConVisitas = new ArrayList<>();
        pagoEntities.parallelStream().forEach(noPago -> {
            HashMap<String, Object> noPagoConVisitasHM = new HashMap<>();
            noPagoConVisitasHM.put("pagoId", noPago.getPagoId());
            noPagoConVisitasHM.put("prestamoId", noPago.getPrestamoId());
            noPagoConVisitasHM.put("monto", noPago.getMonto());
            noPagoConVisitasHM.put("semana", noPago.getSemana());
            noPagoConVisitasHM.put("anio", noPago.getAnio());
            noPagoConVisitasHM.put("tarifa", noPago.getTarifa());
            noPagoConVisitasHM.put("cliente", noPago.getCliente());
            noPagoConVisitasHM.put("agente", noPago.getAgente());
            noPagoConVisitasHM.put("creadoDesde", noPago.getCreadoDesde());
            noPagoConVisitasHM.put("fechaPago", noPago.getFechaPago());
            noPagoConVisitasHM.put("lat", noPago.getLat());
            noPagoConVisitasHM.put("lng", noPago.getLng());
            noPagoConVisitasHM.put("gerencia", this.agenciaService.findById(noPago.getAgente())
                    .getGerenciaId());

            // To easy code
            String agente = noPagoConVisitasHM.get("agente").toString();
            String gerencia = noPagoConVisitasHM.get("gerencia").toString();

            noPagoConVisitasHM.put("numeroCelularAgente", this.usuarioService.findByAgenciaAndStatus(agente, true)
                    .getNumeroCelular());

            noPagoConVisitasHM.put("numeroCelularGerente", this.usuarioService.findByGerenciaAndTipo(gerencia,
                    "Gerente").getNumeroCelular());

            ArrayList<HashMap<String, Object>> visitas = new ArrayList<>();
            visitaEntities.forEach(visita -> {
                if (noPago.getPrestamoId().equals(visita.getPrestamoId())) {
                    HashMap<String, Object> visitaHM = new HashMap<>();
                    visitaHM.put("visitaId", visita.getVisitaId());
                    visitaHM.put("prestamoId", visita.getPrestamoId());
                    visitaHM.put("semana", visita.getSemana());
                    visitaHM.put("anio", visita.getAnio());
                    visitaHM.put("cliente", visita.getCliente());
                    visitaHM.put("agente", visita.getAgente());
                    visitaHM.put("fecha", visita.getFecha());
                    visitaHM.put("lat", visita.getLat());
                    visitaHM.put("lng", visita.getLng());
                    visitas.add(visitaHM);
                }
            });
            noPagoConVisitasHM.put("visitas", visitas);

            noPagosConVisitas.add(noPagoConVisitasHM);
        });

        return new ResponseEntity<>(noPagosConVisitas, HttpStatus.OK);
    }
}
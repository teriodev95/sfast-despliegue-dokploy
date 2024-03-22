package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.models.mariaDB.VisitaModel;
import tech.calaverita.reporterloanssql.models.view.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.ModelValidation;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.services.AgenciaService;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.UsuarioService;
import tech.calaverita.reporterloanssql.services.VisitaService;
import tech.calaverita.reporterloanssql.services.relation.UsuarioGerenciaService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;
import tech.calaverita.reporterloanssql.utils.PagoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public final class PagoController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoService pagoService;
    private final PrestamoService prestamoService;
    private final UsuarioGerenciaService usuarioGerenciaService;
    private final UsuarioService usuarioService;
    private final VisitaService visitaService;
    private final AgenciaService agenciaService;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoController(
            PagoService pagoService,
            PrestamoService prestamoService,
            UsuarioGerenciaService usuarioGerenciaService,
            UsuarioService usuarioService,
            VisitaService visitaService,
            AgenciaService agenciaService
    ) {
        this.pagoService = pagoService;
        this.prestamoService = prestamoService;
        this.usuarioGerenciaService = usuarioGerenciaService;
        this.usuarioService = usuarioService;
        this.visitaService = visitaService;
        this.agenciaService = agenciaService;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @CrossOrigin
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> redarrpagModGetByAgenciaAnioAndSemana(
            @PathVariable("agencia") String strAgencia_I,
            @PathVariable("anio") int intAnio_I,
            @PathVariable("semana") int intSemana_I
    ) {
        ArrayList<PagoModel> darrpagMod_O = this.pagoService.darrpagModFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I,
                intSemana_I);

        HttpStatus httpStatus_O = HttpStatus.OK;

        if (
                darrpagMod_O.isEmpty()
        ) {
            httpStatus_O = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(darrpagMod_O, httpStatus_O);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> restrCreatePagMod(
            //                                              //Dentro de este endpoint se puede recibir el pago con la
            //                                              //      liquidación, si el pago mandado cuenta con
            //                                              //      liquidacion se hará el proceso correspondiente para
            //                                              //      guardar dicha liquidacion y ajustar la propiedad
            //                                              //      cierraCon de pago, si se manda solo el pago solo se
            //                                              //      realizará el proceso para guardar el pago.

            @RequestBody PagoConLiquidacion pagConLiq_I
    ) {
        ModelValidation modVal;
        PrestamoModel prestMod = this.prestamoService.prestModFindByPrestamoId(pagConLiq_I.getPrestamoId());
        modVal = PagoUtil.modValPagoModelValidation(pagConLiq_I, prestMod);

        if (
                modVal.isBoolIsOnline()
        ) {
            PagoUtil.subProcessPayment(modVal, pagConLiq_I, prestMod);
        }

        return new ResponseEntity<>(modVal.getStrResponse(), modVal.getHttpStatus());
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @PostMapping(path = "/create-many")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> redarrdicobjectCreatePagMod(
            @RequestBody ArrayList<PagoConLiquidacion> darrpagConLiq_I
    ) {
        ArrayList<HashMap<String, Object>> darrdicpagMod_O = new ArrayList<>();
        HttpStatus httpStatus_O = HttpStatus.CREATED;

        for (PagoConLiquidacion pagConLiq : darrpagConLiq_I) {
            HashMap<String, Object> dirobjeto = new HashMap<>();

            ModelValidation modVal;
            PrestamoModel prestMod = this.prestamoService.prestModFindByPrestamoId(pagConLiq.getPrestamoId());
            modVal = PagoUtil.modValPagoModelValidation(pagConLiq, prestMod);

            if (
                    modVal.isBoolIsOnline()
            ) {
                PagoUtil.subProcessPayment(modVal, pagConLiq, prestMod);
            }

            dirobjeto.put("id", pagConLiq.getPagoId());
            dirobjeto.put("isOnline", modVal.isBoolIsOnline());
            dirobjeto.put("msg", modVal.getStrResponse());
            darrdicpagMod_O.add(dirobjeto);
        }

        return new ResponseEntity<>(darrdicpagMod_O, httpStatus_O);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @CrossOrigin
    @GetMapping(path = "/history/{id}")
    public @ResponseBody ResponseEntity<ArrayList<PagoAgrupadoModel>> redarrpagAgrModGetHistory(
            @PathVariable("id") String strId_I
    ) {
        ArrayList<PagoAgrupadoModel> pagAgrMod_O = this.pagoService.darrpagAgrModGetHistorialDePagosToApp(strId_I);
        HttpStatus httpStatus_O = HttpStatus.OK;

        if (
                pagAgrMod_O.isEmpty()
        ) {
            httpStatus_O = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(pagAgrMod_O, httpStatus_O);
    }

    @CrossOrigin
    @GetMapping(path = "/noPagosWithVisitas/{usuario}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<HashMap<String, Object>>> getNoPagosConVisitasByUsuarioAnioAndSemana(
            @PathVariable("usuario") String usuario,
            @PathVariable("anio") int anio,
            @PathVariable("semana") int semana
    ) throws ExecutionException, InterruptedException {
        UsuarioModel usuarioModel = this.usuarioService.usuarModFindByUsuario(usuario);
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
            noPagoConVisitasHM.put("gerencia", this.agenciaService.agencModFindByAgenciaId(noPago.getAgente())
                    .getGerenciaId());

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
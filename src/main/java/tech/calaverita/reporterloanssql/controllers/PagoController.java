package tech.calaverita.reporterloanssql.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.pojos.ModelValidation;
import tech.calaverita.reporterloanssql.pojos.PagoConLiquidacion;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.view.PrestamoService;
import tech.calaverita.reporterloanssql.utils.PagoUtil;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public final class PagoController {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoService pagServ;
    private final PrestamoService prestServ;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoController(
            PagoService pagServ_S,
            PrestamoService prestServ_S
    ) {
        this.pagServ = pagServ_S;
        this.prestServ = prestServ_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    @CrossOrigin
    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoEntity>> redarrpagModGetByAgenciaAnioAndSemana(
            @PathVariable("agencia") String strAgencia_I,
            @PathVariable("anio") int intAnio_I,
            @PathVariable("semana") int intSemana_I
    ) {
        ArrayList<PagoEntity> darrpagMod_O = this.pagServ.darrpagModFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I,
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
        PrestamoEntity prestMod = this.prestServ.prestModFindByPrestamoId(pagConLiq_I.getPrestamoId());
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
            PrestamoEntity prestMod = this.prestServ.prestModFindByPrestamoId(pagConLiq.getPrestamoId());
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
    public @ResponseBody ResponseEntity<ArrayList<PagoAgrupadoEntity>> redarrpagAgrModGetHistory(
            @PathVariable("id") String strId_I
    ) {
        ArrayList<PagoAgrupadoEntity> pagAgrMod_O = this.pagServ.darrpagAgrModGetHistorialDePagosToApp(strId_I);
        HttpStatus httpStatus_O = HttpStatus.OK;

        if (
                pagAgrMod_O.isEmpty()
        ) {
            httpStatus_O = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(pagAgrMod_O, httpStatus_O);
    }
}
package tech.calaverita.reporterloanssql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Liquidacion;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "/xpress/v1/pays")
public class PagoController {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private PrestamoRepository prestamoRepository;

    @GetMapping(path = "/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<ArrayList<PagoModel>> getPagosCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<PagoModel> pagos = pagoRepository.getPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);

        if (pagos.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(pagos, HttpStatus.OK);
    }

    @PostMapping(path = "/create-one")
    public @ResponseBody ResponseEntity<String> setPago(@RequestBody PagoModel pago) {
        Optional<PagoModel> pagoAux = pagoRepository.findById(pago.getPagoId());

        if (pago.getPrestamoId() == null || pago.getPrestamoId().equalsIgnoreCase(""))
            return new ResponseEntity<>("Debe Ingresar El prestamoId", HttpStatus.BAD_REQUEST);

        if(!pagoAux.isEmpty())
            return new ResponseEntity<>("El Pago Ya Existe", HttpStatus.CONFLICT);

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(pago.getPrestamoId());

        if (prestamo == null)
            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Ese prestamoId", HttpStatus.NOT_FOUND);

        pago.setAbreCon(prestamo.getSaldo());
        pago.setCierraCon(prestamo.getSaldo() - pago.getMonto());
        pago.setEsPrimerPago(false);

        pagoRepository.save(pago);

        return new ResponseEntity<>("Pago Insertado con Éxito", HttpStatus.CREATED);
    }

    @PutMapping(path = "/liquidacion-pago")
    public @ResponseBody ResponseEntity<String> setLiquidacionPrestamo(@RequestBody Liquidacion liquidacion) {

        PrestamoModel prestamo = prestamoRepository.getPrestamoModelByPrestamoId(liquidacion.getPrestamoId());

        if (prestamo == null)
            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Ese PrestamoId", HttpStatus.NOT_FOUND);

        prestamo.setWkDescu(liquidacion.getSemana() + "-" + liquidacion.getAnio());
        prestamo.setDescuento(liquidacion.getDescuento());
        prestamo.setSaldo(0.0);

        PagoModel pago = pagoRepository.getPagoModelByPrestamoIdAnioAndSemana(liquidacion.getPrestamoId(), liquidacion.getAnio(), liquidacion.getSemana());

        if (pago == null)
            return new ResponseEntity<>("No Se Encontró Ningún Prestamo Con Esos Parametros", HttpStatus.NOT_FOUND);

        pago.setCierraCon(0.0);

        prestamoRepository.save(prestamo);
        pagoRepository.save(pago);

        return new ResponseEntity<>("Liquidación Cargada con Éxito", HttpStatus.OK);
    }
}

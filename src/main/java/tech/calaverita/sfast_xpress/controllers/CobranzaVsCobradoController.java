package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.PWA.CobranzaPWA;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/cobranzas-vs-cobrados")
public class CobranzaVsCobradoController {
    private final CalendarioService calendarioService;
    private final PrestamoViewService prestamoViewService;

    public CobranzaVsCobradoController(CalendarioService calendarioService, PrestamoViewService prestamoViewService) {
        this.calendarioService = calendarioService;
        this.prestamoViewService = prestamoViewService;
    }

    @GetMapping(path = "/agencia/{agencia}/sin-pago")
    public ResponseEntity<CobranzaPWA> getPrestamosSinPagoByAgenciaSaldoAlIniciarSemanaGreaterThanAnioSemana(
            @PathVariable String agencia) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        List<PrestamoViewModel> prestamoViewModels = this.prestamoViewService
                .findPrestamosSinPagoByAgenciaSaldoAlIniciarSemanaGreaterThanAnioSemana(agencia, 0D, anio, semana);

        return new ResponseEntity<>(new CobranzaPWA(prestamoViewModels), HttpStatus.OK);
    }
}

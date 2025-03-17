package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.UsuarioDto;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.BalanceAgenciaUtil;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/users")
public final class UsuarioController {
    private final UsuarioService usuarioService;
    private final PagoDynamicService pagoDynamicService;
    private final PrestamoViewService prestamoViewService;
    private final CalendarioService calendarioService;
    private final GerenciaService gerenciaService;

    public UsuarioController(UsuarioService usuarioService, PagoDynamicService pagoDynamicService,
            PrestamoViewService prestamoViewService, CalendarioService calendarioService,
            GerenciaService gerenciaService) {
        this.usuarioService = usuarioService;
        this.pagoDynamicService = pagoDynamicService;
        this.prestamoViewService = prestamoViewService;
        this.calendarioService = calendarioService;
        this.gerenciaService = gerenciaService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @GetMapping(path = "/all")
    public @ResponseBody ResponseEntity<Iterable<UsuarioModel>> getAllUsers() {
        Iterable<UsuarioModel> usuarios = this.usuarioService.findAll();

        if (!usuarios.iterator().hasNext())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(path = "/one/{usuarioId}")
    public @ResponseBody ResponseEntity<UsuarioModel> getOneUser(@PathVariable Integer usuarioId) {
        UsuarioModel usuario = this.usuarioService.findById(usuarioId);

        if (usuario == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping(path = "/seguridad_by_gerencia/{gerencia}")
    public @ResponseBody ResponseEntity<UsuarioModel> getByGerenciaInnerJoinUsuarioGerenciaModel(
            @PathVariable String gerencia) {
        UsuarioModel usuarioModel = this.usuarioService.findByGerenciaInnerJoinUsuarioGerenciaModel(gerencia,
                "Seguridad", true);

        if (usuarioModel == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @GetMapping(path = "/pin/{pin}")
    public @ResponseBody ResponseEntity<UsuarioDto> getByPin(@PathVariable Integer pin) {
        UsuarioModel usuarioModel = this.usuarioService.findByPin(pin);

        if (usuarioModel != null) {
            ArrayList<GerenciaModel> gerenciaModels = this.gerenciaService.findByUsuario(usuarioModel.getUsuario());
            UsuarioDto usuarioDto = new UsuarioDto(usuarioModel, gerenciaModels);
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/nivel/by_agencia/{agencia}")
    public @ResponseBody ResponseEntity<Object> getNivelByAgencia(@PathVariable String agencia) {
        HashMap<String, Object> responseHM = new HashMap<>();

        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDateTime.now().toString());
        UsuarioModel usuarioModel = this.usuarioService.findByAgenciaTipoAndStatus(agencia, "Agente", true);

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();
        String fechaJueves = LocalDate.parse(calendarioModel.getDesde()).plusDays(1) + " 23:59:59";

        int clientesPagoCompleto = this.pagoDynamicService
                .findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(agencia, anio, semana)
                .join();

        double debitoTotal = this.prestamoViewService
                .findDebitoTotalByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
                        agencia, 0D, anio, semana)
                .join();

        double cobranzaPura = this.pagoDynamicService
                .findCobranzaPuraByAgenciaAnioSemanaAndFechaPagoLessThanEqual(
                        agencia, anio, semana, fechaJueves)
                .join();

        responseHM.put("clientesPagoCompleto", clientesPagoCompleto);
        responseHM.put("rendimientoHastaJueves", MyUtil.getDouble(cobranzaPura / debitoTotal * 100));
        responseHM.put("mesesTrabajando", BalanceAgenciaUtil.getAntiguedadAgenteEnMeses(usuarioModel));
        responseHM.put("nivel",
                BalanceAgenciaUtil.getNivelAgente(clientesPagoCompleto, cobranzaPura / debitoTotal, usuarioModel));

        return new ResponseEntity<>(responseHM, HttpStatus.OK);
    }
}

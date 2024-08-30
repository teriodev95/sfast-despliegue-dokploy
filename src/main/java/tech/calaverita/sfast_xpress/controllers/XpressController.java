package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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
import tech.calaverita.sfast_xpress.DTOs.cobranza.CobranzaDTO;
import tech.calaverita.sfast_xpress.DTOs.cobranza.DebitosCobranzaDTO;
import tech.calaverita.sfast_xpress.DTOs.cobranza.InfoCobranzaDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.CierreDashboardDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.LiquidacionesDashboardDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.PagosDashboardDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoModel;
import tech.calaverita.sfast_xpress.pojos.LoginResponse;
import tech.calaverita.sfast_xpress.security.AuthCredentials;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.views.PagoAgrupadoService;
import tech.calaverita.sfast_xpress.services.views.PrestamoService;

@RestController()
@RequestMapping(path = "/xpress/v1")
public final class XpressController {
    private final AgenciaService agenciaService;
    private final UsuarioService usuarioService;
    private final PrestamoService prestamoService;
    private final PagoAgrupadoService pagoAgrupadoService;
    private final LiquidacionService liquidacionService;
    private final AsignacionService asignacionService;

    public XpressController(AgenciaService agenciaService, UsuarioService usuarioService,
            PrestamoService prestamoService, PagoAgrupadoService pagoAgrupadoService,
            LiquidacionService liquidacionService,
            AsignacionService asignacionService) {
        this.agenciaService = agenciaService;
        this.usuarioService = usuarioService;
        this.prestamoService = prestamoService;
        this.pagoAgrupadoService = pagoAgrupadoService;
        this.liquidacionService = liquidacionService;
        this.asignacionService = asignacionService;
    }

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Version", Constants.VERSION);
        response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
    }

    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<?> login(@RequestBody AuthCredentials login) {
        UsuarioModel usuarioModel = this.usuarioService.findByUsuarioAndPin(login.getUsername(), login.getPassword());
        LoginResponse loginResponse = new LoginResponse();

        if (usuarioModel == null) {
            return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
        }

        loginResponse.setSolicitante(usuarioModel);
        loginResponse.setInvolucrados(this.usuarioService.findByGerencia(usuarioModel.getGerencia()));

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<CobranzaDTO> getCobranzaByAgencia(@PathVariable String agencia,
            @PathVariable int anio,
            @PathVariable int semana) {
        CompletableFuture<ArrayList<PrestamoModel>> prestamoModels = this.prestamoService
                .findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, 0D);

        InfoCobranzaDTO infoSemanaCobranzaDTO = new InfoCobranzaDTO(prestamoModels.join().get(0), anio, semana,
                prestamoModels.join().size());
        DebitosCobranzaDTO debitosCobranzaDTO = new DebitosCobranzaDTO(prestamoModels.join());
        CobranzaDTO cobranzaDTO = new CobranzaDTO(infoSemanaCobranzaDTO, debitosCobranzaDTO, prestamoModels.join());

        return new ResponseEntity<>(cobranzaDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<DashboardDTO> getDashboardByAgenciaAnioAndSemana(@PathVariable String agencia,
            @PathVariable int anio,
            @PathVariable int semana) {
        CompletableFuture<ArrayList<PrestamoModel>> prestamoModels = this.prestamoService
                .findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, 0D);
        CompletableFuture<ArrayList<PagoAgrupadoModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio,
                        semana, false);
        CompletableFuture<ArrayList<LiquidacionModel>> liquidacionModels = this.liquidacionService
                .findByAgenciaAnioAndSemana(agencia,
                        anio,
                        semana);
        CompletableFuture<Double> asignaciones = this.asignacionService
                .findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana);
        CompletableFuture<String> statusAgencia = this.agenciaService.findStatusById(agencia);

        InfoCobranzaDTO infoSemanaCobranzaDTO = new InfoCobranzaDTO(prestamoModels.join().get(0), anio, semana,
                prestamoModels.join().size());
        DebitosCobranzaDTO debitosCobranzaDTO = new DebitosCobranzaDTO(prestamoModels.join());
        PagosDashboardDTO pagosDashboardDTO = new PagosDashboardDTO(pagoAgrupagoModels.join());
        LiquidacionesDashboardDTO liquidacionesDashboardDTO = new LiquidacionesDashboardDTO(liquidacionModels.join(),
                pagoAgrupagoModels.join());
        CierreDashboardDTO cierreDashboardDTO = new CierreDashboardDTO(pagosDashboardDTO, debitosCobranzaDTO,
                asignaciones.join(), statusAgencia.join());
        DashboardDTO dashboardDTO = new DashboardDTO(infoSemanaCobranzaDTO, pagosDashboardDTO,
                liquidacionesDashboardDTO, debitosCobranzaDTO, cierreDashboardDTO);

        return new ResponseEntity<>(dashboardDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<DashboardDTO> getDashboardByGerencia(@PathVariable String gerencia,
            @PathVariable int anio,
            @PathVariable int semana) {
        ArrayList<String> agencias = this.agenciaService.findIdsByGerenciaId(gerencia);

        ArrayList<DashboardDTO> dashboardDTOs = new ArrayList<>();

        for (int i = 0; i < agencias.size(); i++) {
            dashboardDTOs.add(getDashboardByAgenciaAnioAndSemana(agencias.get(i), anio, semana).getBody());
        }

        DashboardDTO dashboardDTO = new DashboardDTO(dashboardDTOs);

        return new ResponseEntity<>(dashboardDTO, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/local_date_time")
    public ResponseEntity<HashMap<String, String>> getLocalDateTime() {
        String fechaYHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String[] campos = fechaYHora.split(" ");

        HashMap<String, String> fechaYHoraHM = new HashMap<>();
        fechaYHoraHM.put("Fecha", campos[0]);
        fechaYHoraHM.put("Hora", campos[1]);

        return new ResponseEntity<>(fechaYHoraHM, HttpStatus.OK);
    }
}
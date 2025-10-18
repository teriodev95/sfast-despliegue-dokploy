package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.LoginResponse;
import tech.calaverita.sfast_xpress.security.AuthCredentials;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@RestController()
@RequestMapping(path = "/xpress/v1")
public final class XpressController {
        private final AgenciaService agenciaService;
        private final UsuarioService usuarioService;
        private final PrestamoViewService prestamoViewService;
        private final PagoDynamicService pagoAgrupadoService;
        private final LiquidacionService liquidacionService;
        private final AsignacionService asignacionService;
        private final GerenciaService gerenciaService;
        private final VentaService ventaService;

        public XpressController(AgenciaService agenciaService, UsuarioService usuarioService,
                        PrestamoViewService prestamoViewService, PagoDynamicService pagoAgrupadoService,
                        LiquidacionService liquidacionService,
                        AsignacionService asignacionService, GerenciaService gerenciaService,
                        VentaService ventaService) {
                this.agenciaService = agenciaService;
                this.usuarioService = usuarioService;
                this.prestamoViewService = prestamoViewService;
                this.pagoAgrupadoService = pagoAgrupadoService;
                this.liquidacionService = liquidacionService;
                this.asignacionService = asignacionService;
                this.gerenciaService = gerenciaService;
                this.ventaService = ventaService;
        }

        @ModelAttribute
        public void setResponseHeader(HttpServletResponse response) {
                response.setHeader("Version", Constants.VERSION);
                response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
        }

        @PostMapping(path = "/login")
        public @ResponseBody ResponseEntity<?> login(@RequestBody AuthCredentials login) {
                UsuarioModel usuarioModel = this.usuarioService.findByUsuarioAndPin(login.getUsername(),
                                login.getPassword());
                LoginResponse loginResponse = new LoginResponse();

                if (usuarioModel == null) {
                        return new ResponseEntity<>("Usuario y/o contraseña incorrecto", HttpStatus.BAD_REQUEST);
                }

                if(usuarioModel.getStatus() == false){
                        return new ResponseEntity<>("Este usuario ha sido deshabilitado. Contacta a Administracion", HttpStatus.UNAUTHORIZED);
                }

                loginResponse.setSolicitante(usuarioModel);
                loginResponse.setInvolucrados(this.usuarioService.findByGerencia(usuarioModel.getGerencia()));

                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }

        @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<?> getCobranzaByAgencia(@RequestHeader String username,
                        @PathVariable String agencia,
                        @PathVariable int anio,
                        @PathVariable int semana) {
                // Validar que el usuario exista y esté activo
                if (!this.usuarioService.existsByUsuario(username)) {
                        return new ResponseEntity<>("Usuario no existe", HttpStatus.FORBIDDEN);
                } else if (!this.usuarioService.existsByUsuarioAndStatus(username, true)) {
                        return new ResponseEntity<>("Usuario deshabilitado. Contacta a Administracion", HttpStatus.UNAUTHORIZED);
                }

                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D, anio,
                                                semana);

                // Verificar que existan registros antes de continuar
                ArrayList<PrestamoViewModel> prestamos = prestamoViewModels.join();
                if (prestamos == null || prestamos.isEmpty()) {
                        return new ResponseEntity<>("No se encontraron registros de cobranza para la agencia " + agencia +
                                        " en la semana " + semana + " del año " + anio, HttpStatus.NOT_FOUND);
                }

                CompletableFuture<GerenciaModel> gerenciaModel = this.gerenciaService.findByDeprecatedNameAndSucursal(
                                prestamos.get(0).getGerencia(),
                                prestamos.get(0).getSucursal());

                GerenciaModel gerencia = gerenciaModel.join();
                if (gerencia == null) {
                        return new ResponseEntity<>("No se encontró la gerencia para la agencia " + agencia +
                                        ". Gerencia: " + prestamos.get(0).getGerencia() +
                                        ", Sucursal: " + prestamos.get(0).getSucursal(), HttpStatus.NOT_FOUND);
                }

                InfoCobranzaDTO infoSemanaCobranzaDTO = new InfoCobranzaDTO(gerencia.getGerenciaId(),
                                agencia, anio, semana,
                                prestamos.size());
                DebitosCobranzaDTO debitosCobranzaDTO = new DebitosCobranzaDTO(prestamos);
                CobranzaDTO cobranzaDTO = new CobranzaDTO(infoSemanaCobranzaDTO, debitosCobranzaDTO,
                                prestamos);

                return new ResponseEntity<>(cobranzaDTO, HttpStatus.OK);
        }

        @CrossOrigin
        @GetMapping(path = "/campos_cobranza/{campo}/by_agencia/{agencia}")
        public @ResponseBody ResponseEntity<HashMap<String, Object>> getCamposCobranzaByAgencia(
                        @PathVariable String campo,
                        @PathVariable String agencia, @RequestParam(required = false) String filtroDebito) {
                HashMap<String, Object> campos = new HashMap<>();

                CalendarioModel calendarioModel = MyUtil.getSemanaActual();
                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia,
                                                calendarioModel.getAnio(),
                                                calendarioModel.getSemana(), false);
                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D,
                                                calendarioModel.getAnio(),
                                                calendarioModel.getSemana());
                UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipo(agencia, "Agente");
                CompletableFuture<Double> asignaciones = CompletableFuture.completedFuture(0D);
                if (agenteUsuarioModel != null) {
                        asignaciones = this.asignacionService
                                        .findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
                                                        agenteUsuarioModel.getUsuarioId(),
                                                        calendarioModel.getAnio(),
                                                        calendarioModel.getSemana());
                }

                switch (campo) {
                        case "cobranza_pura":
                                // To easy code
                                Double cobranzaPura = MyUtil.getDouble(pagoAgrupagoModels.join().stream()
                                                .mapToDouble(pagoModel -> pagoModel.getMonto() >= pagoModel.getTarifa()
                                                                ? pagoModel.getTarifa()
                                                                : pagoModel.getMonto())
                                                .sum());

                                campos.put("cobranzaPura", cobranzaPura);
                                break;
                        case "debito":
                                if (filtroDebito != null) {
                                        switch (filtroDebito) {

                                                case "MI":
                                                        campos.put("debito",
                                                                        MyUtil.getDouble(getDebitoPorDia(
                                                                                        prestamoViewModels.join(),
                                                                                        "MIERCOLES")));
                                                        break;
                                                case "JU":
                                                        campos.put("debito", MyUtil.getDouble(
                                                                        getDebitoPorDia(prestamoViewModels.join(),
                                                                                        "JUEVES")));
                                                        break;
                                                case "VI":
                                                        campos.put("debito", MyUtil.getDouble(
                                                                        getDebitoPorDia(prestamoViewModels.join(),
                                                                                        "VIERNES")));
                                                        break;
                                                case "TOTAL":
                                                        campos.put("debito",
                                                                        MyUtil.getDouble(getDebitoPorDia(
                                                                                        prestamoViewModels.join(),
                                                                                        "TOTAL")));
                                                        break;
                                                default:
                                                        break;
                                        }
                                }
                                break;
                        case "faltante":
                                // To easy code
                                Double faltante = MyUtil.getDouble(pagoAgrupagoModels.join().stream()
                                                .filter(pagoModel -> pagoModel.getTipo().equals("Reducido"))
                                                .mapToDouble(pagoModel -> pagoModel.getAbreCon() < pagoModel.getTarifa()
                                                                ? pagoModel.getAbreCon() - pagoModel.getMonto()
                                                                : pagoModel.getTarifa() - pagoModel.getMonto())
                                                .sum());

                                campos.put("faltante", faltante);
                                break;
                        case "efectivo_en_campo":
                                // To easy code
                                Double cobranzaTotal = MyUtil
                                                .getDouble(pagoAgrupagoModels.join().stream()
                                                                .filter(pagoModel -> !pagoModel.getTipo()
                                                                                .equals("Multa"))
                                                                .mapToDouble(PagoDynamicModel::getMonto).sum());
                                Double efectivoEnCampo = MyUtil.getDouble(cobranzaTotal - asignaciones.join());

                                campos.put("efectivoEnCampo", efectivoEnCampo);
                                break;
                        default:
                                campos.put("response", "Campo no encontrado");
                                break;
                }

                return new ResponseEntity<>(campos, HttpStatus.OK);
        }

        @CrossOrigin
        @GetMapping(path = "/campos_cobranza/{campo}/by_gerencia/{gerencia}")
        public @ResponseBody ResponseEntity<HashMap<String, Object>> getCamposCobranzaByGerencia(
                        @PathVariable String campo, @PathVariable String gerencia,
                        @RequestParam(required = false) String filtroDebito) {
                ArrayList<String> agencias = this.agenciaService.findIdsByGerenciaId(gerencia);
                GerenciaModel gerenciaModel = this.gerenciaService.findById(gerencia);

                HashMap<String, Object> campos = new HashMap<>();

                CalendarioModel calendarioModel = MyUtil.getSemanaActual();
                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                .findByGerenciaSucursalAnioSemanaAndEsPrimerPago(
                                                gerenciaModel.getDeprecatedName(),
                                                gerenciaModel.getSucursal(),
                                                calendarioModel.getAnio(),
                                                calendarioModel.getSemana(), false);
                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByGerenciaSucursalAndSaldoAlIniciarSemanaGreaterThan(
                                                gerenciaModel.getDeprecatedName(),
                                                gerenciaModel.getSucursal(), 0D);

                switch (campo) {
                        case "cobranza_pura":

                                agencias.forEach(agencia -> {
                                        // To easy code
                                        Double cobranzaPura = MyUtil.getDouble(pagoAgrupagoModels.join().stream()
                                                        .filter(pagoAgrupadoModel -> pagoAgrupadoModel.getAgencia()
                                                                        .equals(agencia))
                                                        .mapToDouble(pagoModel -> pagoModel.getMonto() >= pagoModel
                                                                        .getTarifa()
                                                                                        ? pagoModel.getTarifa()
                                                                                        : pagoModel.getMonto())
                                                        .sum());

                                        campos.put(agencia, cobranzaPura);
                                });
                                break;
                        case "debito":
                                if (filtroDebito != null) {
                                        switch (filtroDebito) {

                                                case "MI":
                                                        agencias.forEach(agencia -> {
                                                                campos.put(agencia, MyUtil.getDouble(getDebitoPorDia(
                                                                                prestamoViewModels
                                                                                                .join()
                                                                                                .stream()
                                                                                                .filter(prestamoViewModel -> prestamoViewModel
                                                                                                                .getAgencia()
                                                                                                                .equals(agencia))
                                                                                                .collect(Collectors
                                                                                                                .toList()),
                                                                                "MIERCOLES")));
                                                        });
                                                        break;
                                                case "JU":
                                                        agencias.forEach(agencia -> {
                                                                campos.put(agencia, MyUtil.getDouble(getDebitoPorDia(
                                                                                prestamoViewModels
                                                                                                .join()
                                                                                                .stream()
                                                                                                .filter(prestamoViewModel -> prestamoViewModel
                                                                                                                .getAgencia()
                                                                                                                .equals(agencia))
                                                                                                .collect(Collectors
                                                                                                                .toList()),
                                                                                "JUEVES")));
                                                        });
                                                        break;
                                                case "VI":
                                                        agencias.forEach(agencia -> {
                                                                campos.put(agencia, MyUtil.getDouble(getDebitoPorDia(
                                                                                prestamoViewModels
                                                                                                .join()
                                                                                                .stream()
                                                                                                .filter(prestamoViewModel -> prestamoViewModel
                                                                                                                .getAgencia()
                                                                                                                .equals(agencia))
                                                                                                .collect(Collectors
                                                                                                                .toList()),
                                                                                "VIERNES")));
                                                        });
                                                        break;
                                                case "TOTAL":
                                                        agencias.forEach(agencia -> {
                                                                campos.put(agencia, MyUtil.getDouble(getDebitoPorDia(
                                                                                prestamoViewModels
                                                                                                .join()
                                                                                                .stream()
                                                                                                .filter(prestamoViewModel -> prestamoViewModel
                                                                                                                .getAgencia()
                                                                                                                .equals(agencia))
                                                                                                .collect(Collectors
                                                                                                                .toList()),
                                                                                "TOTAL")));
                                                        });
                                                        break;
                                                default:
                                                        break;
                                        }
                                }
                                break;
                        case "faltante":
                                agencias.forEach(agencia -> {
                                        // To easy code
                                        Double faltante = MyUtil.getDouble(pagoAgrupagoModels.join().stream()
                                                        .filter(pagoModel -> pagoModel.getTipo().equals("Reducido"))
                                                        .filter(prestamoViewModel -> prestamoViewModel
                                                                        .getAgencia()
                                                                        .equals(agencia))
                                                        .mapToDouble(pagoModel -> pagoModel.getAbreCon() < pagoModel
                                                                        .getTarifa()
                                                                                        ? pagoModel.getAbreCon()
                                                                                                        - pagoModel.getMonto()
                                                                                        : pagoModel.getTarifa()
                                                                                                        - pagoModel.getMonto())
                                                        .sum());

                                        campos.put(agencia, faltante);
                                });
                                break;
                        case "efectivo_en_campo":
                                agencias.forEach(agencia -> {
                                        UsuarioModel agenteUsuarioModel = this.usuarioService
                                                        .findByAgenciaTipo(agencia, "Agente");

                                        CompletableFuture<Double> asignaciones = CompletableFuture.completedFuture(0D);
                                        if (agenteUsuarioModel != null) {
                                                asignaciones = this.asignacionService
                                                                .findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
                                                                                agenteUsuarioModel.getUsuarioId(),
                                                                                calendarioModel.getAnio(),
                                                                                calendarioModel.getSemana());
                                        }

                                        // To easy code
                                        Double cobranzaTotal = MyUtil
                                                        .getDouble(pagoAgrupagoModels.join().stream()
                                                                        .filter(pagoModel -> !pagoModel.getTipo()
                                                                                        .equals("Multa"))
                                                                        .filter(prestamoViewModel -> prestamoViewModel
                                                                                        .getAgencia()
                                                                                        .equals(agencia))
                                                                        .mapToDouble(PagoDynamicModel::getMonto).sum());

                                        Double efectivoEnCampo = MyUtil.getDouble(cobranzaTotal - asignaciones.join());

                                        campos.put(agencia, efectivoEnCampo);
                                });
                                break;
                        default:
                                campos.put("response", "Campo no encontrado");
                                break;
                }

                return new ResponseEntity<>(campos, HttpStatus.OK);
        }

        @CrossOrigin
        @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<DashboardDTO> getDashboardByAgenciaAnioAndSemana(
                        @PathVariable String agencia,
                        @PathVariable int anio,
                        @PathVariable int semana) {
                DashboardDTO dashboardDTO = new DashboardDTO();
                UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipo(agencia, "Agente");

                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D, anio,
                                                semana);
                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio,
                                                semana, false);
                CompletableFuture<ArrayList<LiquidacionModel>> liquidacionModels = this.liquidacionService
                                .findByAgenciaAnioAndSemana(agencia,
                                                anio,
                                                semana);
                CompletableFuture<ArrayList<PagoDynamicModel>> liquidacionesPagoModels = this.liquidacionService
                                .findPagoModelsByAgenciaAnioAndSemana(agencia,
                                                anio,
                                                semana);
                CompletableFuture<Double> asignaciones = CompletableFuture.completedFuture(0D);
                if (agenteUsuarioModel != null) {
                        asignaciones = this.asignacionService
                                        .findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
                                                        agenteUsuarioModel.getUsuarioId(), anio, semana);
                }
                CompletableFuture<String> statusAgencia = this.agenciaService.findStatusById(agencia);

                if (!prestamoViewModels.join().isEmpty()) {
                        CompletableFuture<GerenciaModel> gerenciaModel = this.gerenciaService
                                        .findByDeprecatedNameAndSucursal(
                                                        prestamoViewModels.join().get(0).getGerencia(),
                                                        prestamoViewModels.join().get(0).getSucursal());

                        InfoCobranzaDTO infoSemanaCobranzaDTO = new InfoCobranzaDTO(
                                        gerenciaModel.join().getGerenciaId(),
                                        agencia, anio, semana,
                                        prestamoViewModels.join().size());
                        DebitosCobranzaDTO debitosCobranzaDTO = new DebitosCobranzaDTO(prestamoViewModels.join());
                        LiquidacionesDashboardDTO liquidacionesDashboardDTO = new LiquidacionesDashboardDTO(
                                        liquidacionModels.join(),
                                        pagoAgrupagoModels.join(), liquidacionesPagoModels.join());
                        PagosDashboardDTO pagosDashboardDTO = new PagosDashboardDTO(pagoAgrupagoModels.join(),
                                        liquidacionesDashboardDTO.getLiquidaciones(),
                                        debitosCobranzaDTO.getDebitoTotal());
                        CierreDashboardDTO cierreDashboardDTO = new CierreDashboardDTO(pagosDashboardDTO,
                                        debitosCobranzaDTO,
                                        asignaciones.join(), statusAgencia.join());
                        ArrayList<VentaModel> ventaModels = this.ventaService.findByAgenciaAnioAndSemana(agencia, anio,
                                        semana);
                        dashboardDTO = new DashboardDTO(infoSemanaCobranzaDTO, pagosDashboardDTO,
                                        liquidacionesDashboardDTO, debitosCobranzaDTO, cierreDashboardDTO, ventaModels);
                }

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

        private Double getDebitoPorDia(List<PrestamoViewModel> prestamoViewModels, String diaDePago) {
                Double debitoPorDia = 0D;

                for (PrestamoViewModel prestamoViewModel : prestamoViewModels) {
                        if (diaDePago.equals("TOTAL")) {
                                Double tarifa;

                                if (prestamoViewModel.getTarifa() <= prestamoViewModel.getSaldoAlIniciarSemana()) {
                                        tarifa = prestamoViewModel.getTarifa();
                                } else {
                                        tarifa = prestamoViewModel.getSaldoAlIniciarSemana();
                                }

                                debitoPorDia += tarifa;
                        } else if (prestamoViewModel.getDiaDePago().equals(diaDePago)) {
                                Double tarifa;

                                if (prestamoViewModel.getTarifa() <= prestamoViewModel.getSaldoAlIniciarSemana()) {
                                        tarifa = prestamoViewModel.getTarifa();
                                } else {
                                        tarifa = prestamoViewModel.getSaldoAlIniciarSemana();
                                }

                                debitoPorDia += tarifa;
                        }
                }

                return debitoPorDia;
        }
}
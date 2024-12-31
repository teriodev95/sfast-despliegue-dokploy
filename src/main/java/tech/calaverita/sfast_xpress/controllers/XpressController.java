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
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.pojos.LoginResponse;
import tech.calaverita.sfast_xpress.security.AuthCredentials;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
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

        public XpressController(AgenciaService agenciaService, UsuarioService usuarioService,
                        PrestamoViewService prestamoViewService, PagoDynamicService pagoAgrupadoService,
                        LiquidacionService liquidacionService,
                        AsignacionService asignacionService, GerenciaService gerenciaService) {
                this.agenciaService = agenciaService;
                this.usuarioService = usuarioService;
                this.prestamoViewService = prestamoViewService;
                this.pagoAgrupadoService = pagoAgrupadoService;
                this.liquidacionService = liquidacionService;
                this.asignacionService = asignacionService;
                this.gerenciaService = gerenciaService;
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

                loginResponse.setSolicitante(usuarioModel);
                loginResponse.setInvolucrados(this.usuarioService.findByGerencia(usuarioModel.getGerencia()));

                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }

        @GetMapping(path = "/cobranza/{agencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<CobranzaDTO> getCobranzaByAgencia(@PathVariable String agencia,
                        @PathVariable int anio,
                        @PathVariable int semana) {
                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, 0D);
                CompletableFuture<GerenciaModel> gerenciaModel = this.gerenciaService.findByDeprecatedNameAndSucursal(
                                prestamoViewModels.join().get(0).getGerencia(),
                                prestamoViewModels.join().get(0).getSucursal());

                InfoCobranzaDTO infoSemanaCobranzaDTO = new InfoCobranzaDTO(gerenciaModel.join().getGerenciaId(),
                                agencia, anio, semana,
                                prestamoViewModels.join().size());
                DebitosCobranzaDTO debitosCobranzaDTO = new DebitosCobranzaDTO(prestamoViewModels.join());
                CobranzaDTO cobranzaDTO = new CobranzaDTO(infoSemanaCobranzaDTO, debitosCobranzaDTO,
                                prestamoViewModels.join());

                return new ResponseEntity<>(cobranzaDTO, HttpStatus.OK);
        }

        @CrossOrigin
        @GetMapping(path = "/campos_cobranza/{campo}/by_agencia/{agencia}")
        public @ResponseBody ResponseEntity<HashMap<String, Object>> getCamposCobranzaByAgencia(
                        @PathVariable String campo,
                        @PathVariable String agencia, @RequestParam(required = false) String filtroDebito) {
                HashMap<String, Object> campos = new HashMap<>();
                switch (campo) {
                        case "cobranza_pura":
                                CalendarioModel calendarioModel = MyUtil.getSemanaActual();
                                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia,
                                                                calendarioModel.getAnio(),
                                                                calendarioModel.getSemana(), false);

                                // To easy code
                                Double cobranzaPura = MyUtil.getDouble(pagoAgrupagoModels.join().stream()
                                                .mapToDouble(pagoModel -> pagoModel.getMonto() >= pagoModel.getTarifa()
                                                                ? pagoModel.getTarifa()
                                                                : pagoModel.getMonto())
                                                .sum());

                                campos.put("cobranzaPura", cobranzaPura);

                                break;
                        case "debito":
                                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                                .findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, 0D);

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
                switch (campo) {
                        case "cobranza_pura":
                                CalendarioModel calendarioModel = MyUtil.getSemanaActual();
                                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                                .findByGerenciaSucursalAnioSemanaAndEsPrimerPago(
                                                                gerenciaModel.getDeprecatedName(),
                                                                gerenciaModel.getSucursal(),
                                                                calendarioModel.getAnio(),
                                                                calendarioModel.getSemana(), false);
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
                                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                                .findByGerenciaSucursalAndSaldoAlIniciarSemanaGreaterThan(
                                                                gerenciaModel.getDeprecatedName(),
                                                                gerenciaModel.getSucursal(), 0D);

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

                CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
                                .findByAgenciaAndSaldoAlIniciarSemanaGreaterThan(agencia, 0D);
                CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
                                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio,
                                                semana, false);
                CompletableFuture<ArrayList<LiquidacionModel>> liquidacionModels = this.liquidacionService
                                .findByAgenciaAnioAndSemana(agencia,
                                                anio,
                                                semana);
                CompletableFuture<Double> asignaciones = this.asignacionService
                                .findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana);
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
                                        pagoAgrupagoModels.join());
                        PagosDashboardDTO pagosDashboardDTO = new PagosDashboardDTO(pagoAgrupagoModels.join(),
                                        liquidacionesDashboardDTO.getLiquidaciones());
                        CierreDashboardDTO cierreDashboardDTO = new CierreDashboardDTO(pagosDashboardDTO,
                                        debitosCobranzaDTO,
                                        asignaciones.join(), statusAgencia.join());
                        dashboardDTO = new DashboardDTO(infoSemanaCobranzaDTO, pagosDashboardDTO,
                                        liquidacionesDashboardDTO, debitosCobranzaDTO, cierreDashboardDTO);
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
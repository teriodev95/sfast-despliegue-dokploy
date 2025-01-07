package tech.calaverita.sfast_xpress.controllers.PGS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.AsignacionesYGastosDTO;
import tech.calaverita.sfast_xpress.DTOs.DataCierreDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.controllers.XpressController;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.models.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.BalanceAgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosAgenteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.EgresosGerenteModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.IngresosAgenteModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.GastoService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.BalanceAgenciaService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.EgresosAgenteService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.EgresosGerenteService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.IngresosAgenteService;
import tech.calaverita.sfast_xpress.utils.CierreSemanalUtil;
import tech.calaverita.sfast_xpress.utils.DesgloceCobranzaYComisionesUtil;
import tech.calaverita.sfast_xpress.utils.LogUtil;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/cierres_semanales")
public final class CierreSemanalController {
        private final AsignacionService asignacionService;
        private final UsuarioService usuarioService;
        private final XpressController xpressController;
        private final BalanceAgenciaService balanceAgenciaService;
        private final CierreSemanalService cierreSemanalService;
        private final EgresosAgenteService egresosAgenteService;
        private final EgresosGerenteService egresosGerenteService;
        private final IngresosAgenteService ingresosAgenteService;
        private final VentaService ventaService;
        private final GastoService gastoService;
        private final PagoService pagoService;
        private final AgenciaService agenciaService;
        private final GerenciaService gerenciaService;

        public CierreSemanalController(AsignacionService asignacionService, UsuarioService usuarioService,
                        XpressController xpressController, BalanceAgenciaService balanceAgenciaService,
                        CierreSemanalService cierreSemanalService, EgresosAgenteService egresosAgenteService,
                        EgresosGerenteService egresosGerenteService,
                        IngresosAgenteService ingresosAgenteService, VentaService ventaService,
                        GastoService gastoService,
                        PagoService pagoService, GerenciaService gerenciaService, AgenciaService agenciaService) {
                this.asignacionService = asignacionService;
                this.usuarioService = usuarioService;
                this.xpressController = xpressController;
                this.balanceAgenciaService = balanceAgenciaService;
                this.cierreSemanalService = cierreSemanalService;
                this.egresosAgenteService = egresosAgenteService;
                this.egresosGerenteService = egresosGerenteService;
                this.ingresosAgenteService = ingresosAgenteService;
                this.ventaService = ventaService;
                this.gastoService = gastoService;
                this.pagoService = pagoService;
                this.gerenciaService = gerenciaService;
                this.agenciaService = agenciaService;
        }

        @ModelAttribute
        public void setResponseHeader(HttpServletResponse response) {
                response.setHeader("Version", Constants.VERSION);
                response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
        }

        @GetMapping(path = "/{agencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<?> getByAgenciaAnioAndSemana(
                        @RequestHeader(required = false) String staticToken,
                        @RequestHeader String username,
                        @PathVariable String agencia,
                        @PathVariable int anio, @PathVariable int semana)
                        throws ExecutionException, InterruptedException {
                CierreSemanalDTO cierreSemanalDTO = null;
                HttpStatus responseStatus = HttpStatus.OK;

                DashboardDTO dashboard;
                List<UsuarioModel> usuarioModels;
                double asignaciones;

                Optional<CierreSemanalModel> cierreSemanalEntity = this.cierreSemanalService
                                .findByAgenciaAnioAndSemana(agencia, anio, semana);

                // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
                if (!this.usuarioService.existsByUsuario(username)) {
                        responseStatus = HttpStatus.FORBIDDEN;
                } else if (!this.usuarioService.existsByUsuarioAndStatus(username, true)) {
                        responseStatus = HttpStatus.UNAUTHORIZED;
                } else {
                        if (cierreSemanalEntity.isPresent()) {
                                cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(cierreSemanalEntity.get());
                        } else if (this.agenciaService.existsById(agencia)) {
                                dashboard = xpressController.getDashboardByAgenciaAnioAndSemana(agencia, anio, semana)
                                                .getBody();
                                usuarioModels = new ArrayList<>();

                                // To easy code
                                UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaAndStatus(agencia,
                                                true) == null ? UsuarioModel.getSinAgenteAsignado()
                                                                : this.usuarioService
                                                                                .findByAgenciaAndStatus(agencia, true);
                                AgenciaModel agenciaModel = this.agenciaService.findById(agencia);

                                UsuarioModel gerenteUsuarioModel = this.usuarioService.findByGerenciaTipoAndStatus(
                                                agenciaModel.getGerenciaId(), "Gerente", true) == null
                                                                ? UsuarioModel.getSinGerenteAsignado()
                                                                : this.usuarioService
                                                                                .findByGerenciaTipoAndStatus(
                                                                                                agenciaModel.getGerenciaId(),
                                                                                                "Gerente", true);

                                usuarioModels.add(agenteUsuarioModel);
                                usuarioModels.add(gerenteUsuarioModel);

                                asignaciones = this.asignacionService
                                                .findSumaAsigancionesByAgenciaAnioAndSemana(agencia, anio, semana)
                                                .join();

                                cierreSemanalDTO = CierreSemanalUtil.getCierreSemanalDTO(dashboard, usuarioModels,
                                                asignaciones);
                        } else {
                                return new ResponseEntity<>("La agencia no existe", HttpStatus.BAD_REQUEST);
                        }
                }
                // } else {
                // responseStatus = HttpStatus.BAD_REQUEST;
                // }

                return new ResponseEntity<>(cierreSemanalDTO, responseStatus);
        }

        @PostMapping(path = "/create-one")
        public @ResponseBody ResponseEntity<String> createOne(@RequestBody CierreSemanalDTO cierreSemanalDTO,
                        @RequestHeader(required = false) String staticToken,
                        @RequestHeader String username)
                        throws DocumentException, FileNotFoundException {
                String responseText = "";
                HttpStatus responseStatus;
                CierreSemanalModel cierreSemanalModel = this.cierreSemanalService
                                .getCierreSemanalEntity(cierreSemanalDTO);

                // To easy code
                String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
                                + cierreSemanalModel.getId() + ".pdf";

                cierreSemanalDTO.setPDF(urlPDF);
                cierreSemanalModel.setPDF(urlPDF);
                cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

                // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
                if (!this.usuarioService.existsByUsuarioAndTipoIn(username, new String[] { "Gerente", "Seguridad" })) {
                        responseStatus = HttpStatus.FORBIDDEN;
                } else if (!this.usuarioService.existsByUsuarioTipoInAndStatus(username,
                                new String[] { "Gerente", "Seguridad" }, true)) {
                        responseStatus = HttpStatus.UNAUTHORIZED;
                } else {
                        if (this.cierreSemanalService.findById(cierreSemanalModel.getId()).isEmpty()) {
                                BalanceAgenciaModel balanceAgenciaModel = this.balanceAgenciaService
                                                .getBalanceAgenciaEntity(cierreSemanalDTO.getBalanceAgencia());
                                balanceAgenciaModel.setId(cierreSemanalModel.getBalanceAgenciaId());
                                this.balanceAgenciaService.save(balanceAgenciaModel);

                                EgresosAgenteModel egresosAgenteModel = this.egresosAgenteService
                                                .getEgresosGerenteEntity(cierreSemanalDTO.getEgresosAgente());
                                egresosAgenteModel.setId(cierreSemanalModel.getEgresosAgenteId());
                                this.egresosAgenteService.save(egresosAgenteModel);

                                EgresosGerenteModel egresosGerenteModel = this.egresosGerenteService
                                                .getEgresosGerenteEntity(cierreSemanalDTO.getEgresosGerente());
                                egresosGerenteModel.setId(cierreSemanalModel.getEgresosGerenteId());
                                this.egresosGerenteService.save(egresosGerenteModel);

                                IngresosAgenteModel ingresosAgenteModel = this.ingresosAgenteService
                                                .getIngresosAgenteEntity(cierreSemanalDTO.getIngresosAgente());
                                ingresosAgenteModel.setId(cierreSemanalModel.getIngresosAgenteId());
                                this.ingresosAgenteService.save(ingresosAgenteModel);

                                this.cierreSemanalService.save(cierreSemanalModel);

                                responseText = urlPDF;
                                responseStatus = HttpStatus.CREATED;
                        } else {
                                responseText = "No se pudo registrar el cierre semanal porque ya existe";
                                responseStatus = HttpStatus.CONFLICT;
                        }
                }
                // } else {
                // responseText = "Token inválido";
                // responseStatus = HttpStatus.BAD_REQUEST;
                // }

                CierreSemanalUtil.subSendCierreSemanalMessage(cierreSemanalDTO);
                // CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);

                return new ResponseEntity<>(responseText, responseStatus);
        }

        @GetMapping(path = "/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
        public @ResponseBody ResponseEntity<InputStreamResource> getPdfByRuta(@PathVariable String file)
                        throws IOException {
                FileInputStream fileInputStream = new FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

                return new ResponseEntity<>(new InputStreamResource(fileInputStream), HttpStatus.OK);
        }

        @PostMapping(path = "/desgloce_cobranza_y_comisiones/pdf/create-one")
        public @ResponseBody ResponseEntity<String> desgloceCobranzaYComisionesCreateOne(
                        @RequestBody DataCierreDTO dataCierreDTO)
                        throws DocumentException, FileNotFoundException {
                TablaDetallesCierreAgencias tablaDetallesCierreAgencias = dataCierreDTO
                                .getTablaDetallesCierreAgencias();

                String idPDF = "CIERRE_" + tablaDetallesCierreAgencias.getZona() + "_SEM"
                                + tablaDetallesCierreAgencias.getSemana() + "_"
                                + tablaDetallesCierreAgencias.getAnio();
                String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/desgloce_cobranza_y_comisiones/pdf/"
                                + idPDF + ".pdf";

                DesgloceCobranzaYComisionesUtil.createCierreSemanalPDF(dataCierreDTO, idPDF);

                return new ResponseEntity<>(urlPDF, HttpStatus.OK);
        }

        @GetMapping(path = "/desgloce_cobranza_y_comisiones/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
        public @ResponseBody ResponseEntity<InputStreamResource> getPdfDesgloceCobranzaYComisionesByRuta(
                        @PathVariable String file)
                        throws IOException {
                FileInputStream fileInputStream = new FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

                return new ResponseEntity<>(new InputStreamResource(fileInputStream), HttpStatus.OK);
        }

        @GetMapping(path = "/{agencia}/{anio}/{semana}/{nivel}")
        public @ResponseBody ResponseEntity<ComisionCobranza> getComisionCobranzaByAgenciaAnioSemanaAndNivel(
                        @PathVariable String agencia, @PathVariable int anio, @PathVariable int semana,
                        @PathVariable String nivel)
                        throws ExecutionException, InterruptedException {
                HashMap<String, Integer> porcentajesComision = new HashMap<>();
                porcentajesComision.put("SILVER", 7);
                porcentajesComision.put("GOLD", 8);
                porcentajesComision.put("PLATINUM", 9);
                porcentajesComision.put("DIAMOND", 10);

                int porcentajeComision = porcentajesComision.get(nivel);

                double cobranzaTotal = CierreSemanalUtil.getCobranzaTotalByAgenciaAnioAndSemana(agencia, anio, semana);

                ComisionCobranza comisionCobranza = new ComisionCobranza(porcentajeComision,
                                cobranzaTotal / 100 * porcentajeComision);

                return new ResponseEntity<>(comisionCobranza, HttpStatus.OK);
        }

        @GetMapping(path = "/asignaciones_y_gastos/by_gerencia_anio_and_semana/{gerencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<AsignacionesYGastosDTO> getAsignacionesYGastosByGerenciaAnioAndSemana(
                        @PathVariable String gerencia, @PathVariable int anio, @PathVariable int semana) {
                UsuarioModel usuarioModel = this.usuarioService.findByGerenciaTipoAndStatus(gerencia, "Gerente", true);
                GerenciaModel gerenciaModel = this.gerenciaService.findById(gerencia);

                AsignacionesYGastosDTO asignacionesYGastosDTO = new AsignacionesYGastosDTO();

                // Queries
                ArrayList<AsignacionModel> ingresosAdministracionAsignacionModels = this.asignacionService
                                .findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Jefe de Admin");
                ArrayList<AsignacionModel> ingresosSeguridadAsignacionModels = this.asignacionService
                                .findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Seguridad");
                ArrayList<AsignacionModel> ingresosOperacionGerentesAsignacionModels = this.asignacionService
                                .findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Gerente");
                ArrayList<AsignacionModel> ingresosOperacionAgentesAsignacionModels = this.asignacionService
                                .findByQuienRecibioUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Agente");

                asignacionesYGastosDTO.setIngresosAdministracion(ingresosAdministracionAsignacionModels
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());
                asignacionesYGastosDTO.setIngresosSeguridad(ingresosSeguridadAsignacionModels
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());
                asignacionesYGastosDTO.setIngresosOperacionGerentes(ingresosOperacionGerentesAsignacionModels
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());
                asignacionesYGastosDTO.setIngresosOperacionAgentes(ingresosOperacionAgentesAsignacionModels
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());

                asignacionesYGastosDTO.setEgresosAdministracion(this.asignacionService
                                .findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Jefe de Admin")
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());
                asignacionesYGastosDTO.setEgresosSeguridad(this.asignacionService
                                .findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Seguridad")
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());
                asignacionesYGastosDTO.setEgresosOperacionGerentes(this.asignacionService
                                .findByQuienEntregoUsuarioIdAnioSemanaAndTipoInnerJoinUsuarioModel(
                                                usuarioModel.getUsuarioId(), anio,
                                                semana,
                                                "Gerente")
                                .stream()
                                .mapToDouble(asignacion -> asignacion.getMonto()).sum());

                // Queries
                ArrayList<VentaModel> ventaModels = this.ventaService.findByGerenciaAnioAndSemana(gerencia, anio,
                                semana);

                asignacionesYGastosDTO.setEgresosVentas(ventaModels
                                .stream().mapToDouble(venta -> venta.getMonto()).sum());

                asignacionesYGastosDTO.setEgresosGasolina(this.gastoService
                                .findByCreadoPorIdAnioSemanaAndTipoGasto(usuarioModel.getUsuarioId(), anio, semana,
                                                "GASOLINA")
                                .stream()
                                .mapToDouble(gasto -> gasto.getMonto()).sum());
                asignacionesYGastosDTO.setEgresosCasetas(this.gastoService
                                .findByCreadoPorIdAnioSemanaAndTipoGasto(usuarioModel.getUsuarioId(), anio, semana,
                                                "CASETAS")
                                .stream()
                                .mapToDouble(gasto -> gasto.getMonto()).sum());
                asignacionesYGastosDTO.setEgresosMantenimientoAuto(this.gastoService
                                .findByCreadoPorIdAnioSemanaAndTipoGasto(usuarioModel.getUsuarioId(), anio, semana,
                                                "MANTENIMIENTO_VEHICULAR")
                                .stream()
                                .mapToDouble(gasto -> gasto.getMonto()).sum());
                asignacionesYGastosDTO.setEgresosTelefono(this.gastoService
                                .findByCreadoPorIdAnioSemanaAndTipoGasto(usuarioModel.getUsuarioId(), anio, semana,
                                                "CELULAR")
                                .stream()
                                .mapToDouble(gasto -> gasto.getMonto()).sum());

                asignacionesYGastosDTO.setIngresosPrimerosPagos(ventaModels
                                .stream().mapToDouble(pago -> pago.getPrimerPago()).sum());

                // To easy code
                ArrayList<EgresosGerenteModel> egresosGerenteModels = this.egresosGerenteService.findByIdLike(gerencia,
                                anio,
                                semana);
                double comisiones = egresosGerenteModels.stream()
                                .mapToDouble(egresosGerente -> egresosGerente.getPagoComisionCobranza()
                                                + egresosGerente.getPagoComisionVentas())
                                .sum();
                double bonos = egresosGerenteModels.stream()
                                .mapToDouble(egresosGerente -> egresosGerente.getBonos())
                                .sum();

                asignacionesYGastosDTO.setEgresosPagoComisiones(comisiones);
                asignacionesYGastosDTO.setEgresosPagoBonos(bonos);
                asignacionesYGastosDTO.sumaIngresos();
                asignacionesYGastosDTO.sumaEgresos();
                asignacionesYGastosDTO.setBalance();

                asignacionesYGastosDTO.formatToDouble();

                return new ResponseEntity<>(asignacionesYGastosDTO, HttpStatus.OK);
        }

        @Data
        public static class ComisionCobranza {
                private Integer porcentajeComisionCobranza;
                private Double pagoComisionCobranza;

                public ComisionCobranza(Integer porcentajeComisionCobranza, Double pagoComisionCobranza) {
                        this.porcentajeComisionCobranza = porcentajeComisionCobranza;
                        this.pagoComisionCobranza = pagoComisionCobranza;
                }
        }
}

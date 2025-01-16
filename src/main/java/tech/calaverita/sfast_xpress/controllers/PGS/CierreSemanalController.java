package tech.calaverita.sfast_xpress.controllers.PGS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
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
import tech.calaverita.sfast_xpress.DTOs.CierreSemanalConsolidadoV2DTO;
import tech.calaverita.sfast_xpress.DTOs.DataCierreDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.controllers.XpressController;
import tech.calaverita.sfast_xpress.itext.CierreGerenciaAdmin.page1.tables.classes.TablaDetallesCierreAgencias;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.CierreSemanalConsolidadoV2Service;
import tech.calaverita.sfast_xpress.services.ComisionService;
import tech.calaverita.sfast_xpress.services.GastoService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.PagoService;
import tech.calaverita.sfast_xpress.services.ProcedimientoService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.sfast_xpress.utils.CierreSemanalUtil;
import tech.calaverita.sfast_xpress.utils.DesgloceCobranzaYComisionesUtil;

@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/cierres_semanales")
public final class CierreSemanalController {
        private final AsignacionService asignacionService;
        private final UsuarioService usuarioService;
        private final XpressController xpressController;
        private final CierreSemanalService cierreSemanalService;
        private final AgenciaService agenciaService;
        private final ProcedimientoService procedimientoService;
        private final ComisionService comisionService;
        private final CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service;
        private final CalendarioService calendarioService;

        public CierreSemanalController(AsignacionService asignacionService, UsuarioService usuarioService,
                        XpressController xpressController, CierreSemanalService cierreSemanalService,
                        VentaService ventaService, GastoService gastoService, PagoService pagoService,
                        GerenciaService gerenciaService, AgenciaService agenciaService,
                        ProcedimientoService procedimientoService, ComisionService comisionService,
                        CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service,
                        CalendarioService calendarioService) {
                this.asignacionService = asignacionService;
                this.usuarioService = usuarioService;
                this.xpressController = xpressController;
                this.cierreSemanalService = cierreSemanalService;
                this.agenciaService = agenciaService;
                this.procedimientoService = procedimientoService;
                this.comisionService = comisionService;
                this.cierreSemanalConsolidadoV2Service = cierreSemanalConsolidadoV2Service;
                this.calendarioService = calendarioService;
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
                                UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipoAndStatus(
                                                agencia, "Agente",
                                                true) == null ? UsuarioModel.getSinAgenteAsignado()
                                                                : this.usuarioService
                                                                                .findByAgenciaTipoAndStatus(agencia,
                                                                                                "Agente", true);
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
                                                .findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(
                                                                agenteUsuarioModel.getUsuarioId(), anio, semana)
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

        @GetMapping(path = "/by_agencia/{agencia}")
        public @ResponseBody ResponseEntity<?> getV2ByAgenciaAnioAndSemana(
                        @RequestHeader(required = false) String staticToken,
                        @RequestHeader String username,
                        @PathVariable String agencia)
                        throws ExecutionException, InterruptedException {
                CierreSemanalConsolidadoV2DTO cierreSemanalConsolidadoV2DTO = null;
                HttpStatus responseStatus = HttpStatus.OK;
                CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

                // To easy code
                int anio = calendarioModel.getAnio();
                int semana = calendarioModel.getSemana();

                DashboardDTO dashboard;
                List<UsuarioModel> usuarioModels;
                double asignaciones;

                CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model = this.cierreSemanalConsolidadoV2Service
                                .findByAgenciaAnioAndSemana(agencia, anio, semana);
                ComisionModel comisionModel = this.comisionService.findByAgenciaAnioAndSemana(agencia, anio, semana);

                // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
                if (!this.usuarioService.existsByUsuario(username)) {
                        responseStatus = HttpStatus.FORBIDDEN;
                } else if (!this.usuarioService.existsByUsuarioAndStatus(username, true)) {
                        responseStatus = HttpStatus.UNAUTHORIZED;
                } else {
                        if (cierreSemanalConsolidadoV2Model != null) {
                                cierreSemanalConsolidadoV2DTO = CierreSemanalUtil.getCierreSemanalConsolidadoV2DTO(
                                                cierreSemanalConsolidadoV2Model, comisionModel);
                        } else if (this.agenciaService.existsById(agencia)) {
                                dashboard = xpressController.getDashboardByAgenciaAnioAndSemana(agencia, anio, semana)
                                                .getBody();
                                usuarioModels = new ArrayList<>();

                                // To easy code
                                UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipoAndStatus(
                                                agencia, "Agente",
                                                true) == null ? UsuarioModel.getSinAgenteAsignado()
                                                                : this.usuarioService
                                                                                .findByAgenciaTipoAndStatus(agencia,
                                                                                                "Agente", true);
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
                                                .findSumaAsigancionesByQuienEntregoUsuarioIdAnioAndSemana(
                                                                agenteUsuarioModel.getUsuarioId(), anio, semana)
                                                .join();

                                cierreSemanalConsolidadoV2DTO = CierreSemanalUtil.getCierreSemanalConsolidadoV2DTO(
                                                dashboard,
                                                usuarioModels,
                                                asignaciones);
                        } else {
                                return new ResponseEntity<>("La agencia no existe", HttpStatus.BAD_REQUEST);
                        }
                }
                // } else {
                // responseStatus = HttpStatus.BAD_REQUEST;
                // }

                return new ResponseEntity<>(cierreSemanalConsolidadoV2DTO, responseStatus);
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
                // cierreSemanalModel.setPDF(urlPDF);
                // cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

                // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
                if (!this.usuarioService.existsByUsuarioAndTipoIn(username, new String[] { "Gerente", "Seguridad" })) {
                        responseStatus = HttpStatus.FORBIDDEN;
                } else if (!this.usuarioService.existsByUsuarioTipoInAndStatus(username,
                                new String[] { "Gerente", "Seguridad" }, true)) {
                        responseStatus = HttpStatus.UNAUTHORIZED;
                } else {
                        // To easy code
                        String agencia = cierreSemanalDTO.getBalanceAgencia().getAgencia();
                        int anio = cierreSemanalDTO.getAnio();
                        int semana = cierreSemanalDTO.getSemana();

                        if (this.cierreSemanalService.findByAgenciaAnioAndSemana(agencia, anio, semana).isEmpty()) {
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

        @PostMapping(path = "/consolidados_v2/create-one")
        public @ResponseBody ResponseEntity<String> consolidadosV2CreateOne(
                        @RequestBody CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model,
                        @RequestHeader(required = false) String staticToken,
                        @RequestHeader String username)
                        throws DocumentException, FileNotFoundException {
                String responseText = "";
                HttpStatus responseStatus;

                // To easy code
                String agencia = cierreSemanalConsolidadoV2Model.getAgencia();
                int anio = cierreSemanalConsolidadoV2Model.getAnio();
                int semana = cierreSemanalConsolidadoV2Model.getSemana();
                String nombrePDF = agencia + "-" + anio + "-" + semana;
                String urlPDF = "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
                                + nombrePDF + ".pdf";

                // cierreSemanalConsolidadoV2Model.setPDF(urlPDF);
                // cierreSemanalModel.setPDF(urlPDF);
                // cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

                // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
                if (!this.usuarioService.existsByUsuarioAndTipoIn(username, new String[] { "Gerente", "Seguridad" })) {
                        responseStatus = HttpStatus.FORBIDDEN;
                } else if (!this.usuarioService.existsByUsuarioTipoInAndStatus(username,
                                new String[] { "Gerente", "Seguridad" }, true)) {
                        responseStatus = HttpStatus.UNAUTHORIZED;
                } else {
                        if (this.cierreSemanalConsolidadoV2Service.findByAgenciaAnioAndSemana(agencia, anio,
                                        semana) == null) {
                                this.cierreSemanalConsolidadoV2Service.save(cierreSemanalConsolidadoV2Model);

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

                CierreSemanalUtil.subSendCierreSemanalMessageConsolidadoV2(cierreSemanalConsolidadoV2Model);
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

        @GetMapping(path = "/asignaciones_y_gastos/by_usuario-id_gerencia_anio_and_semana/{usuarioId}/{gerencia}/{anio}/{semana}")
        public @ResponseBody ResponseEntity<AsignacionesYGastosDTO> getAsignacionesYGastosByGerenciaAnioAndSemana(
                        @PathVariable Integer usuarioId, @PathVariable String gerencia, @PathVariable int anio,
                        @PathVariable int semana) {
                AsignacionesYGastosDTO asignacionesYGastosDTO = new AsignacionesYGastosDTO(
                                this.procedimientoService.findResumenYBalanceModel(usuarioId, gerencia, semana, anio));

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

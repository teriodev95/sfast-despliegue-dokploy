package tech.calaverita.sfast_xpress.cierre_agencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;

@CrossOrigin
@RestController
@RequestMapping("/api/v2/cierres-agencias")
public final class CierreAgenciaController {
	private final CierreAgenciaService cierreAgenciaService;

	public CierreAgenciaController(CierreAgenciaService cierreAgenciaService) {
		this.cierreAgenciaService = cierreAgenciaService;
	}

	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Version", Constants.VERSION);
		response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
	}

	@GetMapping(path = "/agencia/{agencia}/anio/{anio}/semana/{semana}")
	public @ResponseBody ResponseEntity<?> getByAgenciaAnioAndSemana(
			@RequestHeader(required = false) String staticToken, @RequestHeader String username,
			@PathVariable String agencia, @PathVariable int anio, @PathVariable int semana) {
		return this.cierreAgenciaService.getByUsuarioAgenciaAnioSemana(username, agencia, anio, semana);
	}

	// @GetMapping(path = "/agencia/{agencia}")
	// public @ResponseBody ResponseEntity<?> getV2ByAgenciaAnioAndSemana(
	// @RequestHeader(required = false) String staticToken,
	// @RequestHeader String username,
	// @PathVariable String agencia)
	// throws ExecutionException, InterruptedException {
	// CierreSemanalConsolidadoV2DTO cierreSemanalConsolidadoV2DTO = null;
	// HttpStatus responseStatus = HttpStatus.OK;
	// CalendarioModel calendarioModel =
	// this.calendarioService.findByFechaActual(LocalDate.now().toString());

	// // To easy code
	// int anio = calendarioModel.getAnio();
	// int semana = calendarioModel.getSemana();

	// DashboardDTO dashboard;
	// List<UsuarioModel> usuarioModels;
	// List<AsignacionModel> asignacionModels;

	// CierreAgenciaModel cierreSemanalConsolidadoV2Model =
	// this.cierreSemanalConsolidadoV2Service
	// .findByAgenciaAnioAndSemana(agencia, anio, semana);
	// ComisionModel comisionModel =
	// this.comisionService.findByAgenciaAnioAndSemana(agencia, anio, semana);

	// // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
	// if (!this.usuarioService.existsByUsuario(username)) {
	// responseStatus = HttpStatus.FORBIDDEN;
	// } else if (!this.usuarioService.existsByUsuarioAndStatus(username, true)) {
	// responseStatus = HttpStatus.UNAUTHORIZED;
	// } else {
	// if (cierreSemanalConsolidadoV2Model != null) {
	// cierreSemanalConsolidadoV2DTO =
	// CierreSemanalUtil.getCierreSemanalConsolidadoV2DTO(
	// cierreSemanalConsolidadoV2Model, comisionModel);
	// } else if (this.agenciaService.existsById(agencia)) {
	// dashboard = xpressController.getDashboardByAgenciaAnioAndSemana(agencia,
	// anio, semana)
	// .getBody();
	// usuarioModels = new ArrayList<>();

	// // To easy code
	// UsuarioModel agenteUsuarioModel =
	// this.usuarioService.findByAgenciaTipoAndStatus(
	// agencia, "Agente",
	// true) == null ? UsuarioModel.getSinAgenteAsignado()
	// : this.usuarioService
	// .findByAgenciaTipoAndStatus(agencia,
	// "Agente", true);
	// AgenciaModel agenciaModel = this.agenciaService.findById(agencia);

	// UsuarioModel gerenteUsuarioModel =
	// this.usuarioService.findByGerenciaTipoAndStatus(
	// agenciaModel.getGerenciaId(), "Gerente", true) == null
	// ? UsuarioModel.getSinGerenteAsignado()
	// : this.usuarioService
	// .findByGerenciaTipoAndStatus(
	// agenciaModel.getGerenciaId(),
	// "Gerente", true);

	// usuarioModels.add(agenteUsuarioModel);
	// usuarioModels.add(gerenteUsuarioModel);

	// asignacionModels = this.asignacionService.findByAgenciaAnioSemana(agencia,
	// anio,
	// semana);

	// cierreSemanalConsolidadoV2DTO =
	// CierreSemanalUtil.getCierreSemanalConsolidadoV2DTO(
	// dashboard,
	// usuarioModels,
	// asignacionModels);
	// } else {
	// return new ResponseEntity<>("La agencia no existe", HttpStatus.BAD_REQUEST);
	// }
	// }
	// // } else {
	// // responseStatus = HttpStatus.BAD_REQUEST;
	// // }

	// return new ResponseEntity<>(cierreSemanalConsolidadoV2DTO, responseStatus);
	// }

	// @PostMapping(path = "/create-one")
	// public @ResponseBody ResponseEntity<String> createOne(@RequestBody
	// CierreSemanalDto cierreSemanalDTO,
	// @RequestHeader(required = false) String staticToken,
	// @RequestHeader String username)
	// throws DocumentException, FileNotFoundException {
	// String responseText = "";
	// HttpStatus responseStatus;
	// CierreSemanalModel cierreSemanalModel = this.cierreSemanalService
	// .getCierreSemanalEntity(cierreSemanalDTO);

	// // To easy code
	// String urlPDF =
	// "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
	// + cierreSemanalModel.getId() + ".pdf";

	// cierreSemanalDTO.setPDF(urlPDF);
	// // cierreSemanalModel.setPDF(urlPDF);
	// //
	// cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

	// // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
	// if (!this.usuarioService.existsByUsuarioAndTipoIn(username, new String[] {
	// "Gerente", "Seguridad" })) {
	// responseStatus = HttpStatus.FORBIDDEN;
	// } else if (!this.usuarioService.existsByUsuarioTipoInAndStatus(username,
	// new String[] { "Gerente", "Seguridad" }, true)) {
	// responseStatus = HttpStatus.UNAUTHORIZED;
	// } else {
	// // To easy code
	// String agencia = cierreSemanalDTO.getBalanceAgencia().getAgencia();
	// int anio = cierreSemanalDTO.getAnio();
	// int semana = cierreSemanalDTO.getSemana();

	// if (this.cierreSemanalService.findByAgenciaAnioAndSemana(agencia, anio,
	// semana).isEmpty()) {
	// this.cierreSemanalService.save(cierreSemanalModel);

	// responseText = urlPDF;
	// responseStatus = HttpStatus.CREATED;
	// } else {
	// responseText = "No se pudo registrar el cierre semanal porque ya existe";
	// responseStatus = HttpStatus.CONFLICT;
	// }
	// }
	// // } else {
	// // responseText = "Token inválido";
	// // responseStatus = HttpStatus.BAD_REQUEST;
	// // }

	// CierreSemanalUtil.subSendCierreSemanalMessage(cierreSemanalDTO);
	// // CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);

	// return new ResponseEntity<>(responseText, responseStatus);
	// }

	// @PostMapping(path = "/create-one")
	// public @ResponseBody ResponseEntity<String> consolidadosV2CreateOne(
	// @RequestBody CierreAgenciaModel cierreSemanalConsolidadoV2Model,
	// @RequestHeader(required = false) String staticToken,
	// @RequestHeader String username)
	// throws DocumentException, FileNotFoundException {
	// String responseText = "";
	// HttpStatus responseStatus;

	// // To easy code
	// String agencia = cierreSemanalConsolidadoV2Model.getAgencia();
	// int anio = cierreSemanalConsolidadoV2Model.getAnio();
	// int semana = cierreSemanalConsolidadoV2Model.getSemana();
	// String nombrePDF = agencia + "-" + anio + "-" + semana;
	// String urlPDF =
	// "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/pdf/"
	// + nombrePDF + ".pdf";

	// // cierreSemanalConsolidadoV2Model.setPDF(urlPDF);
	// // cierreSemanalModel.setPDF(urlPDF);
	// //
	// cierreSemanalModel.setLog(LogUtil.getLogCierreSemanal(cierreSemanalDTO.getBalanceAgencia()));

	// // if (staticToken.equals("c4u&S7HizL5!PU$5c2gwYastgMs5%RUViAbK")) {
	// if (!this.usuarioService.existsByUsuarioAndTipoIn(username, new String[] {
	// "Gerente", "Seguridad" })) {
	// responseStatus = HttpStatus.FORBIDDEN;
	// } else if (!this.usuarioService.existsByUsuarioTipoInAndStatus(username,
	// new String[] { "Gerente", "Seguridad" }, true)) {
	// responseStatus = HttpStatus.UNAUTHORIZED;
	// } else {
	// if
	// (this.cierreSemanalConsolidadoV2Service.findByAgenciaAnioAndSemana(agencia,
	// anio,
	// semana) == null) {
	// this.cierreSemanalConsolidadoV2Service.save(cierreSemanalConsolidadoV2Model);

	// responseText = urlPDF;
	// responseStatus = HttpStatus.CREATED;
	// } else {
	// responseText = "No se pudo registrar el cierre semanal porque ya existe";
	// responseStatus = HttpStatus.CONFLICT;
	// }
	// }
	// // } else {
	// // responseText = "Token inválido";
	// // responseStatus = HttpStatus.BAD_REQUEST;
	// // }

	// CierreSemanalUtil.subSendCierreSemanalMessageConsolidadoV2(cierreSemanalConsolidadoV2Model);
	// // CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);

	// return new ResponseEntity<>(responseText, responseStatus);
	// }

	// @GetMapping(path = "/pdf/{file}", produces = MediaType.APPLICATION_PDF_VALUE)
	// public @ResponseBody ResponseEntity<InputStreamResource>
	// getPdfByRuta(@PathVariable String file)
	// throws IOException {
	// FileInputStream fileInputStream = new
	// FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

	// return new ResponseEntity<>(new InputStreamResource(fileInputStream),
	// HttpStatus.OK);
	// }

	// @PostMapping(path = "/desgloce-cobranza-y-comisiones/pdf/create-one")
	// public @ResponseBody ResponseEntity<String>
	// desgloceCobranzaYComisionesCreateOne(
	// @RequestBody DataCierreDTO dataCierreDTO)
	// throws DocumentException, FileNotFoundException {
	// TablaDetallesCierreAgencias tablaDetallesCierreAgencias = dataCierreDTO
	// .getTablaDetallesCierreAgencias();

	// String idPDF = "CIERRE_" + tablaDetallesCierreAgencias.getZona() + "_SEM"
	// + tablaDetallesCierreAgencias.getSemana() + "_"
	// + tablaDetallesCierreAgencias.getAnio();
	// String urlPDF =
	// "https://sfast-api.terio.xyz/xpress/v1/pwa/cierres_semanales/desgloce_cobranza_y_comisiones/pdf/"
	// + idPDF + ".pdf";

	// DesgloceCobranzaYComisionesUtil.createCierreSemanalPDF(dataCierreDTO, idPDF);

	// return new ResponseEntity<>(urlPDF, HttpStatus.OK);
	// }

	// @GetMapping(path = "/desgloce-cobranza-y-comisiones/pdf/{file}", produces =
	// MediaType.APPLICATION_PDF_VALUE)
	// public @ResponseBody ResponseEntity<InputStreamResource>
	// getPdfDesgloceCobranzaYComisionesByRuta(
	// @PathVariable String file)
	// throws IOException {
	// FileInputStream fileInputStream = new
	// FileInputStream(Constants.RUTA_PDF_PRODUCCION + file);

	// return new ResponseEntity<>(new InputStreamResource(fileInputStream),
	// HttpStatus.OK);
	// }

	// @GetMapping(path =
	// "/agencia/{agencia}/anio/{anio}/semana/{semana}/nivel/{nivel}")
	// public @ResponseBody ResponseEntity<ComisionCobranza>
	// getComisionCobranzaByAgenciaAnioSemanaAndNivel(
	// @PathVariable String agencia, @PathVariable int anio, @PathVariable int
	// semana,
	// @PathVariable String nivel)
	// throws ExecutionException, InterruptedException {
	// HashMap<String, Integer> porcentajesComision = new HashMap<>();
	// porcentajesComision.put("SILVER", 7);
	// porcentajesComision.put("GOLD", 8);
	// porcentajesComision.put("PLATINUM", 9);
	// porcentajesComision.put("DIAMOND", 10);

	// int porcentajeComision = porcentajesComision.get(nivel);

	// double cobranzaTotal =
	// CierreSemanalUtil.getCobranzaTotalByAgenciaAnioAndSemana(agencia, anio,
	// semana);

	// ComisionCobranza comisionCobranza = new ComisionCobranza(porcentajeComision,
	// cobranzaTotal / 100 * porcentajeComision);

	// return new ResponseEntity<>(comisionCobranza, HttpStatus.OK);
	// }

	// @GetMapping(path =
	// "/asignaciones-y-gastos/usuarioId/{usuarioId}/gerencia/{gerencia}/anio/{anio}/semana/{semana}")
	// public @ResponseBody ResponseEntity<AsignacionesYGastosDTO>
	// getAsignacionesYGastosByGerenciaAnioAndSemana(
	// @PathVariable Integer usuarioId, @PathVariable String gerencia, @PathVariable
	// int anio,
	// @PathVariable int semana) {
	// AsignacionesYGastosDTO asignacionesYGastosDTO = new AsignacionesYGastosDTO(
	// this.procedimientoService.findResumenYBalanceModel(usuarioId, gerencia,
	// semana, anio));

	// return new ResponseEntity<>(asignacionesYGastosDTO, HttpStatus.OK);
	// }

	// @Data
	// public static class ComisionCobranza {
	// private Integer porcentajeComisionCobranza;
	// private Double pagoComisionCobranza;

	// public ComisionCobranza(Integer porcentajeComisionCobranza, Double
	// pagoComisionCobranza) {
	// this.porcentajeComisionCobranza = porcentajeComisionCobranza;
	// this.pagoComisionCobranza = pagoComisionCobranza;
	// }
	// }
}

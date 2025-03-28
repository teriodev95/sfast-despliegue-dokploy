package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.DTOs.flujo_efectivo.FlujoEfectivoDto;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.GastoService;
import tech.calaverita.sfast_xpress.services.IncidenteReposicionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/flujos-efectivo")
public class FlujoEfectivoController {
	private final AsignacionService asignacionService;
	private final CalendarioService calendarioService;
	private final GastoService gastoService;
	private final IncidenteReposicionService incidenteReposicionService;
	private final PagoDynamicService pagoDynamicService;
	private final VentaService ventaService;
	private final UsuarioService usuarioService;

	public FlujoEfectivoController(AsignacionService asignacionService, CalendarioService calendarioService,
			GastoService gastoService, IncidenteReposicionService incidenteReposicionService,
			PagoDynamicService pagoDynamicService, VentaService ventaService,
			UsuarioService usuarioService) {
		this.asignacionService = asignacionService;
		this.calendarioService = calendarioService;
		this.gastoService = gastoService;
		this.incidenteReposicionService = incidenteReposicionService;
		this.pagoDynamicService = pagoDynamicService;
		this.ventaService = ventaService;
		this.usuarioService = usuarioService;
	}

	@GetMapping(path = "/gerencia/{gerencia}")
	public ResponseEntity<FlujoEfectivoDto> getFlujoEfectivoGerencia(@PathVariable String gerencia) {
		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();

		CompletableFuture<List<AsignacionModel>> recibioAsignacionModelsCF = this.asignacionService
				.findAsignacionesIngresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<AsignacionModel>> entregoAsignacionModelsCF = this.asignacionService
				.findAsignacionesEgresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<GastoModel>> gastoModelsCF = this.gastoService
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<IncidenteReposicionModel>> incidenteReposicionModelsCF = this.incidenteReposicionService
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<PagoDynamicModel>> pagoDynamicModelsCf = this.pagoDynamicService
				.findByGerenciaAnioSemanaStatusAsync(gerencia, anio, semana, "Vacante");
		CompletableFuture<List<VentaModel>> ventaModelsCF = this.ventaService
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);

		AlmacenObjects almacenObjects = new AlmacenObjects();
		almacenObjects.addObject("recibioAsignacionModels", recibioAsignacionModelsCF.join());
		almacenObjects.addObject("entregoAsignacionModels", entregoAsignacionModelsCF.join());
		almacenObjects.addObject("gastoModels", gastoModelsCF.join());
		almacenObjects.addObject("incidenteReposicionModels", incidenteReposicionModelsCF.join());
		almacenObjects.addObject("pagoDynamicModels", pagoDynamicModelsCf.join());
		almacenObjects.addObject("ventaModels", ventaModelsCF.join());

		return new ResponseEntity<>(new FlujoEfectivoDto(almacenObjects, gerencia), HttpStatus.OK);
	}

	@GetMapping(path = "/usuarioId/{usuarioId}")
	public ResponseEntity<?> getFlujoEfectivoGerencia(@PathVariable Integer usuarioId) {
		if (!this.usuarioService.existsByUsuarioIdAndStatus(usuarioId, true)) {
			HashMap<String, String> responseHm = new HashMap<>();
			responseHm.put("error", "Recurso no encontrado");
			responseHm.put("mensaje", "No existe el registro solicitado o está inactivo");
			return new ResponseEntity<>(responseHm, HttpStatus.NOT_FOUND);
		}

		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();

		CompletableFuture<List<AsignacionModel>> recibioAsignacionModelsCF = this.asignacionService
				.findAsignacionesIngresoByUsuarioIdAnioSemanaAsync(usuarioId, anio, semana);
		CompletableFuture<List<AsignacionModel>> entregoAsignacionModelsCF = this.asignacionService
				.findAsignacionesEgresoByUsuarioIdAnioSemanaAsync(usuarioId, anio, semana);
		CompletableFuture<List<GastoModel>> gastoModelsCF = this.gastoService
				.findByUsuarioIdAnioSemanaAsync(usuarioId, anio, semana);
		CompletableFuture<List<IncidenteReposicionModel>> incidenteReposicionModelsCF = this.incidenteReposicionService
				.findByUsuarioIdAnioSemanaAsync(usuarioId, anio, semana);

		AlmacenObjects almacenObjects = new AlmacenObjects();
		almacenObjects.addObject("recibioAsignacionModels", recibioAsignacionModelsCF.join());
		almacenObjects.addObject("entregoAsignacionModels", entregoAsignacionModelsCF.join());
		almacenObjects.addObject("gastoModels", gastoModelsCF.join());
		almacenObjects.addObject("incidenteReposicionModels", incidenteReposicionModelsCF.join());

		return new ResponseEntity<>(new FlujoEfectivoDto(almacenObjects, usuarioId), HttpStatus.OK);
	}
}

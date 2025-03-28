package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal.DetalleCierreSemanalDto;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.pojos.CobranzaGerencia;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.CierreSemanalConsolidadoV2Service;
import tech.calaverita.sfast_xpress.services.ComisionService;
import tech.calaverita.sfast_xpress.services.GastoService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.IncidenteReposicionService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v2/detalles-cierres-semanales")
public class DetalleCierreSemanalController {
	private final AsignacionService asignacionService;
	private final CalendarioService calendarioService;
	private final CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service;
	private final ComisionService comisionService;
	private final GastoService gastoService;
	private final GerenciaService gerenciaService;
	private final IncidenteReposicionService incidenteReposicionService;
	private final PagoDynamicService pagoDynamicService;
	private final VentaService ventaService;

	public DetalleCierreSemanalController(AsignacionService asignacionService, CalendarioService calendarioService,
			CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service,
			ComisionService comisionService, GastoService gastoService, GerenciaService gerenciaService,
			IncidenteReposicionService incidenteReposicionService, PagoDynamicService pagoDynamicService,
			VentaService ventaService) {
		this.asignacionService = asignacionService;
		this.calendarioService = calendarioService;
		this.cierreSemanalConsolidadoV2Service = cierreSemanalConsolidadoV2Service;
		this.comisionService = comisionService;
		this.gastoService = gastoService;
		this.gerenciaService = gerenciaService;
		this.incidenteReposicionService = incidenteReposicionService;
		this.pagoDynamicService = pagoDynamicService;
		this.ventaService = ventaService;
	}

	@GetMapping(path = "/gerencia/{gerencia}")
	public ResponseEntity<DetalleCierreSemanalDto> getByGerencia(@PathVariable String gerencia) {
		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());
		GerenciaModel gerenciaModel = this.gerenciaService.findById(gerencia);

		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();
		String deprecatedNameGerencia = gerenciaModel.getDeprecatedName();
		String sucursal = gerenciaModel.getSucursal();

		CompletableFuture<List<AsignacionModel>> ingresosAsignacionModelCf = this.asignacionService
				.findAsignacionesIngresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<AsignacionModel>> egresosAsignacionModelCf = this.asignacionService
				.findAsignacionesEgresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<AsignacionModel>> egresosAgenteAsignacionModelCf = this.asignacionService
				.findAsignacionesEgresoAgentesByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<CierreSemanalConsolidadoV2Model>> cierreSemanalConsolidadoV2ModelsCf = this.cierreSemanalConsolidadoV2Service
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<ComisionModel>> comisionModelsCf = this.comisionService
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<GastoModel>> gastoModelsCf = this.gastoService.findByGerenciaAnioSemanaAsync(gerencia,
				anio, semana);
		CompletableFuture<List<IncidenteReposicionModel>> incidenteReposicionModelsCf = this.incidenteReposicionService
				.findByCategoriaNotGerenciaAnioSemanaAsync("reposicion", gerencia, anio, semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> pagoDynamicModelsCf = this.pagoDynamicService
				.findByGerenciaSucursalAnioSemanaAndEsPrimerPago(deprecatedNameGerencia, sucursal, anio,
						semana, false);
		CompletableFuture<List<VentaModel>> ventaModelsCf = this.ventaService.findByGerenciaAnioSemanaAsync(
				gerencia,
				anio, semana);

		CompletableFuture.allOf(ingresosAsignacionModelCf, egresosAsignacionModelCf,
				egresosAgenteAsignacionModelCf, cierreSemanalConsolidadoV2ModelsCf, comisionModelsCf,
				gastoModelsCf, incidenteReposicionModelsCf, pagoDynamicModelsCf, ventaModelsCf);

		egresosAsignacionModelCf.join().addAll(egresosAgenteAsignacionModelCf.join());

		CobranzaGerencia cobranzaGerencia = new CobranzaGerencia(pagoDynamicModelsCf.join(),
				ventaModelsCf.join());

		AlmacenObjects almacenObjects = new AlmacenObjects();
		almacenObjects.addObject("ingresosAsignacionModel", ingresosAsignacionModelCf.join());
		almacenObjects.addObject("egresosAsignacionModel", egresosAsignacionModelCf.join());
		almacenObjects.addObject("cierreSemanalConsolidadoV2Models", cierreSemanalConsolidadoV2ModelsCf.join());
		almacenObjects.addObject("cobranzaGerencia", cobranzaGerencia);
		almacenObjects.addObject("comisionModels", comisionModelsCf.join());
		almacenObjects.addObject("gastoModels", gastoModelsCf.join());
		almacenObjects.addObject("incidenteReposicionModels", incidenteReposicionModelsCf.join());
		almacenObjects.addObject("ventaModels", ventaModelsCf.join());

		DetalleCierreSemanalDto detalleCierreSemanalDto = new DetalleCierreSemanalDto(almacenObjects);

		return new ResponseEntity<>(detalleCierreSemanalDto, HttpStatus.OK);
	}

	@GetMapping(path = "/gerencia/{gerencia}/semana/{semana}")
	public ResponseEntity<DetalleCierreSemanalDto> getByGerenciaSemana(@PathVariable String gerencia,
			@PathVariable int semana) {
		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());
		GerenciaModel gerenciaModel = this.gerenciaService.findById(gerencia);

		// To easy code
		int anio = calendarioModel.getAnio();
		String deprecatedNameGerencia = gerenciaModel.getDeprecatedName();
		String sucursal = gerenciaModel.getSucursal();

		CompletableFuture<List<AsignacionModel>> ingresosAsignacionModelCf = this.asignacionService
				.findAsignacionesIngresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<AsignacionModel>> egresosAsignacionModelCf = this.asignacionService
				.findAsignacionesEgresoByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<AsignacionModel>> egresosAgenteAsignacionModelCf = this.asignacionService
				.findAsignacionesEgresoAgentesByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<CierreSemanalConsolidadoV2Model>> cierreSemanalConsolidadoV2ModelsCf = this.cierreSemanalConsolidadoV2Service
				.findByGerenciaAnioSemanaAsync(gerencia, anio, semana);
		CompletableFuture<List<ComisionModel>> comisionModelsCf = this.comisionService
				.findByGerenciaAnioSemanaAsync(
						gerencia,
						anio, semana);
		CompletableFuture<List<GastoModel>> gastoModelsCf = this.gastoService.findByGerenciaAnioSemanaAsync(
				gerencia,
				anio, semana);
		CompletableFuture<List<IncidenteReposicionModel>> incidenteReposicionModelsCf = this.incidenteReposicionService
				.findByCategoriaNotGerenciaAnioSemanaAsync("reposicion", gerencia, anio, semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> pagoDynamicModelsCf = this.pagoDynamicService
				.findByGerenciaSucursalAnioSemanaAndEsPrimerPago(deprecatedNameGerencia, sucursal, anio,
						semana, false);
		CompletableFuture<List<VentaModel>> ventaModelsCf = this.ventaService.findByGerenciaAnioSemanaAsync(
				gerencia,
				anio, semana);

		CompletableFuture.allOf(ingresosAsignacionModelCf, egresosAsignacionModelCf,
				egresosAgenteAsignacionModelCf, cierreSemanalConsolidadoV2ModelsCf, comisionModelsCf,
				gastoModelsCf, incidenteReposicionModelsCf, pagoDynamicModelsCf, ventaModelsCf);

		egresosAsignacionModelCf.join().addAll(egresosAgenteAsignacionModelCf.join());

		CobranzaGerencia cobranzaGerencia = new CobranzaGerencia(pagoDynamicModelsCf.join(),
				ventaModelsCf.join());

		AlmacenObjects almacenObjects = new AlmacenObjects();
		almacenObjects.addObject("ingresosAsignacionModel", ingresosAsignacionModelCf.join());
		almacenObjects.addObject("egresosAsignacionModel", egresosAsignacionModelCf.join());
		almacenObjects.addObject("cierreSemanalConsolidadoV2Models", cierreSemanalConsolidadoV2ModelsCf.join());
		almacenObjects.addObject("cobranzaGerencia", cobranzaGerencia);
		almacenObjects.addObject("comisionModels", comisionModelsCf.join());
		almacenObjects.addObject("gastoModels", gastoModelsCf.join());
		almacenObjects.addObject("incidenteReposicionModels", incidenteReposicionModelsCf.join());
		almacenObjects.addObject("ventaModels", ventaModelsCf.join());

		DetalleCierreSemanalDto detalleCierreSemanalDto = new DetalleCierreSemanalDto(almacenObjects);

		return new ResponseEntity<>(detalleCierreSemanalDto, HttpStatus.OK);
	}
}

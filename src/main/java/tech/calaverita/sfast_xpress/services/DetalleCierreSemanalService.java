package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;

@Service
public class DetalleCierreSemanalService {
	private final AsignacionService asignacionService;
	private final CalendarioService calendarioService;
	private final CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service;
	private final ComisionService comisionService;
	private final GastoService gastoService;
	private final GerenciaService gerenciaService;
	private final IncidenteReposicionService incidenteReposicionService;
	private final PagoDynamicService pagoDynamicService;
	private final VentaService ventaService;

	public DetalleCierreSemanalService(AsignacionService asignacionService, CalendarioService calendarioService,
			CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service,
			ComisionService comisionService,
			GastoService gastoService, GerenciaService gerenciaService,
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

	public DetalleCierreSemanalDto getDetalleCierreSemanalByGerencia(String gerencia) {
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
				.findByCategoriaGerenciaAnioSemanaAsync("incidente", gerencia, anio, semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> pagoDynamicModelsCf = this.pagoDynamicService
				.findByGerenciaSucursalAnioSemanaAndEsPrimerPago(deprecatedNameGerencia, sucursal, anio,
						semana, false);
		CompletableFuture<List<VentaModel>> ventaModelsCf = this.ventaService.findByGerenciaAnioSemanaAsync(
				gerencia,
				anio, semana);

		CompletableFuture.allOf(ingresosAsignacionModelCf, egresosAsignacionModelCf,
				cierreSemanalConsolidadoV2ModelsCf, comisionModelsCf,
				gastoModelsCf, incidenteReposicionModelsCf, pagoDynamicModelsCf, ventaModelsCf);

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

		return detalleCierreSemanalDto;
	}

	@Async("asyncExecutor")
	public CompletableFuture<DetalleCierreSemanalDto> getDetalleCierreSemanalByGerenciaAsync(String gerencia) {
		return CompletableFuture.completedFuture(getDetalleCierreSemanalByGerencia(gerencia));
	}
}

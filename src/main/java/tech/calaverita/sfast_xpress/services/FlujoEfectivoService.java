package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import tech.calaverita.sfast_xpress.DTOs.flujo_efectivo.FlujoEfectivoDto;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.models.mariaDB.IncidenteReposicionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;

@Service
public class FlujoEfectivoService {
	private final AsignacionService asignacionService;
	private final CalendarioService calendarioService;
	private final GastoService gastoService;
	private final IncidenteReposicionService incidenteReposicionService;
	private final PagoDynamicService pagoDynamicService;
	private final VentaService ventaService;

	public FlujoEfectivoService(AsignacionService asignacionService, CalendarioService calendarioService,
			GastoService gastoService, IncidenteReposicionService incidenteReposicionService,
			PagoDynamicService pagoDynamicService, VentaService ventaService) {
		this.asignacionService = asignacionService;
		this.calendarioService = calendarioService;
		this.gastoService = gastoService;
		this.incidenteReposicionService = incidenteReposicionService;
		this.pagoDynamicService = pagoDynamicService;
		this.ventaService = ventaService;
	}

	public FlujoEfectivoDto getFlujoEfectivoByGerencia(String gerencia) {
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

		return new FlujoEfectivoDto(almacenObjects, gerencia);
	}

	@Async("asyncExecutor")
	public CompletableFuture<FlujoEfectivoDto> getFlujoEfectivoByGerenciaAsync(
			@PathVariable String gerencia) {
		return CompletableFuture
				.completedFuture(getFlujoEfectivoByGerencia(gerencia));
	}
}

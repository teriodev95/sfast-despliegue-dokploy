package tech.calaverita.sfast_xpress.dashboard_agencia;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.CierreDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.DebitosCobranzaAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.InfoCobranzaAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.LiquidacionesDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.PagosDashboardAgencia;
import tech.calaverita.sfast_xpress.dashboard_agencia.pojo.VentasDashboardAgencia;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.LiquidacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.GerenciaService;
import tech.calaverita.sfast_xpress.services.LiquidacionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;

@Service
public class DashboardAgenciaService {
	private final AgenciaService agenciaService;
	private final AsignacionService asignacionService;
	private final GerenciaService gerenciaService;
	private final PagoDynamicService pagoAgrupadoService;
	private final PrestamoViewService prestamoViewService;
	private final LiquidacionService liquidacionService;
	private final UsuarioService usuarioService;
	private final VentaService ventaService;

	public DashboardAgenciaService(AgenciaService agenciaService, AsignacionService asignacionService,
			GerenciaService gerenciaService, PagoDynamicService pagoAgrupadoService,
			PrestamoViewService prestamoViewService, LiquidacionService liquidacionService,
			UsuarioService usuarioService, VentaService ventaService) {
		this.agenciaService = agenciaService;
		this.asignacionService = asignacionService;
		this.gerenciaService = gerenciaService;
		this.pagoAgrupadoService = pagoAgrupadoService;
		this.prestamoViewService = prestamoViewService;
		this.liquidacionService = liquidacionService;
		this.usuarioService = usuarioService;
		this.ventaService = ventaService;
	}

	public ResponseEntity<DashboardAgenciaDto> getByAgenciaAnioSemana(String agencia, int anio, int semana) {
		UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipo(agencia, "Agente");

		CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
				.findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D, anio,
						semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
				.findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio, semana, false);
		CompletableFuture<ArrayList<LiquidacionModel>> liquidacionModels = this.liquidacionService
				.findByAgenciaAnioAndSemana(agencia, anio, semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> liquidacionesPagoModels = this.liquidacionService
				.findPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);
		CompletableFuture<String> statusAgencia = this.agenciaService.findStatusById(agencia);

		if (prestamoViewModels.join().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		CompletableFuture<Double> asignaciones = CompletableFuture.completedFuture(0D);
		if (agenteUsuarioModel != null) {
			asignaciones = this.asignacionService
					.findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
							agenteUsuarioModel.getUsuarioId(), anio, semana);
		}

		// To easy code
		String gerencia = prestamoViewModels.join().get(0).getGerencia();
		String sucursal = prestamoViewModels.join().get(0).getSucursal();

		CompletableFuture<GerenciaModel> gerenciaModel = this.gerenciaService
				.findByDeprecatedNameAndSucursal(gerencia, sucursal);

		InfoCobranzaAgencia infoCobranzaAgencia = new InfoCobranzaAgencia()
				.setGerencia(gerenciaModel.join().getGerenciaId())
				.setAgencia(agencia)
				.setAnio(anio)
				.setSemana(semana)
				.setClientes(prestamoViewModels.join().size());
		DebitosCobranzaAgencia debitosCobranzaAgencia = new DebitosCobranzaAgencia(prestamoViewModels.join());
		LiquidacionesDashboardAgencia liquidacionesDashboardAgencia = new LiquidacionesDashboardAgencia(
				liquidacionModels.join(), pagoAgrupagoModels.join(), liquidacionesPagoModels.join());
		PagosDashboardAgencia pagosDashboardAgencia = new PagosDashboardAgencia(pagoAgrupagoModels.join(),
				liquidacionesDashboardAgencia.getLiquidaciones(), debitosCobranzaAgencia.getDebitoTotal());
		CierreDashboardAgencia cierreDashboardAgencia = new CierreDashboardAgencia(pagosDashboardAgencia,
				debitosCobranzaAgencia, asignaciones.join())
				.setStatusAgencia(statusAgencia.join());
		VentasDashboardAgencia ventasDashboardAgencia = new VentasDashboardAgencia(
				this.ventaService.findByAgenciaAnioAndSemana(agencia, anio, semana));

		DashboardAgenciaDto dashboardAgenciaDto = new DashboardAgenciaDto()
				.cierreDashboardAgencia(cierreDashboardAgencia)
				.liquidacionesDashboardAgencia(liquidacionesDashboardAgencia)
				.pagosDashboardAgencia(pagosDashboardAgencia)
				.debitosCobranzaAgencia(debitosCobranzaAgencia)
				.infoCobranzaAgencia(infoCobranzaAgencia)
				.ventasDashboardAgencia(ventasDashboardAgencia);

		return new ResponseEntity<>(dashboardAgenciaDto, HttpStatus.OK);
	}

	@Async("asyncExecutor")
	public CompletableFuture<ResponseEntity<DashboardAgenciaDto>> getByAgenciaAnioSemanaAsync(String agencia, int anio,
			int semana) {
		UsuarioModel agenteUsuarioModel = this.usuarioService.findByAgenciaTipo(agencia, "Agente");

		CompletableFuture<ArrayList<PrestamoViewModel>> prestamoViewModels = this.prestamoViewService
				.findByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D, anio,
						semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> pagoAgrupagoModels = this.pagoAgrupadoService
				.findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio, semana, false);
		CompletableFuture<ArrayList<LiquidacionModel>> liquidacionModels = this.liquidacionService
				.findByAgenciaAnioAndSemana(agencia, anio, semana);
		CompletableFuture<ArrayList<PagoDynamicModel>> liquidacionesPagoModels = this.liquidacionService
				.findPagoModelsByAgenciaAnioAndSemana(agencia, anio, semana);
		CompletableFuture<String> statusAgencia = this.agenciaService.findStatusById(agencia);

		if (prestamoViewModels.join().isEmpty()) {
			return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.NO_CONTENT));
		}

		CompletableFuture<Double> asignaciones = CompletableFuture.completedFuture(0D);
		if (agenteUsuarioModel != null) {
			asignaciones = this.asignacionService
					.findSumaAsignacionesByQuienEntregoUsuarioIdAnioAndSemana(
							agenteUsuarioModel.getUsuarioId(), anio, semana);
		}

		// To easy code
		String gerencia = prestamoViewModels.join().get(0).getGerencia();
		String sucursal = prestamoViewModels.join().get(0).getSucursal();

		CompletableFuture<GerenciaModel> gerenciaModel = this.gerenciaService
				.findByDeprecatedNameAndSucursal(gerencia, sucursal);

		InfoCobranzaAgencia infoCobranzaAgenciaDto = new InfoCobranzaAgencia()
				.setGerencia(gerenciaModel.join().getGerenciaId())
				.setAgencia(agencia)
				.setAnio(anio)
				.setSemana(semana)
				.setClientes(prestamoViewModels.join().size());
		DebitosCobranzaAgencia debitosCobranzaAgencia = new DebitosCobranzaAgencia(prestamoViewModels.join());
		LiquidacionesDashboardAgencia liquidacionesDashboardAgencia = new LiquidacionesDashboardAgencia(
				liquidacionModels.join(), pagoAgrupagoModels.join(), liquidacionesPagoModels.join());
		PagosDashboardAgencia pagosDashboardAgencia = new PagosDashboardAgencia(pagoAgrupagoModels.join(),
				liquidacionesDashboardAgencia.getLiquidaciones(), debitosCobranzaAgencia.getDebitoTotal());
		CierreDashboardAgencia cierreDashboardAgencia = new CierreDashboardAgencia(pagosDashboardAgencia,
				debitosCobranzaAgencia, asignaciones.join())
				.setStatusAgencia(statusAgencia.join());
		VentasDashboardAgencia ventasDashboardAgencia = new VentasDashboardAgencia(
				this.ventaService.findByAgenciaAnioAndSemana(agencia, anio, semana));

		DashboardAgenciaDto dashboardAgenciaDto = new DashboardAgenciaDto()
				.cierreDashboardAgencia(cierreDashboardAgencia)
				.liquidacionesDashboardAgencia(liquidacionesDashboardAgencia)
				.pagosDashboardAgencia(pagosDashboardAgencia)
				.debitosCobranzaAgencia(debitosCobranzaAgencia)
				.infoCobranzaAgencia(infoCobranzaAgenciaDto)
				.ventasDashboardAgencia(ventasDashboardAgencia);

		return CompletableFuture.completedFuture(new ResponseEntity<>(dashboardAgenciaDto, HttpStatus.OK));
	}
}

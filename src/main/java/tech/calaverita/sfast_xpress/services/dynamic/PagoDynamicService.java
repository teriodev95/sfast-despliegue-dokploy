package tech.calaverita.sfast_xpress.services.dynamic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.repositories.dynamic.PagoDynamicRepository;
import tech.calaverita.sfast_xpress.services.CalendarioService;

@Service
public class PagoDynamicService {
	private final PagoDynamicRepository repo;
	private final CalendarioService calendarioService;

	public PagoDynamicService(PagoDynamicRepository repo, CalendarioService calendarioService) {
		this.repo = repo;
		this.calendarioService = calendarioService;
	}

	@Async("asyncExecutor")
	public CompletableFuture<ArrayList<PagoDynamicModel>> findByAgenciaAnioSemanaAndEsPrimerPago(String agencia,
			int anio, int semana,
			boolean esPrimerPago) {
		return CompletableFuture.completedFuture(this.repo.findByAgenciaAndAnioAndSemanaAndEsPrimerPago(agencia,
				anio, semana, esPrimerPago));
	}

	@Async("asyncExecutor")
	public CompletableFuture<ArrayList<PagoDynamicModel>> findByGerenciaSucursalAnioSemanaAndEsPrimerPago(
			String gerencia,
			String sucursal,
			int anio, int semana,
			boolean esPrimerPago) {
		return CompletableFuture.completedFuture(
				this.repo.findByGerenciaAndSucursalAndAnioAndSemanaAndEsPrimerPago(gerencia, sucursal,
						anio, semana, esPrimerPago));
	}

	@Async("asyncExecutor")
	public CompletableFuture<List<PagoDynamicModel>> findByGerenciaAnioSemanaStatusAsync(String gerencia, int anio,
			int semana, String status) {
		return CompletableFuture.completedFuture(
				this.repo.findByGerenciaAndAnioAndSemanaAndStatus(gerencia, anio,
						semana, status));
	}

	@Async("asyncExecutor")
	public CompletableFuture<List<PagoDynamicModel>> findByGerenciaAnioSemanaEsPrimerPagoAsync(String gerencia,
			int anio, int semana, boolean esPrimerPago) {
		return CompletableFuture.completedFuture(
				this.repo.findByGerenciaAndAnioAndSemanaAndEsPrimerPago(gerencia, anio,
						semana, esPrimerPago));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findDebitoTotalByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findDebitoTotalByAgenciaAndAnioAndSemana(agencia, anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findExcedenteByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findExcedenteByAgenciaAndAnioAndSemana(agencia, anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findCobranzaTotalByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findCobranzaTotalByAgenciaAndAnioAndSemana(agencia, anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Integer> findClientesCobradosByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findClientesCobradosByAgenciaAndAnioAndSemana(agencia, anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Integer> findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		CompletableFuture<Integer> clientesPagoCompletoCF = CompletableFuture.completedFuture(this.repo
				.findClientesPagoCompletoByAgenciaAndAnioAndSemana(agencia, anio, semana));

		return clientesPagoCompletoCF.join() != null ? clientesPagoCompletoCF
				: CompletableFuture.completedFuture(0);
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findCobranzaPuraByAgenciaAnioSemanaAndFechaPagoLessThanEqual(String agencia,
			int anio,
			int semana, String fechaPago) {
		CompletableFuture<Double> cobranzaPuraCF = CompletableFuture.completedFuture(this.repo
				.findCobranzaPuraByAgenciaAndAnioAndSemanaAndFechaPagoLessThanEqual(agencia, anio,
						semana, fechaPago));

		return cobranzaPuraCF.join() != null ? cobranzaPuraCF : CompletableFuture.completedFuture(0D);
	}

	@Async("asyncExecutor")
	public CompletableFuture<Integer> findClientesTotalesByAgenciaAnioAndSemanaAsync(String agencia, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findClientesTotalesByAgenciaAndAnioAndSemana(agencia, anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findDebitoTotalSemanaByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
			String sucursal,
			int anio, int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findDebitoTotalSemanaByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(
						gerencia, sucursal,
						anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findExcedenteByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
			String sucursal,
			int anio, int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findExcedenteByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(gerencia,
						sucursal, anio,
						semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findCobranzaTotalByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
			String sucursal, int anio,
			int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(gerencia,
						sucursal, anio,
						semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findDebitoTotalParcialByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
			String sucursal,
			int anio, int semana) {
		return CompletableFuture.completedFuture(this.repo
				.findDebitoTotalParcialByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(
						gerencia, sucursal,
						anio, semana));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findExcedenteByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
			String sucursal,
			int anio, int semana,
			String fecha) {
		return CompletableFuture.completedFuture(
				this.repo.findExcedenteByGerenciaAndSucursalAndAnioAndFechaInnerJoinPrestamoModel(
						gerencia, sucursal, anio,
						semana, fecha));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findCobranzaTotalByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
			String sucursal,
			int anio, int semana,
			String fecha) {
		return CompletableFuture.completedFuture(this.repo
				.findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(
						gerencia, sucursal,
						anio, semana, fecha));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Integer> findClientesCobradosByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
			String sucursal,
			int anio,
			int semana,
			String fecha) {
		return CompletableFuture.completedFuture(this.repo
				.findClientesCobradosByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(
						gerencia,
						sucursal, anio, semana, fecha));
	}

	@Async("asyncExecutor")
	public CompletableFuture<Double> findCobranzaPuraByAgenciaAnioSemanaAlJueves(String agencia, int anio, int semana) {
		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());
		String fechaJueves = LocalDate.parse(calendarioModel.getDesde()).plusDays(1) + " 23:59:59";

		Double cobranzaPura = this.repo.findCobranzaPuraByAgenciaAndAnioAndSemanaAndFechaPagoLessThanEqual(agencia,
				anio, semana, fechaJueves);

		return CompletableFuture.completedFuture(cobranzaPura != null ? cobranzaPura : 0.0);
	}

	public ArrayList<PagoDynamicModel> findByAgenciaAnioSemanaAndCierraConGreaterThan(String agencia, int anio,
			int semana,
			double cierraConGreaterThan) {
		return this.repo.findByAgenciaAndAnioAndSemanaAndCierraConGreaterThan(agencia, anio, semana,
				cierraConGreaterThan);
	}

	public ArrayList<PagoDynamicModel> findByAgenciaInAnioSemanaAndCierraConGreaterThan(String[] agencia, int anio,
			int semana,
			double cierraConGreaterThan) {
		return this.repo.findByAgenciaInAndAnioAndSemanaAndCierraConGreaterThan(agencia, anio, semana,
				cierraConGreaterThan);
	}

	public PagoDynamicModel findByPrestamoIdAnioAndSemana(String prestamoId, int anio, int semana) {
		return this.repo.findByPrestamoIdAndAnioAndSemana(prestamoId, anio, semana);
	}
}

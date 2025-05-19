package tech.calaverita.sfast_xpress.cierre_agencia;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.cierre_agencia.pojo.BalanceAgencia;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.Calendario;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.ComisionesAPagarEnSemana;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.EgresosAgente;
import tech.calaverita.sfast_xpress.cierre_agencia.pojo.IngresosAgente;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaService;
import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.AsignacionService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.ComisionService;
import tech.calaverita.sfast_xpress.services.SucursalService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;

@Service
public class CierreAgenciaService {
	private final AgenciaService agenciaService;
	private final AsignacionService asignacionService;
	private final CalendarioService calendarioService;
	private final CierreAgenciaRepository cierreAgenciaRepository;
	private final ComisionService comisionService;
	private final DashboardAgenciaService dashboardAgenciaService;
	private final PagoDynamicService pagoDynamicService;
	private final UsuarioService usuarioService;
	private final SucursalService sucursalService;

	public CierreAgenciaService(AgenciaService agenciaService, AsignacionService asignacionService,
			CalendarioService calendarioService, CierreAgenciaRepository cierreAgenciaRepository,
			ComisionService comisionService, DashboardAgenciaService dashboardAgenciaService,
			PagoDynamicService pagoDynamicService, UsuarioService usuarioService, SucursalService sucursalService) {
		this.agenciaService = agenciaService;
		this.asignacionService = asignacionService;
		this.calendarioService = calendarioService;
		this.cierreAgenciaRepository = cierreAgenciaRepository;
		this.comisionService = comisionService;
		this.dashboardAgenciaService = dashboardAgenciaService;
		this.pagoDynamicService = pagoDynamicService;
		this.usuarioService = usuarioService;
		this.sucursalService = sucursalService;
	}

	public CierreAgenciaModel findByAgenciaAnioSemana(String agencia, Integer anio, Integer semana) {
		return this.cierreAgenciaRepository.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
	}

	public CierreAgenciaModel save(CierreAgenciaModel cierreAgenciaModel) {
		return this.cierreAgenciaRepository.save(cierreAgenciaModel);
	}

	@Async("asyncExecutor")
	public CompletableFuture<List<CierreAgenciaModel>> findByGerenciaAnioSemanaAsync(String gerencia, Integer anio,
			Integer semana) {
		return CompletableFuture.completedFuture(
				this.cierreAgenciaRepository.findByGerenciaAndAnioAndSemana(gerencia, anio, semana));
	}

	public ResponseEntity<CierreAgenciaDto> getByUsuarioAgenciaAnioSemana(String usuario, String agencia, Integer anio,
			Integer semana) {
		CierreAgenciaModel cierreAgenciaModel = this.cierreAgenciaRepository
				.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
		ComisionModel comisionModel = this.comisionService.findByAgenciaAnioAndSemana(agencia, anio, semana);

		if (!this.usuarioService.existsByUsuario(usuario)) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		if (!this.usuarioService.existsByUsuarioAndStatus(usuario, true)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (cierreAgenciaModel != null) {
			CierreAgenciaDto cierreAgenciaDto = new CierreAgenciaDto(cierreAgenciaModel)
					.setBalanceAgencia(new BalanceAgencia(cierreAgenciaModel, comisionModel))
					.setComisionesAPagarEnSemana(new ComisionesAPagarEnSemana(cierreAgenciaModel))
					.setEgresosAgente(new EgresosAgente(cierreAgenciaModel))
					.setIngresosAgente(new IngresosAgente(cierreAgenciaModel, comisionModel));

			return new ResponseEntity<>(cierreAgenciaDto, HttpStatus.OK);
		}

		if (this.agenciaService.existsById(agencia)) {
			DashboardAgenciaDto dashboardAgenciaDto = this.dashboardAgenciaService
					.getByAgenciaAnioSemana(agencia, anio, semana).getBody();
			CompletableFuture<Integer> clientesPagoCompleto = this.pagoDynamicService
					.findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
			CompletableFuture<Double> cobranzaPuraAlJueves = this.pagoDynamicService
					.findCobranzaPuraByAgenciaAnioSemanaAlJueves(agencia, anio, semana);

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

			List<UsuarioModel> usuarioModels = new ArrayList<>();
			usuarioModels.add(agenteUsuarioModel);
			usuarioModels.add(gerenteUsuarioModel);

			List<AsignacionModel> asignacionModels = this.asignacionService.findByAgenciaAnioSemana(agencia,
					anio,
					semana);
			Calendario semanaAnteriorCalendario = this.calendarioService.getSemanaAnteriorByAnioSemana(anio, semana);
			ComisionModel semanaAnteriorComisionModel = this.comisionService.findByAgenciaAnioAndSemana(agencia,
					semanaAnteriorCalendario.getAnio(), semanaAnteriorCalendario.getSemana());

			CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());
			Calendario mesAnteriorCalendario = Calendario.getMesAnterior(calendarioModel);
			List<CalendarioModel> semanasDelMesCalendarioModel = this.calendarioService.findByAnioAndMesAsync(
					mesAnteriorCalendario.getAnio(), mesAnteriorCalendario.getMes()).join();

			List<CierreAgenciaModel> cierreAgenciaModels = new ArrayList<>();
			List<ComisionModel> comisionModels = new ArrayList<>();
			for (int i = 0; i < semanasDelMesCalendarioModel.size(); i++) {
				// To easy code
				int anioMesAnterior = semanasDelMesCalendarioModel.get(i).getAnio();
				int semanaMesAnterior = semanasDelMesCalendarioModel.get(i).getSemana();

				cierreAgenciaModels.add(findByAgenciaAnioSemana(agencia, anioMesAnterior, semanaMesAnterior));
				comisionModels.add(this.comisionService.findByAgenciaAnioAndSemana(agencia, anioMesAnterior,
						semanaMesAnterior));
			}

			// To easy code
			String sucursal = this.sucursalService.findNombreSucursalByGerenciaId(dashboardAgenciaDto.getGerencia());
			String fechaIngresoAgente = agenteUsuarioModel.getFechaIngreso();
			int pinAgente = agenteUsuarioModel.getPin();
			BalanceAgencia balanceAgencia = new BalanceAgencia(dashboardAgenciaDto, usuarioModels)
					.nivel(clientesPagoCompleto.join(),
							cobranzaPuraAlJueves.join() / dashboardAgenciaDto.getDebitoTotal(), fechaIngresoAgente)
					.nivelCalculado(clientesPagoCompleto.join(),
							cobranzaPuraAlJueves.join() / dashboardAgenciaDto.getDebitoTotal(), fechaIngresoAgente);
			ComisionesAPagarEnSemana comisionesAPagarEnSemana = new ComisionesAPagarEnSemana(
					semanaAnteriorComisionModel);
			EgresosAgente egresosAgente = new EgresosAgente(asignacionModels,
					dashboardAgenciaDto.getCobranzaTotal());
			IngresosAgente ingresosAgente = new IngresosAgente(dashboardAgenciaDto);

			if (calendarioModel.isPagoBono()) {
				comisionesAPagarEnSemana.bonos(cierreAgenciaModels, comisionModels, fechaIngresoAgente);
			}

			@SuppressWarnings("deprecation")
			String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es",
					"ES"));
			String primeraLetra = mes.substring(0, 1);
			String mayuscula = primeraLetra.toUpperCase();
			String demasLetras = mes.substring(1);
			mes = mayuscula + demasLetras;

			CierreAgenciaDto cierreAgenciaDto = new CierreAgenciaDto(sucursal, mes, pinAgente)
					.dashboardAgenciaDto(dashboardAgenciaDto)
					.setBalanceAgencia(balanceAgencia)
					.setComisionesAPagarEnSemana(comisionesAPagarEnSemana)
					.setEgresosAgente(egresosAgente)
					.setIngresosAgente(ingresosAgente);

			return new ResponseEntity<>(cierreAgenciaDto, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}

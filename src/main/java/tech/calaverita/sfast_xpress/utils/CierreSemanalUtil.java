package tech.calaverita.sfast_xpress.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.calaverita.sfast_xpress.Constants;
import tech.calaverita.sfast_xpress.DTOs.CierreSemanalConsolidadoV2DTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.BalanceAgenciaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.ComisionesAPagarEnSemanaDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.EgresosAgenteDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.EgresosGerenteDTO;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.IngresosAgenteDTO;
import tech.calaverita.sfast_xpress.DTOs.dashboard.DashboardDTO;
import tech.calaverita.sfast_xpress.itext.PdfStyleManager;
import tech.calaverita.sfast_xpress.itext.fonts.Fonts;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.CierreSemanalConsolidadoV2Service;
import tech.calaverita.sfast_xpress.services.ComisionService;
import tech.calaverita.sfast_xpress.services.SucursalService;
import tech.calaverita.sfast_xpress.services.cierre_semanal.CierreSemanalService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;

@Component
public class CierreSemanalUtil {
	private static CierreSemanalService cierreSemanalService;
	private static SucursalService sucursalService;
	private static AgenciaService agenciaService;
	private static PagoDynamicService pagoDynamicService;
	private static CalendarioService calendarioService;
	private static PrestamoViewService prestamoViewService;
	private static final Fonts fuentes = new Fonts();
	private static ComisionService comisionService;
	private static CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service;

	public CierreSemanalUtil(CierreSemanalService cierreSemanalService, PagoDynamicService pagoDynamicService,
			SucursalService sucursalService, AgenciaService agenciaService,
			PrestamoViewService prestamoViewService, CalendarioService calendarioService,
			ComisionService comisionService,
			CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service) {
		CierreSemanalUtil.cierreSemanalService = cierreSemanalService;
		CierreSemanalUtil.sucursalService = sucursalService;
		CierreSemanalUtil.agenciaService = agenciaService;
		CierreSemanalUtil.pagoDynamicService = pagoDynamicService;
		CierreSemanalUtil.calendarioService = calendarioService;
		CierreSemanalUtil.prestamoViewService = prestamoViewService;
		CierreSemanalUtil.comisionService = comisionService;
		CierreSemanalUtil.cierreSemanalConsolidadoV2Service = cierreSemanalConsolidadoV2Service;
	}

	public static CierreSemanalDTO getCierreSemanalDTO(CierreSemanalModel cierreSemanalModel) {
		CierreSemanalDTO cierreSemanalDTO = CierreSemanalUtil.cierreSemanalService
				.getCierreSemanalDTO(cierreSemanalModel);

		cierreSemanalDTO.setIsAgenciaCerrada(true);

		return cierreSemanalDTO;
	}

	public static CierreSemanalConsolidadoV2DTO getCierreSemanalConsolidadoV2DTO(
			CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model, ComisionModel comisionModel) {
		CierreSemanalConsolidadoV2DTO cierreSemanalConsolidadoV2DTO = CierreSemanalUtil.cierreSemanalService
				.getCierreSemanalConsolidadoV2DTO(cierreSemanalConsolidadoV2Model, comisionModel);

		cierreSemanalConsolidadoV2DTO.setIsAgenciaCerrada(true);

		return cierreSemanalConsolidadoV2DTO;
	}

	public static CierreSemanalDTO getCierreSemanalDTO(DashboardDTO dashboard, List<UsuarioModel> darrusuarEnt,
			double asignaciones)
			throws ExecutionException, InterruptedException {
		CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();

		CompletableFuture<Integer> clientesPagoCompletoCF = CierreSemanalUtil.pagoDynamicService
				.findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(),
						dashboard.getAnio(),
						dashboard.getSemana());
		CompletableFuture<CalendarioModel> calendarioModelCF = CierreSemanalUtil.calendarioService
				.findByAnioAndSemanaAsync(dashboard.getAnio(), dashboard.getSemana());

		// To easy code
		UsuarioModel agenteUsuarioModel = darrusuarEnt.get(0);
		UsuarioModel gerenteUsuarioModel = darrusuarEnt.get(1);
		String nombreAgente = agenteUsuarioModel.getNombre() + " " + agenteUsuarioModel.getApellidoPaterno()
				+ " "
				+ agenteUsuarioModel.getApellidoMaterno();
		String nombreGerente = gerenteUsuarioModel.getNombre() + " " + gerenteUsuarioModel.getApellidoPaterno()
				+ " " + gerenteUsuarioModel.getApellidoMaterno();

		BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
		{
			balanceAgenciaDTO.setZona(dashboard.getGerencia());
			balanceAgenciaDTO.setGerente(nombreGerente);
			balanceAgenciaDTO.setAgencia(dashboard.getAgencia());
			balanceAgenciaDTO.setAgente(nombreAgente);
			balanceAgenciaDTO.setRendimiento(dashboard.getRendimiento());

			CalendarioModel calendarioModel = CierreSemanalUtil.calendarioService
					.findByFechaActual(LocalDate.now().toString());

			// To easy code
			String agencia = dashboard.getAgencia();
			int anio = dashboard.getAnio();
			int semana = dashboard.getSemana();
			String fechaJueves = LocalDate.parse(calendarioModel.getDesde()).plusDays(1) + " 23:59:59";

			int clientesPagoCompleto = CierreSemanalUtil.pagoDynamicService
					.findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(agencia, anio, semana)
					.join();

			double debitoTotal = CierreSemanalUtil.prestamoViewService
					.findDebitoTotalByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
							agencia, 0D, anio, semana)
					.join();

			double cobranzaPura = CierreSemanalUtil.pagoDynamicService
					.findCobranzaPuraByAgenciaAnioSemanaAndFechaPagoLessThanEqual(
							agencia, anio, semana, fechaJueves)
					.join();

			balanceAgenciaDTO.setNivel(BalanceAgenciaUtil.getNivelAgente(clientesPagoCompleto,
					cobranzaPura / debitoTotal, agenteUsuarioModel));

			balanceAgenciaDTO.setNivelCalculado(balanceAgenciaDTO.getNivel());
			balanceAgenciaDTO.setClientes(dashboard.getClientes());
			balanceAgenciaDTO.setPagosReducidos(dashboard.getPagosReducidos());
			balanceAgenciaDTO.setNoPagos(dashboard.getNoPagos());
			balanceAgenciaDTO.setLiquidaciones(dashboard.getNumeroLiquidaciones());
		}

		double cobranzaTotal = dashboard.getCobranzaTotal();

		EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
		{
			egresosAgenteDTO.setAsignaciones(asignaciones);

			Double efectivoEntregadoEnCierre = cobranzaTotal - egresosAgenteDTO.getAsignaciones();
			egresosAgenteDTO.setEfectivoEntregadoCierre(MyUtil.getDouble(efectivoEntregadoEnCierre));
		}

		EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
		{
			egresosGerenteDTO.setPorcentajeComisionCobranza(BalanceAgenciaUtil
					.getPorcentajeComisionCobranza(balanceAgenciaDTO.getNivel()));

			calendarioModelCF.join();

			// To easy code
			int porcentajeBonoMensual = 0;
			CalendarioModel calendarioModel = calendarioModelCF.get();

			if (calendarioModel.isPagoBono()) {
				clientesPagoCompletoCF.join();

				porcentajeBonoMensual = BalanceAgenciaUtil.getPorcentajeBonoMensual(
						clientesPagoCompletoCF.get(),
						dashboard.getRendimiento(), agenteUsuarioModel);
			}

			egresosGerenteDTO.setPorcentajeBonoMensual(porcentajeBonoMensual);

			CalendarioModel semanaAnteriorCalendarioModel = calendarioModel;
			CierreSemanalUtil.funSemanaAnterior(calendarioModel);

			Double pagoComisionCobranza = CierreSemanalUtil.cierreSemanalService
					.findComisionCobranzaAgenciaByAgenciaAnioAndSemana(dashboard.getAgencia(),
							semanaAnteriorCalendarioModel.getAnio(),
							semanaAnteriorCalendarioModel.getSemana());

			// Double pagoComisionCobranza = cobranzaTotal / 100
			// * egresosGerenteDTO.getPorcentajeComisionCobranza();

			egresosGerenteDTO.setPagoComisionCobranza(MyUtil.getDouble(pagoComisionCobranza));

			egresosGerenteDTO.setBonos(0.0);
			if (calendarioModel.isPagoBono()) {
				int indiceMes = Calendar.MONTH - 1;
				String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
						"Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };

				ArrayList<CalendarioModel> semanasDelMes = CierreSemanalUtil.calendarioService
						.findByAnioAndMesAsync(calendarioModel.getAnio(),
								meses[indiceMes])
						.join();

				for (int i = 0; i < semanasDelMes.size() - 1; i++) {
					cobranzaTotal += CierreSemanalUtil.pagoDynamicService
							.findCobranzaTotalByAgenciaAnioAndSemanaAsync(
									dashboard.getAgencia(),
									semanasDelMes.get(i).getAnio(),
									semanasDelMes.get(i).getSemana())
							.join();
				}

				Double bonos = cobranzaTotal / 100 * egresosGerenteDTO.getPorcentajeBonoMensual();
				egresosGerenteDTO.setBonos(bonos);
			}
		}

		IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
		{
			ingresosAgenteDTO.setCobranzaPura(dashboard.getTotalCobranzaPura());
			ingresosAgenteDTO.setMontoExcedente(dashboard.getMontoExcedente());
			ingresosAgenteDTO.setLiquidaciones(dashboard.getLiquidaciones());
			ingresosAgenteDTO.setMultas(dashboard.getMultas());
		}

		cierreSemanalDTO.setSemana(dashboard.getSemana());
		cierreSemanalDTO.setAnio(dashboard.getAnio());
		cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
		cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
		cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
		cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);
		cierreSemanalDTO.setPinAgente(agenteUsuarioModel.getPin());
		cierreSemanalDTO.setIsAgenciaCerrada(false);
		cierreSemanalDTO.setDia(LocalDate.now().getDayOfMonth());
		cierreSemanalDTO.setSucursal(sucursalService.findNombreSucursalByGerenciaId(dashboard.getGerencia()));
		cierreSemanalDTO.setStatusAgencia(agenciaService.findStatusById(dashboard.getAgencia()).join());

		String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es",
				"ES"));
		String primeraLetra = mes.substring(0, 1);
		String mayuscula = primeraLetra.toUpperCase();
		String demasLetras = mes.substring(1);
		mes = mayuscula + demasLetras;
		cierreSemanalDTO.setMes(mes);

		return cierreSemanalDTO;
	}

	public static CierreSemanalConsolidadoV2DTO getCierreSemanalConsolidadoV2DTO(DashboardDTO dashboard,
			List<UsuarioModel> darrusuarEnt,
			List<AsignacionModel> asignacionModels)
			throws ExecutionException, InterruptedException {
		CierreSemanalConsolidadoV2DTO cierreSemanalConsolidadoV2DTO = new CierreSemanalConsolidadoV2DTO();

		CompletableFuture<Integer> clientesPagoCompletoCF = CierreSemanalUtil.pagoDynamicService
				.findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(),
						dashboard.getAnio(),
						dashboard.getSemana());
		CompletableFuture<CalendarioModel> calendarioModelCF = CierreSemanalUtil.calendarioService
				.findByAnioAndSemanaAsync(dashboard.getAnio(), dashboard.getSemana());

		// To easy code
		UsuarioModel agenteUsuarioModel = darrusuarEnt.get(0);
		UsuarioModel gerenteUsuarioModel = darrusuarEnt.get(1);
		String nombreAgente = agenteUsuarioModel.getNombre() + " " + agenteUsuarioModel.getApellidoPaterno()
				+ " "
				+ agenteUsuarioModel.getApellidoMaterno();
		String nombreGerente = gerenteUsuarioModel.getNombre() + " " + gerenteUsuarioModel.getApellidoPaterno()
				+ " " + gerenteUsuarioModel.getApellidoMaterno();

		BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
		{
			balanceAgenciaDTO.setZona(dashboard.getGerencia());
			balanceAgenciaDTO.setGerente(nombreGerente);
			balanceAgenciaDTO.setAgencia(dashboard.getAgencia());
			balanceAgenciaDTO.setAgente(nombreAgente);
			balanceAgenciaDTO.setRendimiento(dashboard.getRendimiento());

			CalendarioModel calendarioModel = CierreSemanalUtil.calendarioService
					.findByFechaActual(LocalDate.now().toString());

			// To easy code
			String agencia = dashboard.getAgencia();
			int anio = dashboard.getAnio();
			int semana = dashboard.getSemana();
			String fechaJueves = LocalDate.parse(calendarioModel.getDesde()).plusDays(1) + " 23:59:59";

			int clientesPagoCompleto = CierreSemanalUtil.pagoDynamicService
					.findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(agencia, anio, semana)
					.join();

			double debitoTotal = CierreSemanalUtil.prestamoViewService
					.findDebitoTotalByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(
							agencia, 0D, anio, semana)
					.join();

			double cobranzaPura = CierreSemanalUtil.pagoDynamicService
					.findCobranzaPuraByAgenciaAnioSemanaAndFechaPagoLessThanEqual(
							agencia, anio, semana, fechaJueves)
					.join();

			balanceAgenciaDTO.setNivel(BalanceAgenciaUtil.getNivelAgente(clientesPagoCompleto,
					cobranzaPura / debitoTotal, agenteUsuarioModel));

			balanceAgenciaDTO.setNivelCalculado(balanceAgenciaDTO.getNivel());
			balanceAgenciaDTO.setClientes(dashboard.getClientes());
			balanceAgenciaDTO.setPagosReducidos(dashboard.getPagosReducidos());
			balanceAgenciaDTO.setNoPagos(dashboard.getNoPagos());
			balanceAgenciaDTO.setLiquidaciones(dashboard.getNumeroLiquidaciones());
		}

		double cobranzaTotal = dashboard.getCobranzaTotal();

		EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
		{
			// To easy code
			double asignaciones = asignacionModels.stream().mapToDouble(AsignacionModel::getMonto).sum();
			double asignacionesASeguridad = asignacionModels.stream()
					.filter(asignacionModel -> asignacionModel.getRecibioUsuarioModel().getTipo()
							.equals("Seguridad"))
					.mapToDouble(AsignacionModel::getMonto).sum();
			double asignacionesAGerente = asignacionModels.stream()
					.filter(asignacionModel -> asignacionModel.getRecibioUsuarioModel().getTipo()
							.equals("Gerente"))
					.mapToDouble(AsignacionModel::getMonto).sum();

			HashMap<String, Double> asignacionesDesglose = new HashMap<>();
			asignacionesDesglose.put("aSeguridad", asignacionesASeguridad);
			asignacionesDesglose.put("aGerente", asignacionesAGerente);

			egresosAgenteDTO.setAsignaciones(asignaciones);
			egresosAgenteDTO.setAsignaciones_desglose(asignacionesDesglose);

			Double efectivoEntregadoEnCierre = cobranzaTotal - egresosAgenteDTO.getAsignaciones();
			egresosAgenteDTO.setEfectivoEntregadoCierre(MyUtil.getDouble(efectivoEntregadoEnCierre));
		}

		ComisionesAPagarEnSemanaDTO comisionesAPagarEnSemanaDTO = new ComisionesAPagarEnSemanaDTO();
		{
			CalendarioModel semanaActualCalendarioModel = calendarioModelCF.join();
			CalendarioModel semanaAnteriorCalendarioModel = CierreSemanalUtil
					.getSemanaAnterior(semanaActualCalendarioModel);

			// To easy code
			String agencia = dashboard.getAgencia();
			int anioSemanaAnterior = semanaAnteriorCalendarioModel.getAnio();
			int semanaAnterior = semanaAnteriorCalendarioModel.getSemana();

			ComisionModel comisionModel = CierreSemanalUtil.comisionService
					.findByAgenciaAnioAndSemana(agencia, anioSemanaAnterior, semanaAnterior);

			if (comisionModel != null) {
				comisionesAPagarEnSemanaDTO.setPorcentajeComisionCobranza(
						comisionModel.getPorcentajeComisionCobranza());

				comisionesAPagarEnSemanaDTO
						.setPagoComisionCobranza(comisionModel.getComisionCobranza());
				comisionesAPagarEnSemanaDTO.setPagoComisionVentas(comisionModel.getComisionVentas());
			}

			if (semanaActualCalendarioModel.isPagoBono()) {
				String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
						"Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
				int indiceMes = 0;
				for (int i = 0; i < meses.length; i++) {
					if (semanaActualCalendarioModel.getMes().equals(meses[i])) {
						indiceMes = i - 1;
						break;
					}
				}

				// To easy code
				int anio = semanaActualCalendarioModel.getAnio();

				ArrayList<CalendarioModel> semanasDelMes = CierreSemanalUtil.calendarioService
						.findByAnioAndMesAsync(indiceMes < 0 ? anio - 1 : anio,
								meses[indiceMes < 0 ? 11 : indiceMes])
						.join();

				CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model;

				cobranzaTotal = 0;
				double rendimiento = 0D;
				int clientesPagoCompleto = 0;
				for (int i = 0; i < semanasDelMes.size(); i++) {
					cierreSemanalConsolidadoV2Model = CierreSemanalUtil.cierreSemanalConsolidadoV2Service
							.findByAgenciaAnioAndSemana(agencia,
									semanasDelMes.get(i).getAnio(),
									semanasDelMes.get(i).getSemana());
					comisionModel = CierreSemanalUtil.comisionService.findByAgenciaAnioAndSemana(
							agencia, semanasDelMes.get(i).getAnio(),
							semanasDelMes.get(i).getSemana());

					if (cierreSemanalConsolidadoV2Model != null && comisionModel != null) {
						cobranzaTotal += comisionModel.getCobranzaTotal()
								- cierreSemanalConsolidadoV2Model.getLiquidaciones();

						rendimiento += comisionModel.getRendimiento();

						clientesPagoCompleto += cierreSemanalConsolidadoV2Model.getClientes()
								- cierreSemanalConsolidadoV2Model.getPagosReducidos()
								- cierreSemanalConsolidadoV2Model.getNoPagos();
					}
				}

				rendimiento = rendimiento / semanasDelMes.size();
				clientesPagoCompleto = clientesPagoCompleto / semanasDelMes.size();
				cobranzaTotal = MyUtil.getDouble(cobranzaTotal);

				comisionesAPagarEnSemanaDTO.setPorcentajeBonoMensual(
						BalanceAgenciaUtil.getPorcentajeBonoMensual(clientesPagoCompleto,
								rendimiento,
								agenteUsuarioModel));
				comisionesAPagarEnSemanaDTO.setBonos(MyUtil.getDouble(
						cobranzaTotal / 100 * comisionesAPagarEnSemanaDTO
								.getPorcentajeBonoMensual()));
			}
		}

		IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
		{
			ingresosAgenteDTO.setCobranzaPura(dashboard.getTotalCobranzaPura());
			ingresosAgenteDTO.setMontoExcedente(dashboard.getMontoExcedente());
			ingresosAgenteDTO.setLiquidaciones(dashboard.getLiquidaciones());
			ingresosAgenteDTO.setMultas(dashboard.getMultas());
		}

		cierreSemanalConsolidadoV2DTO.setSemana(dashboard.getSemana());
		cierreSemanalConsolidadoV2DTO.setAnio(dashboard.getAnio());
		cierreSemanalConsolidadoV2DTO.setBalanceAgenciaDTO(balanceAgenciaDTO);
		cierreSemanalConsolidadoV2DTO.setEgresosAgenteDTO(egresosAgenteDTO);
		cierreSemanalConsolidadoV2DTO.setComisionesAPagarEnSemanaDTO(comisionesAPagarEnSemanaDTO);
		cierreSemanalConsolidadoV2DTO.setIngresosAgenteDTO(ingresosAgenteDTO);
		cierreSemanalConsolidadoV2DTO.setPinAgente(agenteUsuarioModel.getPin());
		cierreSemanalConsolidadoV2DTO.setIsAgenciaCerrada(false);
		cierreSemanalConsolidadoV2DTO
				.setSucursal(sucursalService.findNombreSucursalByGerenciaId(dashboard.getGerencia()));
		cierreSemanalConsolidadoV2DTO
				.setStatusAgencia(agenciaService.findStatusById(dashboard.getAgencia()).join());

		String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es",
				"ES"));
		String primeraLetra = mes.substring(0, 1);
		String mayuscula = primeraLetra.toUpperCase();
		String demasLetras = mes.substring(1);
		mes = mayuscula + demasLetras;
		cierreSemanalConsolidadoV2DTO.setMes(mes);

		return cierreSemanalConsolidadoV2DTO;
	}

	private static void funSemanaAnterior(CalendarioModel calendarioModel) {
		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();

		if (semana == 1) {
			anio = anio - 1;
			semana = CierreSemanalUtil.calendarioService
					.existsByAnioAndSemana(anio, 53) ? 53 : 52;
		} else {
			semana = semana - 1;
		}

		calendarioModel.setAnio(anio);
		calendarioModel.setSemana(semana);
	}

	private static CalendarioModel getSemanaAnterior(CalendarioModel calendarioModel) {
		CalendarioModel semanaAnteriorCalendarioModel = new CalendarioModel();

		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();

		if (semana == 1) {
			anio = anio - 1;
			semana = CierreSemanalUtil.calendarioService
					.existsByAnioAndSemana(anio, 53) ? 53 : 52;
		} else {
			semana = semana - 1;
		}

		semanaAnteriorCalendarioModel.setAnio(anio);
		semanaAnteriorCalendarioModel.setSemana(semana);

		return semanaAnteriorCalendarioModel;
	}

	public static void createCierreSemanalPDF(CierreSemanalDTO dto)
			throws DocumentException, FileNotFoundException {
		Rectangle hoja = new Rectangle(612f, 380f);
		Document document = new Document(hoja, 0f, 0f, 5f, 5f);

		// To easy code
		String id = dto.getBalanceAgencia().getZona() + '-' + dto.getBalanceAgencia().getAgencia() + '-'
				+ dto.getAnio() + '-' + dto.getSemana();

		PdfWriter.getInstance(document, new FileOutputStream(Constants.RUTA_PDF_PRODUCCION + id + ".pdf"));
		document.open();

		PdfPTable tablaCierreSemanal = new PdfPTable(3);
		tablaCierreSemanal.setWidths(new float[] { 1.35f, .1f, 1.64f });

		PdfPCell columnaIzquierda = new PdfPCell();
		columnaIzquierda.setBorder(0);

		PdfPTable tablaColumnaIzquierda = new PdfPTable(1);
		tablaColumnaIzquierda.setWidthPercentage(100);

		tablaColumnaIzquierda.addCell(CierreSemanalUtil.getTitulo());
		tablaColumnaIzquierda.addCell(CierreSemanalUtil.getBalanceAgencia(dto.getBalanceAgencia()));
		tablaColumnaIzquierda.addCell(CierreSemanalUtil.getIngresosAgente(dto.getIngresosAgente()));
		tablaColumnaIzquierda.addCell(CierreSemanalUtil.getPorcentajesEgresosGerente(dto.getEgresosGerente()));

		columnaIzquierda.addElement(tablaColumnaIzquierda);

		PdfPCell columnaCentral = new PdfPCell();
		columnaCentral.setBorder(0);

		PdfPCell columnaDerecha = new PdfPCell();
		columnaDerecha.setBorder(0);

		PdfPTable tablaColumnaDerecha = new PdfPTable(1);
		tablaColumnaDerecha.setWidthPercentage(100);

		tablaColumnaDerecha.addCell(CierreSemanalUtil.getSemana(dto));
		tablaColumnaDerecha.addCell(CierreSemanalUtil.getCobranza(dto.getBalanceAgencia()));
		tablaColumnaDerecha.addCell(CierreSemanalUtil.getEgresosAgente(dto.getEgresosAgente()));
		tablaColumnaDerecha.addCell(CierreSemanalUtil.getEgresosGerente(dto.getEgresosGerente()));

		columnaDerecha.addElement(tablaColumnaDerecha);

		tablaCierreSemanal.setWidthPercentage(100);
		tablaCierreSemanal.addCell(columnaIzquierda);
		tablaCierreSemanal.addCell(columnaCentral);
		tablaCierreSemanal.addCell(columnaDerecha);

		document.add(tablaCierreSemanal);

		/*
		 * Chunk linebreak = new Chunk(new DottedLineSeparator());
		 * document.add(linebreak);
		 */
		document.add(new Paragraph("\n"));
		document.add(getTerminosTable());

		document.close();
	}

	private static PdfPTable getTerminosTable() {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(95);

		PdfPCell pdfPCell = new PdfPCell(getTerminosParagraph());
		PdfStyleManager.commonCellStyle(pdfPCell);
		table.addCell(pdfPCell);
		return table;
	}

	private static Paragraph getTerminosParagraph() {
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 6, PdfStyleManager.getPrimaryBasecolor());
		Paragraph paragraph = new Paragraph(terminosTxt(), font);
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
		return paragraph;
	}

	private static String terminosTxt() {
		return "Por política de la empresa, es necesario revisar y garantizar que este comprobante sea completado de "
				+
				"manera adecuada antes de proceder con la firma. Le recordamos que dicho comprobante constituye el "
				+
				"respaldo del efectivo entregado al gerente, y será requerido en caso de aclaraciones o auditorías. "
				+
				"Tenga en cuenta que cualquier comprobante que presente tachaduras o enmendaduras no será aceptado.";
	}

	private static PdfPCell getTitulo() {
		PdfPCell cellTitulo = new PdfPCell();
		PdfStyleManager.setStyleForCellTitles(cellTitulo);

		Paragraph titulo = fuentes.bold("CIERRE SEMANAL", 12, 1,
				PdfStyleManager.getPrimaryBasecolor());
		Paragraph subTitulo = fuentes.bold("BALANCE DE AGENCIA", 12, 1,
				PdfStyleManager.getPrimaryBasecolor());

		cellTitulo.addElement(titulo);
		cellTitulo.addElement(subTitulo);

		return cellTitulo;
	}

	private static PdfPCell getBalanceAgencia(BalanceAgenciaDTO balanceAgenciaDTO) {
		PdfPCell cellBalanceAgencia = new PdfPCell();
		cellBalanceAgencia.setBorder(0);
		cellBalanceAgencia.setPadding(0);

		PdfPTable tablaBalanceAgencia = new PdfPTable(1);
		tablaBalanceAgencia.setWidthPercentage(100);

		Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8,
				PdfStyleManager.getPrimaryBasecolor());
		Font fontRegular = FontFactory.getFont(FontFactory.HELVETICA, 8, PdfStyleManager.getPrimaryBasecolor());

		Paragraph zonaGerenteParagraph = new Paragraph();
		zonaGerenteParagraph.add(new Chunk("ZONA: ", fontBold));
		zonaGerenteParagraph.add(new Chunk(balanceAgenciaDTO.getZona(), fontRegular));
		zonaGerenteParagraph.add(new Chunk("  ·  GERENTE: ", fontBold));
		zonaGerenteParagraph.add(new Chunk(balanceAgenciaDTO.getGerente(), fontRegular));

		PdfPCell zonaGerenteCell = new PdfPCell(zonaGerenteParagraph);
		PdfStyleManager.setStyleNormalCell(zonaGerenteCell);

		Paragraph agenciaYAgenteParagraph = new Paragraph();
		agenciaYAgenteParagraph.add(new Chunk("AGENCIA: ", fontBold));
		agenciaYAgenteParagraph.add(new Chunk(balanceAgenciaDTO.getAgencia(), fontRegular));
		agenciaYAgenteParagraph.add(new Chunk("  ·  AGENTE: ", fontBold));
		agenciaYAgenteParagraph.add(new Chunk(balanceAgenciaDTO.getAgente(), fontRegular));

		PdfPCell agenciaYAgenteCell = new PdfPCell(agenciaYAgenteParagraph);
		PdfStyleManager.setStyleNormalCell(agenciaYAgenteCell);

		Paragraph rendimientoYNivelParagraph = new Paragraph();
		rendimientoYNivelParagraph.add(new Chunk("% DE AGENCIA: ", fontBold));
		rendimientoYNivelParagraph.add(new Chunk(balanceAgenciaDTO.getRendimiento() + "", fontRegular));
		rendimientoYNivelParagraph.add(new Chunk("  ·  NIVEL: ", fontBold));
		rendimientoYNivelParagraph.add(new Chunk(balanceAgenciaDTO.getNivel(), fontRegular));
		PdfPCell rendimientoYNivelCell = new PdfPCell(rendimientoYNivelParagraph);
		PdfStyleManager.setStyleNormalCell(rendimientoYNivelCell);

		tablaBalanceAgencia.addCell(zonaGerenteCell);
		tablaBalanceAgencia.addCell(agenciaYAgenteCell);
		tablaBalanceAgencia.addCell(rendimientoYNivelCell);

		cellBalanceAgencia.addElement(tablaBalanceAgencia);
		return cellBalanceAgencia;
	}

	private static PdfPCell getIngresosAgente(IngresosAgenteDTO ingresosAgenteDTO) throws DocumentException {
		PdfPCell cellIngresosAgente = new PdfPCell();
		cellIngresosAgente.setBorder(0);
		cellIngresosAgente.setPadding(0);
		cellIngresosAgente.setPaddingTop(5f);

		PdfPTable tablaIngresosAgente = new PdfPTable(2);
		tablaIngresosAgente.setWidthPercentage(100);
		tablaIngresosAgente.setWidths(new float[] { 1.3f, .7f });

		PdfPCell cellTitulo = new PdfPCell(fuentes.bold("INGRESOS DEL AGENTE", 11,
				PdfStyleManager.getPrimaryBasecolor()));
		cellTitulo.setColspan(2);
		PdfStyleManager.setStyleForCellTitles(cellTitulo);

		PdfPCell cellCobranzaPura = new PdfPCell(fuentes.bold("COBRANZA PURA:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellCobranzaPura);

		PdfPCell cellMontoExcedente = new PdfPCell(fuentes.bold("MONTO EXCEDENTE:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellMontoExcedente);

		PdfPCell cellLiquidaciones = new PdfPCell(fuentes.bold("LIQUIDACIONES:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellLiquidaciones);

		PdfPCell cellMultas = new PdfPCell(fuentes.bold("MULTAS:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellMultas);

		PdfPCell cellOtros = new PdfPCell(fuentes.bold("OTROS (" + ingresosAgenteDTO.getMotivoOtros() + "):", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellOtros);

		PdfPCell cellTotalIngresos = new PdfPCell(fuentes.bold("TOTAL DE INGRESOS: " + money(ingresosAgenteDTO
				.getTotal()), 11, PdfStyleManager.getPrimaryBasecolor()));
		cellTotalIngresos.setColspan(2);
		PdfStyleManager.setStyleFillColorCell(cellTotalIngresos);

		PdfPCell cellCobranzaPura2 = new PdfPCell(fuentes.regular(money(ingresosAgenteDTO.getCobranzaPura()),
				9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellCobranzaPura2);

		PdfPCell cellMontoExcedente2 = new PdfPCell(
				fuentes.regular(money(ingresosAgenteDTO.getMontoExcedente()),
						9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellMontoExcedente2);

		PdfPCell cellLiquidaciones2 = new PdfPCell(
				fuentes.regular(money(ingresosAgenteDTO.getLiquidaciones()), 9,
						PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellLiquidaciones2);

		PdfPCell cellMultas2 = new PdfPCell(fuentes.regular(money(ingresosAgenteDTO.getMultas()), 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellMultas2);

		PdfPCell cellOtros2 = new PdfPCell(fuentes.regular(money(ingresosAgenteDTO.getOtros()), 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellOtros2);

		tablaIngresosAgente.addCell(cellTitulo);
		tablaIngresosAgente.addCell(cellCobranzaPura);
		tablaIngresosAgente.addCell(cellCobranzaPura2);
		tablaIngresosAgente.addCell(cellMontoExcedente);
		tablaIngresosAgente.addCell(cellMontoExcedente2);
		tablaIngresosAgente.addCell(cellLiquidaciones);
		tablaIngresosAgente.addCell(cellLiquidaciones2);
		tablaIngresosAgente.addCell(cellMultas);
		tablaIngresosAgente.addCell(cellMultas2);
		tablaIngresosAgente.addCell(cellOtros);
		tablaIngresosAgente.addCell(cellOtros2);
		tablaIngresosAgente.addCell(cellTotalIngresos);

		cellIngresosAgente.addElement(tablaIngresosAgente);

		return cellIngresosAgente;
	}

	private static PdfPCell getPorcentajesEgresosGerente(EgresosGerenteDTO egresosGerenteDTO) {
		PdfPCell cellPorcentajesEgresosGerente = new PdfPCell();
		cellPorcentajesEgresosGerente.setBorder(0);
		cellPorcentajesEgresosGerente.setPadding(0);
		cellPorcentajesEgresosGerente.setPaddingTop(5f);

		PdfPTable tablaPorcentajesEgresosGerente = new PdfPTable(1);
		tablaPorcentajesEgresosGerente.setWidthPercentage(100);

		PdfPCell cellComisionCobranza = new PdfPCell(fuentes.regular("% DE COMISIÓN POR COBRANZA: "
				+ egresosGerenteDTO.getPorcentajeComisionCobranza() + "%", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellComisionCobranza);

		PdfPCell cellBonoMensual = new PdfPCell(fuentes.regular("% DE BONO MENSUAL: " + egresosGerenteDTO
				.getPorcentajeBonoMensual() + "%", 9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellBonoMensual);

		PdfPCell cellValidation = new PdfPCell(fuentes.regular(
				"* La generacion de este documento PDF se lleva a " +
						"cabo de manera automatizada unicamente si el cierre es validado mediante la firma con "
						+
						"fotografia y codigo PIN, tanto por parte del agente como del gerente. ",
				6, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.commonCellStyle(cellValidation);

		tablaPorcentajesEgresosGerente.addCell(cellComisionCobranza);
		tablaPorcentajesEgresosGerente.addCell(cellBonoMensual);
		tablaPorcentajesEgresosGerente.addCell(cellValidation);

		cellPorcentajesEgresosGerente.addElement(tablaPorcentajesEgresosGerente);

		return cellPorcentajesEgresosGerente;
	}

	private static PdfPCell getSemana(CierreSemanalDTO dto) {
		PdfPCell cellSemana = new PdfPCell();
		PdfStyleManager.setStyleForCellTitles(cellSemana);

		Paragraph semana = fuentes.bold("SEM: " + dto.getSemana() + " ANIO: " + dto.getAnio(), 11, 1,
				PdfStyleManager.getPrimaryBasecolor());
		Paragraph fecha = fuentes.bold(mxFormatFullCurrent().toUpperCase(), 9, 1,
				PdfStyleManager.getPrimaryBasecolor());

		cellSemana.addElement(semana);
		cellSemana.addElement(fecha);

		return cellSemana;
	}

	private static PdfPCell getCobranza(BalanceAgenciaDTO balanceAgenciaDTO) {
		PdfPCell cellCobranza = new PdfPCell();
		cellCobranza.setBorder(0);
		cellCobranza.setPadding(0);

		PdfPTable tablaBalanceAgencia = new PdfPTable(1);
		tablaBalanceAgencia.setWidthPercentage(100);

		Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
				PdfStyleManager.getPrimaryBasecolor());
		Font fontRegular = FontFactory.getFont(FontFactory.HELVETICA, 8, PdfStyleManager.getPrimaryBasecolor());

		Paragraph totalClientesParagraph = new Paragraph();
		totalClientesParagraph.add(new Chunk("TOTAL DE CLIENTES DE LA AGENCIA: ", fontRegular));
		totalClientesParagraph.add(new Chunk(balanceAgenciaDTO.getClientes() + "*", fontBold));

		PdfPCell totalClientesCell = new PdfPCell(totalClientesParagraph);
		PdfStyleManager.setStyleNormalCell(totalClientesCell);

		Paragraph pagoReducidoParagraph = new Paragraph();
		pagoReducidoParagraph.add(new Chunk("TOTAL DE CLIENTES CON PAGO REDUCIDO: ", fontRegular));
		pagoReducidoParagraph.add(new Chunk(balanceAgenciaDTO.getPagosReducidos() + "*", fontBold));

		PdfPCell pagoReducidoCell = new PdfPCell(pagoReducidoParagraph);
		PdfStyleManager.setStyleFillColorCell(pagoReducidoCell);

		Paragraph noPagoParagraph = new Paragraph();
		noPagoParagraph.add(new Chunk("TOTAL DE CLIENTES CON NO PAGO: ", fontRegular));
		noPagoParagraph.add(new Chunk(balanceAgenciaDTO.getNoPagos() + "*", fontBold));

		PdfPCell noPagoCell = new PdfPCell(noPagoParagraph);
		PdfStyleManager.setStyleNormalCell(noPagoCell);

		Paragraph liquidacionesParagraph = new Paragraph();
		liquidacionesParagraph.add(new Chunk("TOTAL DE CLIENTES LIQUIDADOS: ", fontRegular));
		liquidacionesParagraph.add(new Chunk(balanceAgenciaDTO.getLiquidaciones() + "*", fontBold));

		PdfPCell liquidacionesCell = new PdfPCell(liquidacionesParagraph);
		PdfStyleManager.setStyleFillColorCell(liquidacionesCell);

		tablaBalanceAgencia.addCell(totalClientesCell);
		tablaBalanceAgencia.addCell(pagoReducidoCell);
		tablaBalanceAgencia.addCell(noPagoCell);
		tablaBalanceAgencia.addCell(liquidacionesCell);

		cellCobranza.addElement(tablaBalanceAgencia);
		return cellCobranza;
	}

	private static PdfPCell getEgresosAgente(EgresosAgenteDTO egresosAgenteDTO) throws DocumentException {
		PdfPCell cellIngresosAgente = new PdfPCell();
		cellIngresosAgente.setBorder(0);
		cellIngresosAgente.setPadding(0);
		cellIngresosAgente.setPaddingTop(5f);

		PdfPTable tablaIngresosAgente = new PdfPTable(2);
		tablaIngresosAgente.setWidthPercentage(100);
		tablaIngresosAgente.setWidths(new float[] { 1.3f, .7f });

		PdfPCell cellTitulo = new PdfPCell(fuentes.bold("EGRESOS DEL AGENTE", 11,
				PdfStyleManager.getPrimaryBasecolor()));
		cellTitulo.setColspan(2);
		PdfStyleManager.setStyleForCellTitles(cellTitulo);

		PdfPCell cellAsignaciones = new PdfPCell(fuentes.bold("ASIGNACIONES PREVIAS DE EFECTIVO:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellAsignaciones);

		PdfPCell cellOtros = new PdfPCell(fuentes.bold("OTROS (" + egresosAgenteDTO.getMotivoOtros() + "):", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellOtros);

		PdfPCell cellEfectivoEntregadoCierre = new PdfPCell(fuentes.bold("EFECTIVO ENTREGADO EN CIERRE:",
				9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellEfectivoEntregadoCierre);

		PdfPCell cellTotalEgresos = new PdfPCell(fuentes.bold("TOTAL DE EGRESOS: " + money(egresosAgenteDTO
				.getTotal()), 11, PdfStyleManager.getPrimaryBasecolor()));
		cellTotalEgresos.setColspan(2);
		PdfStyleManager.setStyleFillColorCell(cellTotalEgresos);

		PdfPCell cellAsignaciones2 = new PdfPCell(fuentes.regular(money(egresosAgenteDTO.getAsignaciones()), 9,
				PdfStyleManager.getPrimaryBasecolor()));
		cellTitulo.setColspan(2);
		PdfStyleManager.setStyleNormalCell(cellAsignaciones2);

		PdfPCell cellOtros2 = new PdfPCell(fuentes.regular(money(egresosAgenteDTO.getOtros()), 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellOtros2);

		PdfPCell cellEfectivoEntregadoCierre2 = new PdfPCell(fuentes.regular(money(egresosAgenteDTO
				.getEfectivoEntregadoCierre()), 9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellEfectivoEntregadoCierre2);

		tablaIngresosAgente.addCell(cellTitulo);
		tablaIngresosAgente.addCell(cellAsignaciones);
		tablaIngresosAgente.addCell(cellAsignaciones2);
		tablaIngresosAgente.addCell(cellOtros);
		tablaIngresosAgente.addCell(cellOtros2);
		tablaIngresosAgente.addCell(cellEfectivoEntregadoCierre);
		tablaIngresosAgente.addCell(cellEfectivoEntregadoCierre2);
		tablaIngresosAgente.addCell(cellTotalEgresos);

		cellIngresosAgente.addElement(tablaIngresosAgente);

		return cellIngresosAgente;
	}

	private static PdfPCell getEgresosGerente(EgresosGerenteDTO egresosGerenteDTO) throws DocumentException {
		PdfPCell cellIngresosAgente = new PdfPCell();
		cellIngresosAgente.setBorder(0);
		cellIngresosAgente.setPadding(0);
		cellIngresosAgente.setPaddingTop(5f);

		PdfPTable tablaIngresosAgente = new PdfPTable(2);
		tablaIngresosAgente.setWidthPercentage(100);
		tablaIngresosAgente.setWidths(new float[] { 1.3f, .7f });

		PdfPCell cellTitulo = new PdfPCell(fuentes.bold("EGRESOS DEL GERENTE (Pagos a agente en CIERRE)",
				11, PdfStyleManager.getPrimaryBasecolor()));
		cellTitulo.setColspan(2);
		PdfStyleManager.setStyleForCellTitles(cellTitulo);

		PdfPCell cellPagoComisionCobranza = new PdfPCell(fuentes.bold("PAGO DE COMISION POR COBRANZA:",
				9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellPagoComisionCobranza);

		PdfPCell cellPagoComisionVentas = new PdfPCell(fuentes.bold("PAGO DE COMISION POR VENTAS:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellPagoComisionVentas);

		PdfPCell cellBonos = new PdfPCell(fuentes.bold("BONOS:", 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellBonos);

		PdfPCell cellEfectivoRestanteCierre = new PdfPCell(fuentes.bold("EFECTIVO RESTANTE DE CIERRE: "
				+ money(egresosGerenteDTO.getEfectivoRestanteCierre()), 11,
				PdfStyleManager.getPrimaryBasecolor()));
		cellEfectivoRestanteCierre.setColspan(2);
		PdfStyleManager.setStyleFillColorCell(cellEfectivoRestanteCierre);

		PdfPCell cellPagoComisionCobranza2 = new PdfPCell(fuentes.regular(money(egresosGerenteDTO
				.getPagoComisionCobranza()), 9, PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellPagoComisionCobranza2);

		PdfPCell cellPagoComisionVentas2 = new PdfPCell(
				fuentes.regular(money(egresosGerenteDTO.getPagoComisionVentas()), 9,
						PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleFillColorCell(cellPagoComisionVentas2);

		PdfPCell cellBonos2 = new PdfPCell(fuentes.regular(money(egresosGerenteDTO.getBonos()), 9,
				PdfStyleManager.getPrimaryBasecolor()));
		PdfStyleManager.setStyleNormalCell(cellBonos2);

		tablaIngresosAgente.addCell(cellTitulo);
		tablaIngresosAgente.addCell(cellPagoComisionCobranza);
		tablaIngresosAgente.addCell(cellPagoComisionCobranza2);
		tablaIngresosAgente.addCell(cellPagoComisionVentas);
		tablaIngresosAgente.addCell(cellPagoComisionVentas2);
		tablaIngresosAgente.addCell(cellBonos);
		tablaIngresosAgente.addCell(cellBonos2);
		tablaIngresosAgente.addCell(cellEfectivoRestanteCierre);

		cellIngresosAgente.addElement(tablaIngresosAgente);

		return cellIngresosAgente;
	}

	// TODO - mover a otra clase helper o util
	public static String mxFormatFullCurrent() {
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy h:mm a",
				new Locale("es", "MX"));
		return currentDate.format(formatter);
	}

	// public static String decimal(double numero) {
	// DecimalFormat decimalesformato = new DecimalFormat("###,###,###.00");
	// return decimalesformato.format(numero);
	// }

	public static String money(double numero) {
		DecimalFormat decimalesformato = new DecimalFormat("###,###,###.00");
		return "$" + decimalesformato.format(numero);
	}

	public static void subSendCierreSemanalMessage(CierreSemanalDTO cierreSemanalDTO) {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://fast-n8n.terio.xyz/webhook/08cff6e3-a379-4dc1-8d77-afc188147b99")
				.post(CierreSemanalUtil.requestBodyCierreSemanal(cierreSemanalDTO))
				.build();

		try (Response response = client.newCall(request).execute()) {
		} catch (IOException ignored) {
		}
	}

	public static void subSendCierreSemanalMessageConsolidadoV2(
			CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://fast-n8n.terio.xyz/webhook/08cff6e3-a379-4dc1-8d77-afc188147b99")
				.post(CierreSemanalUtil
						.requestBodyCierreSemanalConsolidadoV2(cierreSemanalConsolidadoV2Model))
				.build();

		try (Response response = client.newCall(request).execute()) {
		} catch (IOException ignored) {
		}
	}

	private static RequestBody requestBodyCierreSemanal(CierreSemanalDTO cierreSemanalDTO) {
		String cierreSemanalJSON = new Gson().toJson(cierreSemanalDTO);

		return RequestBody.create(MediaType.parse("application/json"), cierreSemanalJSON);
	}

	private static RequestBody requestBodyCierreSemanalConsolidadoV2(
			CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model) {
		String cierreSemanalJSON = new Gson().toJson(cierreSemanalConsolidadoV2Model);

		return RequestBody.create(MediaType.parse("application/json"), cierreSemanalJSON);
	}

	public static double getCobranzaTotalByAgenciaAnioAndSemana(String agencia, int anio, int semana)
			throws ExecutionException, InterruptedException {
		CompletableFuture<Double> cobranzaTotal = CierreSemanalUtil.pagoDynamicService
				.findCobranzaTotalByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
		cobranzaTotal.join();

		return cobranzaTotal.get();
	}
}

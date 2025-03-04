package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal.DetalleCierreSemanalDto;
import tech.calaverita.sfast_xpress.DTOs.flujo_efectivo.FlujoEfectivoDto;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreGerenciaModel;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;
import tech.calaverita.sfast_xpress.repositories.CierreGerenciaRepository;

@Service
public class CierreGerenciaService {
	private final CalendarioService calendarioService;
	private final CierreGerenciaRepository cierreGerenciaRepository;
	private final CobranzaAgenciaService cobranzaAgenciaService;
	private final DetalleCierreSemanalService detalleCierreAgenciasService;
	private final FlujoEfectivoService flujoEfectivoService;
	private final TabulacionDineroService tabulacionDineroService;

	public CierreGerenciaService(CalendarioService calendarioService, CierreGerenciaRepository cierreGerenciaRepository,
			CobranzaAgenciaService cobranzaAgenciaService, DetalleCierreSemanalService detalleCierreAgenciasService,
			FlujoEfectivoService flujoEfectivoService, TabulacionDineroService tabulacionDineroService) {
		this.calendarioService = calendarioService;
		this.cierreGerenciaRepository = cierreGerenciaRepository;
		this.cobranzaAgenciaService = cobranzaAgenciaService;
		this.detalleCierreAgenciasService = detalleCierreAgenciasService;
		this.flujoEfectivoService = flujoEfectivoService;
		this.tabulacionDineroService = tabulacionDineroService;
	}

	public boolean existsByGerenciaAnioSemana(String gerencia, int anio, int semana) {
		return this.cierreGerenciaRepository.existsByGerenciaAndAnioAndSemana(gerencia, anio, semana);
	}

	public CierreGerenciaModel save(CierreGerenciaModel cierreGerenciaModel) {
		return this.cierreGerenciaRepository.save(cierreGerenciaModel);
	}

	public Object createByUsuarioIdGerencia(int usuarioId, String gerencia) {
		CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

		// To easy code
		int anio = calendarioModel.getAnio();
		int semana = calendarioModel.getSemana();

		if (existsByGerenciaAnioSemana(gerencia, anio, semana)) {
			HashMap<String, String> respuestaHm = new HashMap<>();
			respuestaHm.put("cierreGerencia", "Ya existe un registro para esa gerencia");
			return respuestaHm;
		}

		Object cierreGerenciaModel = getCierreGerenciaByUsuarioIdAndGerencia(usuarioId, gerencia);

		if (cierreGerenciaModel instanceof CierreGerenciaModel) {
			CierreGerenciaModel respuestCierreGerenciaModel = save((CierreGerenciaModel) cierreGerenciaModel);
			if (respuestCierreGerenciaModel != null) {
				HashMap<String, String> respuestaHm = new HashMap<>();
				respuestaHm.put("cierreGerencia", "Creado exitosamente");
				return respuestaHm;
			}
		}

		return cierreGerenciaModel;
	}

	public Object getCierreGerenciaByUsuarioIdAndGerencia(Integer usuarioId, String gerencia) {
		CompletableFuture<List<CobranzaAgencia>> cobranzaAgenciaCf = this.cobranzaAgenciaService
				.getCobranzaAgenciasByGerenciaAsync(gerencia);
		CompletableFuture<DetalleCierreSemanalDto> detalleCierreAgenciasCf = this.detalleCierreAgenciasService
				.getDetalleCierreSemanalByGerenciaAsync(gerencia);
		CompletableFuture<FlujoEfectivoDto> flujoEfectivoCf = this.flujoEfectivoService
				.getFlujoEfectivoByGerenciaAsync(gerencia);
		CompletableFuture<Integer> idTabulacionDineroCf = this.tabulacionDineroService
				.findIdByGerenciaAsync(gerencia);
		CobranzaAgencia cobranzaAgencia = new CobranzaAgencia(cobranzaAgenciaCf.join());

		HashMap<String, String> respuestaHm = new HashMap<>();
		if (detalleCierreAgenciasCf.join() == null) {
			respuestaHm.put("detalleCierreSemanal", "No se pudo obtener correctamente");
		}
		if (flujoEfectivoCf.join() == null) {
			respuestaHm.put("flujoEfectivo", "No se pudo obtener correctamente");
		}
		if (idTabulacionDineroCf.join() == null) {
			respuestaHm.put("tabulacionDinero", "Debe existir un registro previo de tabulación");
		}
		if (!respuestaHm.isEmpty()) {
			return respuestaHm;
		}

		return new CierreGerenciaModel(cobranzaAgencia, detalleCierreAgenciasCf.join(), flujoEfectivoCf.join(),
				idTabulacionDineroCf.join(), usuarioId);
	}
}

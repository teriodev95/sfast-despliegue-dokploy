package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.DTOs.numeros_gerencia.NumerosGerenciaDto;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Service
public class NumerosGerenciaService {
    private final CalendarioService calendarioService;
    private final CobranzaAgenciaService cobranzaAgenciaService;
    private final VentaService ventaService;

    public NumerosGerenciaService(CalendarioService calendarioService, CobranzaAgenciaService cobranzaAgenciaService,
            VentaService ventaService) {
        this.calendarioService = calendarioService;
        this.cobranzaAgenciaService = cobranzaAgenciaService;
        this.ventaService = ventaService;
    }

    public NumerosGerenciaDto getNumerosGerenciaByGerencia(String gerencia) {
        CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        NumerosGerenciaDto numerosGerenciaDto = new NumerosGerenciaDto();
        if (calendarioModel != null) {
            numerosGerenciaDto = getNumerosGerenciaByGerenciaAnioSemana(gerencia, anio, semana);
        }

        return numerosGerenciaDto;
    }

    public NumerosGerenciaDto getNumerosGerenciaByGerenciaAnioSemana(String gerencia, int anio, int semana) {
        // To easy code
        CalendarioModel semanaActualCalendarioModel = new CalendarioModel();
        semanaActualCalendarioModel.setAnio(anio);
        semanaActualCalendarioModel.setSemana(semana);
        CalendarioModel semanaAnteriorCalendarioModel = new CalendarioModel(semanaActualCalendarioModel);
        MyUtil.funSemanaAnterior(semanaAnteriorCalendarioModel);
        int anioSemanaAnterior = semanaAnteriorCalendarioModel.getAnio();
        int semanaAnterior = semanaAnteriorCalendarioModel.getSemana();

        CompletableFuture<List<CobranzaAgencia>> cobranzaAgenciasSemanaAnteriorCf = this.cobranzaAgenciaService
                .getCobranzaAgenciasByGerenciaAnioSemanaAsync(gerencia, anioSemanaAnterior, semanaAnterior);
        CompletableFuture<List<CobranzaAgencia>> cobranzaAgenciasSemanaActualCf = this.cobranzaAgenciaService
                .getCobranzaAgenciasByGerenciaAnioSemanaAsync(gerencia, anio, semana);
        CompletableFuture<List<VentaModel>> ventaModelsCf = this.ventaService.findByGerenciaAnioSemanaAsync(gerencia,
                anio, semana);

        return new NumerosGerenciaDto(cobranzaAgenciasSemanaAnteriorCf.join(), cobranzaAgenciasSemanaActualCf.join(),
                ventaModelsCf.join());
    }

}

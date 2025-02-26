package tech.calaverita.sfast_xpress.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.DTOs.balance_general.BalanceGeneralAgenciaDto;
import tech.calaverita.sfast_xpress.DTOs.balance_general.BalanceGeneralDto;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.GerenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.pojos.AlmacenObjects;
import tech.calaverita.sfast_xpress.pojos.CobranzaAgencia;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Service
public class BalanceGeneralService {
    private final CalendarioService calendarioService;
    private final CobranzaAgenciaService cobranzaAgenciaService;
    private final UsuarioService usuarioService;

    public BalanceGeneralService(CalendarioService calendarioService, CobranzaAgenciaService cobranzaAgenciaService,
            UsuarioService usuarioService) {
        this.calendarioService = calendarioService;
        this.cobranzaAgenciaService = cobranzaAgenciaService;
        this.usuarioService = usuarioService;
    }

    public BalanceGeneralDto getBalanceGeneralByGerencia(String gerencia) {
        CalendarioModel calendarioModel = this.calendarioService
                .findByFechaActual(LocalDate.now().toString());

        // To easy code
        int anio = calendarioModel.getAnio();
        int semana = calendarioModel.getSemana();

        BalanceGeneralDto balanceGeneralDto = new BalanceGeneralDto();
        if (calendarioModel != null) {
            balanceGeneralDto = getBalanceGeneralByGerenciaAnioSemana(gerencia, anio, semana);
        }

        return balanceGeneralDto;
    }

    public BalanceGeneralDto getBalanceGeneralByGerenciaAnioSemana(String gerencia, int anio, int semana) {
        // To easy code
        CalendarioModel semanaActualCalendarioModel = new CalendarioModel();
        semanaActualCalendarioModel.setAnio(anio);
        semanaActualCalendarioModel.setSemana(semana);
        CalendarioModel semanaAnteriorCalendarioModel = new CalendarioModel(semanaActualCalendarioModel);
        MyUtil.funSemanaAnterior(semanaAnteriorCalendarioModel);
        int anioSemanaAnterior = semanaAnteriorCalendarioModel.getAnio();
        int semanaAnterior = semanaAnteriorCalendarioModel.getSemana();

        CompletableFuture<UsuarioModel> usuarioModelCf = this.usuarioService.findByGerenciaTipoAndStatusAsync(gerencia,
                "Gerente", true);

        CompletableFuture<List<CobranzaAgencia>> cobranzaAgenciasSemanaActualCf = this.cobranzaAgenciaService
                .getCobranzaAgenciasByGerenciaAnioSemanaAsync(gerencia, anio, semana);
        CompletableFuture<List<CobranzaAgencia>> cobranzaAgenciasSemanaAnteriorCf = this.cobranzaAgenciaService
                .getCobranzaAgenciasByGerenciaAnioSemanaAsync(gerencia, anioSemanaAnterior, semanaAnterior);

        List<BalanceGeneralAgenciaDto> balanceGeneralAgenciaDtos = new ArrayList<>();
        BalanceGeneralAgenciaDto balanceGeneralAgenciaDto = new BalanceGeneralAgenciaDto();
        for (CobranzaAgencia cobranzaAgenciaSemanaActual : cobranzaAgenciasSemanaActualCf.join()) {
            for (CobranzaAgencia cobranzaAgenciaSemanaAnterior : cobranzaAgenciasSemanaAnteriorCf.join()) {
                if (cobranzaAgenciaSemanaActual.getAgencia().equals(cobranzaAgenciaSemanaAnterior.getAgencia())) {
                    balanceGeneralAgenciaDto = new BalanceGeneralAgenciaDto(
                            cobranzaAgenciaSemanaActual, cobranzaAgenciaSemanaAnterior);
                    balanceGeneralAgenciaDtos.add(balanceGeneralAgenciaDto);
                    break;
                }
            }
        }

        // To easy code
        UsuarioModel usuarioModel = usuarioModelCf.join();
        GerenciaModel gerenciaModel = usuarioModel.getGerenciaModel();
        String sucursal = gerenciaModel.getSucursal().toUpperCase();

        AlmacenObjects almacenObjects = new AlmacenObjects();
        almacenObjects.addObject("semana", semana);
        almacenObjects.addObject("anio", anio);
        almacenObjects.addObject("sucursal", sucursal);
        almacenObjects.addObject("gerente", usuarioModelCf.join().getNombre());
        almacenObjects.addObject("zona", gerencia);

        return new BalanceGeneralDto(almacenObjects, balanceGeneralAgenciaDtos);
    }
}

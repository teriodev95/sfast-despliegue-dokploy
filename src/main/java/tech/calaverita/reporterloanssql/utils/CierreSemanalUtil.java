package tech.calaverita.reporterloanssql.utils;

import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.*;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.*;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CierreSemanalUtil {
    private static BalanceAgenciaService balanceAgenciaService;
    private static CierreSemanalService cierreSemanalService;
    private static EgresosAgenteService egresosAgenteService;
    private static EgresosGerenteService egresosGerenteService;
    private static IngresosAgenteService ingresosAgenteService;
    private static PagoService pagoService;

    public CierreSemanalUtil(
            BalanceAgenciaService balanceAgenciaService,
            CierreSemanalService cierreSemanalService,
            EgresosAgenteService egresosAgenteService,
            EgresosGerenteService egresosGerenteService,
            IngresosAgenteService ingresosAgenteService,
            PagoService pagoService
    ) {
        CierreSemanalUtil.balanceAgenciaService = balanceAgenciaService;
        CierreSemanalUtil.cierreSemanalService = cierreSemanalService;
        CierreSemanalUtil.egresosAgenteService = egresosAgenteService;
        CierreSemanalUtil.egresosGerenteService = egresosGerenteService;
        CierreSemanalUtil.ingresosAgenteService = ingresosAgenteService;
        CierreSemanalUtil.pagoService = pagoService;
    }

    public static CierreSemanalDTO getCierreSemanalDTO(
            CierreSemanalEntity cierreSemanalEntity
    ) throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = CierreSemanalUtil.cierreSemanalService
                .getCierreSemanalDTO(cierreSemanalEntity);

        CompletableFuture<Optional<BalanceAgenciaEntity>> balanceAgenciaEntity = CierreSemanalUtil.balanceAgenciaService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<EgresosAgenteEntity>> egresosAgenteEntity = CierreSemanalUtil.egresosAgenteService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<EgresosGerenteEntity>> egresosGerenteEntity = CierreSemanalUtil.egresosGerenteService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<IngresosAgenteEntity>> ingresosAgenteEntity = CierreSemanalUtil.ingresosAgenteService
                .findById(cierreSemanalEntity.getId());

        CompletableFuture.allOf(balanceAgenciaEntity, egresosAgenteEntity, egresosGerenteEntity,
                ingresosAgenteEntity);

        BalanceAgenciaDTO balanceAgenciaDTO = CierreSemanalUtil.balanceAgenciaService
                .getBalanceAgenciaDTO(balanceAgenciaEntity.get().get());
        EgresosAgenteDTO egresosAgenteDTO = CierreSemanalUtil.egresosAgenteService
                .getEgresosGerenteDTO(egresosAgenteEntity.get().get());
        EgresosGerenteDTO egresosGerenteDTO = CierreSemanalUtil.egresosGerenteService
                .getEgresosGerenteDTO(egresosGerenteEntity.get().get());
        IngresosAgenteDTO ingresosAgenteDTO = CierreSemanalUtil.ingresosAgenteService
                .getIngresosAgenteDTO(ingresosAgenteEntity.get().get());

        cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
        cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
        cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
        cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);
        cierreSemanalDTO.setIsAgenciaCerrada(true);

        return cierreSemanalDTO;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static CierreSemanalDTO getCierreSemanalDTO(
            Dashboard dashboard,
            List<UsuarioEntity> darrusuarEnt,
            double asignaciones
    ) throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();

        // To easy code
        CompletableFuture<Integer> clientePagoCompleto = CierreSemanalUtil.pagoService
                .getClientesPagoCompletoByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());
        CompletableFuture<Double> cobranzaTotal = CierreSemanalUtil.pagoService
                .getCobranzaTotalByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());

        // To easy code
        UsuarioEntity agenteUsuarioEntity = darrusuarEnt.get(0);
        UsuarioEntity gerenteUsuarioEntity = darrusuarEnt.get(1);
        String nombreAgente = agenteUsuarioEntity.getNombre() + " " + agenteUsuarioEntity.getApellidoPaterno() + " "
                + agenteUsuarioEntity.getApellidoMaterno();
        String nombreGerente = gerenteUsuarioEntity.getNombre() + " " + gerenteUsuarioEntity.getApellidoPaterno()
                + " " + gerenteUsuarioEntity.getApellidoMaterno();

        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        {
            balanceAgenciaDTO.setZona(dashboard.getGerencia());
            balanceAgenciaDTO.setGerente(nombreGerente);
            balanceAgenciaDTO.setAgencia(dashboard.getAgencia());
            balanceAgenciaDTO.setAgente(nombreAgente);
            balanceAgenciaDTO.setRendimiento(dashboard.getRendimiento());
            balanceAgenciaDTO.setNivel(BalanceAgenciaUtil.getNivelAgente(dashboard.getClientes(),
                    dashboard.getRendimiento() / 100, agenteUsuarioEntity));
            balanceAgenciaDTO.setClientes(dashboard.getClientes());
            balanceAgenciaDTO.setPagosReducidos(dashboard.getPagosReducidos());
            balanceAgenciaDTO.setNoPagos(dashboard.getNoPagos());
            balanceAgenciaDTO.setLiquidaciones(dashboard.getNumeroLiquidaciones());
        }

        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        {
            egresosAgenteDTO.setAsignaciones(asignaciones);

            cobranzaTotal.join();
            egresosAgenteDTO.setEfectivoEntregadoCierre(cobranzaTotal.get() - egresosAgenteDTO.getAsignaciones());
        }

        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        {
            egresosGerenteDTO.setPorcentajeComisionCobranza(BalanceAgenciaUtil
                    .getPorcentajeComisionCobranza(balanceAgenciaDTO.getNivel()));

            clientePagoCompleto.join();

            // To easy code
            int porcentajeBonoMensual = BalanceAgenciaUtil.getPorcentajeBonoMensual(clientePagoCompleto.get(),
                    dashboard.getRendimiento(), agenteUsuarioEntity);

            egresosGerenteDTO.setPorcentajeBonoMensual(porcentajeBonoMensual);

            cobranzaTotal.join();

            egresosGerenteDTO.setPagoComisionCobranza(cobranzaTotal.get() / 100
                    * egresosGerenteDTO.getPorcentajeComisionCobranza());
            egresosGerenteDTO.setBonos(cobranzaTotal.get() / 100 * egresosGerenteDTO.getPorcentajeBonoMensual());
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
        cierreSemanalDTO.setPinAgente(agenteUsuarioEntity.getPin());
        cierreSemanalDTO.setIsAgenciaCerrada(false);
        cierreSemanalDTO.setDia(LocalDate.now().getDayOfMonth());

        String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es",
                "ES"));
        String primeraLetra = mes.substring(0, 1);
        String mayuscula = primeraLetra.toUpperCase();
        String demasLetras = mes.substring(1, mes.length());
        mes = mayuscula + demasLetras;
        cierreSemanalDTO.setMes(mes);

        return cierreSemanalDTO;
    }
}

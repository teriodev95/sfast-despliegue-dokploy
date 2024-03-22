package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.mappers.PagoMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.PagoModel;
import tech.calaverita.reporterloanssql.models.view.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.models.view.PagoUtilModel;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PagoService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoRepository repo;
    private final PagoMapper mapper;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public PagoService(
            PagoRepository repo,
            PagoMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public PagoModel save(
            PagoModel pagMod_I
    ) {
        return this.repo.save(pagMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<PagoModel> optpagModFindById(
            String strId_I
    ) {
        return this.repo.optpagEntFindById(strId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PagoModel pagModFindByAgenteAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.pagEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByPrestamoIdAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoModel> darrpagAgrModGetHistorialDePagosToApp(
            String strPrestamoId_I
    ) {
        return this.repo.darrpagAgrEntGetHistorialDePagosToApp(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.repo.darrpagEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoModel> darrpagAgrModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagAgrEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoModel> darrpagAgrModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.repo.darrpagAgrEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByPrestamoIdAnioSemanaAndNoPrimerPago(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagEntFindByPrestamoIdAnioSemanaAndNoPrimerPago(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoModel> darrpagAgrModGetHistorialDePagosToPGS(
            String strPrestamoId_I
    ) {
        return this.repo.darrpagAgrEntGetHistorialDePagosToPGS(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoUtilModel> darrpagUtilModFindByAgenciaAnioAndSemanaToCobranza(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoUtilModel> darrpagUtilModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.repo.darrpagEntFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.repo.darrpagEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoModel> darrpagModFindByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.repo.darrpagEntFindByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public double numGetSaldoFromPrestamoByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.repo.numGetSaldoFromPrestamoByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getDebitoTotalByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Double debitoTotal = this.repo.getDebitoTotalByAgenciaAnioAndSemana(agencia, anio, semana);
        debitoTotal = debitoTotal == null ? 0 : debitoTotal;

        return CompletableFuture.completedFuture(debitoTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getExcedenteByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Double excedente = this.repo.getExcedenteByAgenciaAnioAndSemana(agencia, anio, semana);
        excedente = excedente == null ? 0 : excedente;

        return CompletableFuture.completedFuture(excedente);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getCobranzaTotalByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Double cobranzaTotal = this.repo.getCobranzaTotalByAgenciaAnioAndSemana(agencia, anio, semana);
        cobranzaTotal = cobranzaTotal == null ? 0 : cobranzaTotal;

        return CompletableFuture.completedFuture(cobranzaTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesCobradosByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Integer clientes = this.repo.getClientesCobradosAgenciaAnioAndSemana(agencia, anio, semana);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesPagoCompletoByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Integer clientes = this.repo.getClientesPagoCompletoByAgenciaAnioAndSemana(agencia, anio, semana);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesTotalesByAgenciaAnioAndSemanaAsync(
            String agencia,
            int anio,
            int semana
    ) {
        Integer clientes = this.repo.getClientesTotalesByAgenciaAnioAndSemana(agencia, anio, semana);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getDebitoTotalParcialByGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double debitoTotal = this.repo.getDebitoTotalParcialByGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);
        debitoTotal = debitoTotal == null ? 0 : debitoTotal;

        return CompletableFuture.completedFuture(debitoTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getDebitoTotalSemanaByGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double debitoTotal = this.repo.getDebitoTotalSemanaByGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);
        debitoTotal = debitoTotal == null ? 0 : debitoTotal;

        return CompletableFuture.completedFuture(debitoTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getExcedenteByGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double excedente = this.repo.getExcedenteByGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);
        excedente = excedente == null ? 0 : excedente;

        return CompletableFuture.completedFuture(excedente);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getCobranzaTotalByGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double cobranzaTotal = this.repo.getCobranzaTotalByGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);
        cobranzaTotal = cobranzaTotal == null ? 0 : cobranzaTotal;

        return CompletableFuture.completedFuture(cobranzaTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesCobradosGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Integer clientes = this.repo.getClientesCobradosGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesTotalesGerenciaAnioAndSemanaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Integer clientes = this.repo.getClientesTotalesGerenciaAnioAndSemana(gerencia, sucursal, anio, semana);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getDebitoTotalParcialByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double debitoTotal = this.repo.getDebitoTotalParcialByGerenciaAnioSemana(gerencia, sucursal, anio, semana);
        debitoTotal = debitoTotal == null ? 0 : debitoTotal;

        return CompletableFuture.completedFuture(debitoTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getDebitoTotalSemanaByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana
    ) {
        Double debitoTotal = this.repo.getDebitoTotalSemanaByGerenciaAnioSemana(gerencia, sucursal, anio, semana);
        debitoTotal = debitoTotal == null ? 0 : debitoTotal;

        return CompletableFuture.completedFuture(debitoTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getExcedenteByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    ) {
        Double excedente = this.repo.getExcedenteByGerenciaAnioSemanaAndFecha(gerencia, sucursal, anio, semana, fecha);
        excedente = excedente == null ? 0 : excedente;

        return CompletableFuture.completedFuture(excedente);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Double> getCobranzaTotalByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    ) {
        Double cobranzaTotal = this.repo.getCobranzaTotalByGerenciaAnioSemanaAndFecha(gerencia, sucursal, anio, semana,
                fecha);
        cobranzaTotal = cobranzaTotal == null ? 0 : cobranzaTotal;

        return CompletableFuture.completedFuture(cobranzaTotal);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesCobradosByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    ) {
        Integer clientes = this.repo.getClientesCobradosGerenciaAnioSemanaAndFecha(gerencia, sucursal, anio, semana,
                fecha);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Integer> getClientesTotalesByGerenciaAnioSemanaAndFechaAsync(
            String gerencia,
            String sucursal,
            int anio,
            int semana,
            String fecha
    ) {
        Integer clientes = this.repo.getClientesTotalesGerenciaAnioSemanaAndFecha(gerencia, sucursal, anio, semana,
                fecha);

        return CompletableFuture.completedFuture(clientes);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PagoModel getPagoEntity(
            LiquidacionDTO liquidacionDTO
    ) {
        return this.mapper.mapIn(liquidacionDTO);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<PagoModel>> findByGerenciasAnioSemanaAndTipoAsync(ArrayList<String> gerencias, int anio, int semana) {
        String tipo = "No_pago";
        return CompletableFuture.completedFuture(this.repo.findByGerenciasAndAnioAndSemanaAndTipo(gerencias, anio, semana, tipo));
    }
}

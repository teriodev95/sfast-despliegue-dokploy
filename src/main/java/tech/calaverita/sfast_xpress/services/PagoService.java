package tech.calaverita.sfast_xpress.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.dto.LiquidacionDTO;
import tech.calaverita.sfast_xpress.mappers.PagoMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.PagoModel;
import tech.calaverita.sfast_xpress.repositories.PagoRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class PagoService {
    private final PagoRepository repo;
    private final PagoMapper mapper;

    public PagoService(PagoRepository repo, PagoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public PagoModel save(PagoModel pagoModel) {
        return this.repo.save(pagoModel);
    }

    public boolean existsById(String id) {
        return this.repo.existsById(id);
    }

    public PagoModel findById(String id) {
        return this.repo.findById(id).orElse(null);
    }

    public PagoModel findByPrestamoIdAnioSemanaAndCreadoDesde(String prestamoId, int anio, int semana, String creadoDesde) {
        return this.repo.findByPrestamoIdAndAnioAndSemanaAndCreadoDesde(prestamoId, anio, semana, creadoDesde);
    }

    public ArrayList<PagoModel> findByPrestamoIdAnioAndSemanaOrderByFechaPagoDesc(String prestamoId, int anio, int semana) {
        return this.repo.findByPrestamoIdAndAnioAndSemanaOrderByFechaPagoDesc(prestamoId, anio, semana);
    }

    public ArrayList<PagoModel> findByAgenciaAnioSemanaAndEsPrimerPago(String agencia, int anio, int semana,
                                                                       boolean esPrimerPago) {
        return this.repo.findByAgenteAndAnioAndSemanaAndEsPrimerPago(agencia, anio, semana, esPrimerPago);
    }

    public ArrayList<PagoModel> findByPrestamoIdAnioSemanaAndEsPrimerPago(String prestamoId, int anio,
                                                                          int semana, boolean esPrimerPago) {
        return this.repo.findByPrestamoIdAndAnioAndSemanaAndEsPrimerPago(prestamoId, anio, semana, esPrimerPago);
    }

    public ArrayList<PagoModel> findByAgenteFechaPagoAndEsPrimerPagoInnerJoinPagoModel(String agencia, String fechaPago,
                                                                                       boolean esPrimerPago) {
        return this.repo.findByAgenteAndFechaPagoAndEsPrimerPagoInnerJoinPagoModel(agencia, fechaPago, esPrimerPago);
    }

    public ArrayList<PagoModel> findByAgenteAnioAndSemanaInnerJoinLiquidacionModel(String agencia, int anio,
                                                                                   int semana) {
        return this.repo.findByAgenteAndAnioAndSemanaInnerJoinLiquidacionModel(agencia, anio, semana);
    }

    public ArrayList<PagoModel> findByAgenteAndFechaPagoInnerJoinLiquidacionModel(String agencia, String fechaPago) {
        return this.repo.findByAgenteAndFechaPagoInnerJoinLiquidacionModel(agencia, fechaPago);
    }

    public double findCobradoByPrestamoId(String prestamoId) {
        return this.repo.findCobradoByPrestamoId(prestamoId);
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<PagoModel>> findByGerenciasAnioSemanaAndTipoAsync(ArrayList<String> gerencias,
                                                                                         int anio, int semana) {
        String tipo = "No_pago";
        return CompletableFuture.completedFuture(this.repo.findByGerenciasAndAnioAndSemanaAndTipo(gerencias, anio,
                semana, tipo));
    }

    public PagoModel getPagoEntity(LiquidacionDTO liquidacionDTO) {
        return this.mapper.mapIn(liquidacionDTO);
    }
}

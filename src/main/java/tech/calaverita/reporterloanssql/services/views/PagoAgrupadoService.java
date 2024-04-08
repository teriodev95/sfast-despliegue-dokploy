package tech.calaverita.reporterloanssql.services.views;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.views.PagoAgrupadoModel;
import tech.calaverita.reporterloanssql.repositories.views.PagoAgrupadoRepository;

import java.util.ArrayList;

@Service
public class PagoAgrupadoService {
    private final PagoAgrupadoRepository repo;

    public PagoAgrupadoService(PagoAgrupadoRepository repo) {
        this.repo = repo;
    }

    public ArrayList<PagoAgrupadoModel> findByPrestamoIdOrderByAnioAscSemanaAsc(String prestamoId) {
        return this.repo.findByPrestamoIdOrderByAnioAscSemanaAsc(prestamoId);
    }

    public ArrayList<PagoAgrupadoModel> findByPrestamoIdAndEsPrimerPagoOrderByAnioDescSemanaDesc(String prestamoId,
                                                                                                 boolean esPrimerPago) {
        return this.repo.findByPrestamoIdAndEsPrimerPagoOrderByAnioDescSemanaDesc(prestamoId, esPrimerPago);
    }
}

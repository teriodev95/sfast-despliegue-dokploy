package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.VentaModel;
import tech.calaverita.sfast_xpress.repositories.VentaRepository;

@Service
public class VentaService {
    private final VentaRepository repo;

    public VentaService(VentaRepository repo) {
        this.repo = repo;
    }

    public VentaModel save(VentaModel ventaModel) {
        return this.repo.save(ventaModel);
    }

    public ArrayList<VentaModel> findByGerenciaAnioAndSemana(String gerencia, int anio, int semana) {
        return this.repo.findByGerenciaAndAnioAndSemana(gerencia, anio, semana);
    }
}

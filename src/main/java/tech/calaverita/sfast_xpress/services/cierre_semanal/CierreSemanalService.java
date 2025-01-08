package tech.calaverita.sfast_xpress.services.cierre_semanal;

import java.util.Optional;

import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.mappers.cierre_semanal.CierreSemanalMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.repositories.cierre_semanal.CierreSemanalRepository;

@Service
public class CierreSemanalService {
    private final CierreSemanalRepository repo;
    private final CierreSemanalMapper mapper;

    public CierreSemanalService(
            CierreSemanalRepository repo,
            CierreSemanalMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Boolean existsById(String id) {
        return this.repo.existsById(id);
    }

    public void save(
            CierreSemanalModel cierSemEnt_I) {
        this.repo.save(cierSemEnt_I);
    }

    public Optional<CierreSemanalModel> findById(
            String id) {
        return this.repo.findById(id);
    }

    public Optional<CierreSemanalModel> findByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana) {
        return this.repo.findByAgenciaAndAnioAndSemana(agencia, anio, semana);
    }

    public Double findComisionCobranzaAgenciaByAgenciaAnioAndSemana(String agencia, int anio, int semana) {
        Double pagoComisionCobranza = this.repo.findComisionCobranzaAgenciaByAgenciaAndAnioAndSemana(agencia, anio,
                semana);

        return pagoComisionCobranza == null ? 0D : pagoComisionCobranza;
    }

    public CierreSemanalDTO getCierreSemanalDTO(
            CierreSemanalModel entity) {
        return this.mapper.mapOut(entity);
    }

    public CierreSemanalModel getCierreSemanalEntity(
            CierreSemanalDTO DTO) {
        return this.mapper.mapIn(DTO);
    }
}

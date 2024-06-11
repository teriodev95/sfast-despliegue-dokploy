package tech.calaverita.sfast_xpress.services.cierre_semanal;

import org.springframework.stereotype.Service;
import tech.calaverita.sfast_xpress.DTOs.cierre_semanal.CierreSemanalDTO;
import tech.calaverita.sfast_xpress.mappers.cierre_semanal.CierreSemanalMapper;
import tech.calaverita.sfast_xpress.models.mariaDB.cierre_semanal.CierreSemanalModel;
import tech.calaverita.sfast_xpress.repositories.cierre_semanal.CierreSemanalRepository;

import java.util.Optional;

@Service
public class CierreSemanalService {
    private final CierreSemanalRepository repo;
    private final CierreSemanalMapper mapper;

    public CierreSemanalService(
            CierreSemanalRepository repo,
            CierreSemanalMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public void save(
            CierreSemanalModel cierSemEnt_I
    ) {
        this.repo.save(cierSemEnt_I);
    }

    public Optional<CierreSemanalModel> findById(
            String id
    ) {
        return this.repo.findById(id);
    }

    public Optional<CierreSemanalModel> findByAgenciaAnioAndSemana(
            String agencia,
            int anio,
            int semana
    ) {
        return this.repo.findByAgenciaAnioAndSemana(agencia + "-" + anio + "-" + semana);
    }

    public CierreSemanalDTO getCierreSemanalDTO(
            CierreSemanalModel entity
    ) {
        return this.mapper.mapOut(entity);
    }

    public CierreSemanalModel getCierreSemanalEntity(
            CierreSemanalDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }
}

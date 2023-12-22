package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.persistence.mappers.LiquidacionMapper;
import tech.calaverita.reporterloanssql.persistence.repositories.LiquidacionRepository;
import tech.calaverita.reporterloanssql.utils.LiquidacionUtil;

import java.util.ArrayList;

@Service
public final class LiquidacionService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final LiquidacionRepository repo;
    private final LiquidacionMapper mapper;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private LiquidacionService(
            LiquidacionRepository repo,
            LiquidacionMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public LiquidacionEntity save(
            LiquidacionEntity liqMod_I
    ) {
        return this.repo.save(liqMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionEntity> darrliqModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {

        return this.repo.darrliqEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionEntity> darrliqModFindByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.repo.darrliqEntFindByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public LiquidacionEntity getLiquidacionEntity(
            LiquidacionDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public LiquidacionDTO getLiquidacionDTO(
            PrestamoEntity entity
    ) {
        return this.mapper.mapOut(entity);
    }
}

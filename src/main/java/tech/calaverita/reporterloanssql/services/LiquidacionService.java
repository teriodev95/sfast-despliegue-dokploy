package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.dto.LiquidacionDTO;
import tech.calaverita.reporterloanssql.mappers.LiquidacionMapper;
import tech.calaverita.reporterloanssql.models.mariaDB.LiquidacionModel;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.LiquidacionRepository;

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
    public LiquidacionModel save(
            LiquidacionModel liqMod_I
    ) {
        return this.repo.save(liqMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionModel> darrliqModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {

        return this.repo.darrliqEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionModel> darrliqModFindByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.repo.darrliqEntFindByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public LiquidacionModel getLiquidacionEntity(
            LiquidacionDTO DTO
    ) {
        return this.mapper.mapIn(DTO);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public LiquidacionDTO getLiquidacionDTO(
            PrestamoModel entity
    ) {
        return this.mapper.mapOut(entity);
    }
}

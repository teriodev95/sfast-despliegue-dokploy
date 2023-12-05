package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.LiquidacionEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.LiquidacionRepository;

import java.util.ArrayList;

@Service
public final class LiquidacionService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final LiquidacionRepository liqRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private LiquidacionService(
            LiquidacionRepository liqRepo_S
    ) {
        this.liqRepo = liqRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public void save(
            LiquidacionEntity liqMod_I
    ) {
        this.liqRepo.save(liqMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionEntity> darrliqModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {

        return this.liqRepo.darrliqEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<LiquidacionEntity> darrliqModFindByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.liqRepo.darrliqEntFindByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }
}

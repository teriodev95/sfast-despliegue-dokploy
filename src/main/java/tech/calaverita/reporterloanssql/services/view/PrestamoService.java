package tech.calaverita.reporterloanssql.services.view;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PrestamoUtilEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.view.PrestRepoPrestamoRepository;

import java.util.ArrayList;

@Service
public final class PrestamoService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PrestRepoPrestamoRepository prestRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PrestamoService(
            PrestRepoPrestamoRepository prestRepo_S
    ) {
        this.prestRepo = prestRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public ArrayList<PrestamoUtilEntity> darrprestUtilModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.prestRepo.darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoUtilEntity> darrPrestUtilModFindByAgenciaAnioAndSemanaToCobranza(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.prestRepo.darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoEntity> darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I) {
        return this.prestRepo.darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoUtilEntity> darrprestUtilModByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.prestRepo.darrprestUtilEntByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PrestamoEntity prestModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.prestRepo.prestEntFindByPrestamoId(strPrestamoId_I);
    }
}

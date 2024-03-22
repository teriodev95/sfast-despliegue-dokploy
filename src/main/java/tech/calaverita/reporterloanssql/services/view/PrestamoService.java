package tech.calaverita.reporterloanssql.services.view;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.view.PrestamoModel;
import tech.calaverita.reporterloanssql.models.view.PrestamoUtilModel;
import tech.calaverita.reporterloanssql.repositories.view.PrestRepoPrestamoRepository;

import java.util.ArrayList;
import java.util.Optional;

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
    public ArrayList<PrestamoUtilModel> darrprestUtilModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.prestRepo.darrprestUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoUtilModel> darrPrestUtilModFindByAgenciaAnioAndSemanaToCobranza(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.prestRepo.darrprestUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoModel> darrprestModFindByAgenciaAnioAndSemanaToCobranzaPGS(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I) {
        return this.prestRepo.darrprestEntFindByAgenciaAnioAndSemanaToCobranzaPGS(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PrestamoUtilModel> darrprestUtilModByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.prestRepo.darrprestUtilEntByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PrestamoModel prestModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.prestRepo.prestEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<PrestamoModel> findById(
            String strPrestamoId_I
    ) {
        return this.prestRepo.findById(strPrestamoId_I);
    }
}

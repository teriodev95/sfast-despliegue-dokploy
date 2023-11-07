package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.PagoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoAgrupadoEntity;
import tech.calaverita.reporterloanssql.persistence.entities.view.PagoUtilEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.PagoRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public final class PagoService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final PagoRepository pagRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private PagoService(
            PagoRepository pagRepo_S
    ) {
        this.pagRepo = pagRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public void save(
            PagoEntity pagMod_I
    ) {
        this.pagRepo.save(pagMod_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<PagoEntity> optpagModFindById(
            String strId_I
    ) {
        return this.pagRepo.optpagEntFindById(strId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public PagoEntity pagModFindByAgenteAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.pagEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByPrestamoIdAnioAndSemana(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagEntFindByPrestamoIdAnioAndSemana(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoEntity> darrpagAgrModGetHistorialDePagosToApp(
            String strPrestamoId_I
    ) {
        return this.pagRepo.darrpagAgrEntGetHistorialDePagosToApp(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.pagRepo.darrpagEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoEntity> darrpagAgrModFindByAgenciaAnioAndSemana(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagAgrEntFindByAgenciaAnioAndSemana(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoEntity> darrpagAgrModFindByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.pagRepo.darrpagAgrEntFindByPrestamoId(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByPrestamoIdAnioSemanaAndNoPrimerPago(
            String strPrestamoId_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagEntFindByPrestamoIdAnioSemanaAndNoPrimerPago(strPrestamoId_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoAgrupadoEntity> darrpagAgrModGetHistorialDePagosToPGS(
            String strPrestamoId_I
    ) {
        return this.pagRepo.darrpagAgrEntGetHistorialDePagosToPGS(strPrestamoId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoUtilEntity> darrpagUtilModFindByAgenciaAnioAndSemanaToCobranza(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagUtilEntFindByAgenciaAnioAndSemanaToCobranza(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoUtilEntity> darrpagUtilModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagUtilEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.pagRepo.darrpagEntFindByAgenciaFechaPagoAndNoPrimerPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByAgenciaAnioAndSemanaToDashboard(
            String strAgencia_I,
            int intAnio_I,
            int intSemana_I
    ) {
        return this.pagRepo.darrpagEntFindByAgenciaAnioAndSemanaToDashboard(strAgencia_I, intAnio_I, intSemana_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<PagoEntity> darrpagModFindByAgenciaAndFechaPagoToDashboard(
            String strAgencia_I,
            String strFechaPago_I
    ) {
        return this.pagRepo.darrpagEntFindByAgenciaAndFechaPagoToDashboard(strAgencia_I, strFechaPago_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public double numGetSaldoFromPrestamoByPrestamoId(
            String strPrestamoId_I
    ) {
        return this.pagRepo.numGetSaldoFromPrestamoByPrestamoId(strPrestamoId_I);
    }
}

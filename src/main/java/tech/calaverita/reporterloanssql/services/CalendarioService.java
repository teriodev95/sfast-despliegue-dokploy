package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.CalendarioEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.CalendarioRepository;

@Service
public final class CalendarioService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final CalendarioRepository calRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public CalendarioService(
            CalendarioRepository calRepo_S
    ) {
        this.calRepo = calRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public CalendarioEntity calModFindBySemanaActualXpressByFechaActual(
            String fechaActual_I
    ) {
        return this.calRepo.calModFindBySemanaActualXpressByFechaActual(fechaActual_I);
    }
}

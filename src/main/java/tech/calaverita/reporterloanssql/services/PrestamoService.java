package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    public PrestamoModel getPrestamoModelByPrestamoId(String prestamoId){

        return prestamoRepository.getPrestamoByPrestamoId(prestamoId);
    }
}

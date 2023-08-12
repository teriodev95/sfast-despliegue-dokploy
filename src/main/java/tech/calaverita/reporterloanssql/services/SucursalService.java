package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.SucursalModel;
import tech.calaverita.reporterloanssql.repositories.SucursalRepository;

@Service
public class SucursalService {
    private static SucursalRepository sucursalRepository;

    @Autowired
    public SucursalService(SucursalRepository sucursalRepository) {
        SucursalService.sucursalRepository = sucursalRepository;
    }

    public static SucursalModel findOneBySucursalId(String sucursalId) {
        return sucursalRepository.findOneBySucursalId(sucursalId);
    }
}

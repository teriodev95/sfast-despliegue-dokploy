package tech.calaverita.reporterloanssql.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.XpressRepository;

@Service
public class XpressService {
    private static XpressRepository xpressRepository;

    @Autowired
    public XpressService(XpressRepository xpressRepository) {
        XpressService.xpressRepository = xpressRepository;
    }
}

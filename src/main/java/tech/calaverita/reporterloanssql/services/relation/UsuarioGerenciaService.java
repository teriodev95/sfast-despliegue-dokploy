package tech.calaverita.reporterloanssql.services.relation;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.repositories.relation.UsuarioGerenciaRepository;

import java.util.ArrayList;

@Service
public final class UsuarioGerenciaService {
    private final UsuarioGerenciaRepository repo;

    private UsuarioGerenciaService(
            UsuarioGerenciaRepository repo
    ) {
        this.repo = repo;
    }

    public ArrayList<String> darrstrGerenciaIdFindByUsuarioId(
            int usuarioId
    ) {
        return this.repo.darrstrGerenciaIdFindByUsuarioId(usuarioId);
    }
}

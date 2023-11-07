package tech.calaverita.reporterloanssql.services;

import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public final class UsuarioService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarioRepository usuarRepo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    private UsuarioService(
            UsuarioRepository usuarRepo_S
    ) {
        this.usuarRepo = usuarRepo_S;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public Iterable<UsuarioEntity> iteausuarEntFindAll() {
        return this.usuarRepo.findAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<UsuarioEntity> optusuarEntFindById(
            int intId_I
    ) {
        return this.usuarRepo.findById(intId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioAndPin(
            String usuario,
            String pin
    ) {
        return this.usuarRepo.usuarEntFindByUsuarioAndPin(usuario, pin);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuario(
            String usuario
    ) {
        return this.usuarRepo.optusuarEntFindByUsuario(usuario).orElseThrow();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioEntity> darrusuarModFindByTipo(
            String tipo
    ) {
        return this.usuarRepo.darrusuarEntFindByTipo(tipo);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioId(
            int usuarioId
    ) {
        return this.usuarRepo.usuarEntFindByUsuarioId(usuarioId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioIdFromGerenciaIdOfGerenciaModel(
            String gerenciaId
    ) {
        return this.usuarRepo.usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(gerenciaId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioEntity> darrUsuarModFindByGerencia(
            String gerencia
    ) {
        return this.usuarRepo.darrusuarEntFindByGerencia(gerencia);
    }
}

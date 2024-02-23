package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UsuarioService {
    //------------------------------------------------------------------------------------------------------------------
    /*INSTANCE VARIABLES*/
    //------------------------------------------------------------------------------------------------------------------
    private final UsuarioRepository repo;

    //------------------------------------------------------------------------------------------------------------------
    /*CONSTRUCTORS*/
    //------------------------------------------------------------------------------------------------------------------
    public UsuarioService(
            UsuarioRepository repo
    ) {
        this.repo = repo;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*METHODS*/
    //------------------------------------------------------------------------------------------------------------------
    public Iterable<UsuarioEntity> findAll() {
        return this.repo.findAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<UsuarioEntity> optusuarEntFindById(
            int intId_I
    ) {
        return this.repo.findById(intId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioAndPin(
            String usuario,
            String pin
    ) {
        return this.repo.usuarEntFindByUsuarioAndPin(usuario, pin);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuario(
            String usuario
    ) {
        return this.repo.optusuarEntFindByUsuario(usuario).orElseThrow();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<UsuarioEntity> findByAgencia(
            String agencia
    ) {
        return this.repo.findByAgencia(agencia);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Boolean existsByUsuarioGerente(
            String usuario
    ) {
        return this.repo.existsByUsuarioAndTipo(usuario, "Gerente");
    }

    public UsuarioEntity findGerenteByGerencia(
            String agencia
    ) {
        return this.repo.findByGerenciaAndTipo(agencia, "Gerente").orElseThrow();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Boolean existsByUsuarioGerenteActivo(
            String usuario
    ) {
        return this.repo.existsByUsuarioAndTipoAndStatus(usuario, "Gerente", true);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Boolean existsByUsuario(
            String usuario
    ) {
        return this.repo.existsByUsuario(usuario);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Boolean existsByUsuarioActivo(
            String usuario
    ) {
        return this.repo.existsByUsuarioAndStatus(usuario, true);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<Optional<UsuarioEntity>> findByUsuarioAsync(
            String usuario
    ) {
        Optional<UsuarioEntity> entity = this.repo.findByUsuario(usuario);

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioEntity> darrusuarModFindByTipo(
            String tipo
    ) {
        return this.repo.darrusuarEntFindByTipo(tipo);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioId(
            int usuarioId
    ) {
        return this.repo.usuarEntFindByUsuarioId(usuarioId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<UsuarioEntity> findByUsuarioIdAsync(
            int usuarioId
    ) {
        UsuarioEntity entity = this.repo.usuarEntFindByUsuarioId(usuarioId);

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioEntity usuarModFindByUsuarioIdFromGerenciaIdOfGerenciaModel(
            String gerenciaId
    ) {
        return this.repo.usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(gerenciaId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioEntity> darrUsuarModFindByGerencia(
            String gerencia
    ) {
        return this.repo.darrusuarEntFindByGerencia(gerencia);
    }
}

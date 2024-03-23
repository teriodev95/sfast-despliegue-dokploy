package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

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
    public Iterable<UsuarioModel> findAll() {
        return this.repo.findAll();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Optional<UsuarioModel> optusuarEntFindById(
            int intId_I
    ) {
        return this.repo.findById(intId_I);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioModel usuarModFindByUsuarioAndPin(
            String usuario,
            String pin
    ) {
        return this.repo.usuarEntFindByUsuarioAndPin(usuario, pin);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioModel usuarModFindByUsuario(
            String usuario
    ) {
        return this.repo.optusuarEntFindByUsuario(usuario).orElseThrow();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioModel findByAgencia(
            String agencia
    ) {
        return this.repo.findByAgencia(agencia).orElseThrow();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public Boolean existsByUsuarioGerente(
            String usuario
    ) {
        return this.repo.existsByUsuarioAndTipo(usuario, "Gerente");
    }

    public UsuarioModel findGerenteByGerencia(
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
    public CompletableFuture<Optional<UsuarioModel>> findByUsuarioAsync(
            String usuario
    ) {
        Optional<UsuarioModel> entity = this.repo.findByUsuario(usuario);

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioModel> darrusuarModFindByTipo(
            String tipo
    ) {
        return this.repo.darrusuarEntFindByTipo(tipo);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioModel usuarModFindByUsuarioId(
            int usuarioId
    ) {
        return this.repo.usuarEntFindByUsuarioId(usuarioId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByUsuarioIdAsync(
            int usuarioId
    ) {
        UsuarioModel entity = this.repo.usuarEntFindByUsuarioId(usuarioId);

        return CompletableFuture.completedFuture(entity);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public UsuarioModel usuarModFindByUsuarioIdFromGerenciaIdOfGerenciaModel(
            String gerenciaId
    ) {
        return this.repo.usuarEntFindByUsuarioIdFromGerenciaIdOfGerenciaModel(gerenciaId);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public ArrayList<UsuarioModel> darrUsuarModFindByGerencia(
            String gerencia
    ) {
        return this.repo.darrusuarEntFindByGerencia(gerencia);
    }

    public Boolean existsByAgencia(String agencia) {
        return this.repo.existsByAgencia(agencia);
    }
}

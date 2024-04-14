package tech.calaverita.reporterloanssql.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.calaverita.reporterloanssql.models.mariaDB.UsuarioModel;
import tech.calaverita.reporterloanssql.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public boolean existsByUsuario(String usuario) {
        return this.repo.existsByUsuario(usuario);
    }

    public Boolean existsByAgencia(String agencia) {
        return this.repo.existsByAgencia(agencia);
    }

    public boolean existsByUsuarioAndTipo(String usuario, String tipo) {
        return this.repo.existsByUsuarioAndTipo(usuario, tipo);
    }

    public boolean existsByUsuarioAndStatus(String usuario, boolean status) {
        return this.repo.existsByUsuarioAndStatus(usuario, status);
    }

    public boolean existsByUsuarioTipoAndStatus(String usuario, String tipo, boolean status) {
        return this.repo.existsByUsuarioAndTipoAndStatus(usuario, tipo, status);
    }

    public UsuarioModel findById(int id) {
        return this.repo.findById(id).orElseThrow();
    }

    public UsuarioModel findByUsuario(String usuario) {
        return this.repo.findByUsuario(usuario);
    }

    public UsuarioModel findByAgenciaAndStatus(String agencia, boolean status) {
        return this.repo.findByAgenciaAndStatus(agencia, status);
    }

    public UsuarioModel findByUsuarioAndPin(String usuario, int pin) {
        return this.repo.findByUsuarioAndPin(usuario, pin);
    }

    public UsuarioModel findByGerenciaAndTipo(String gerencia, String tipo) {
        return this.repo.findByGerenciaAndTipo(gerencia, tipo).get(0);
    }

    public ArrayList<UsuarioModel> findByGerencia(String gerencia) {
        return this.repo.findByGerenciaInnerJoinUsuarioGerenciaModel(gerencia);
    }

    public ArrayList<UsuarioModel> findAll() {
        return (ArrayList<UsuarioModel>) this.repo.findAll();
    }

    public ArrayList<String> findByGerenciasTipoAndStatus(ArrayList<String> gerencias) {
        String tipo = "Gerente";
        boolean status = true;
        return this.repo.findByGerenciaAndTipoAndStatus(gerencias, tipo, status);
    }

    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByUsuarioAsync(String usuario) {
        return CompletableFuture.completedFuture(this.repo.findByUsuario(usuario));
    }

    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByIdAsync(int usuarioId) {
        return CompletableFuture.completedFuture(this.repo.findById(usuarioId).orElseThrow());
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<String>> findByGerenciaTipoAndStatusAsync(String gerencia, String tipo,
                                                                                 boolean status) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaAndTipoAndStatus(gerencia, tipo,
                status));
    }
}

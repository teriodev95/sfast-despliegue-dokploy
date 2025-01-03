package tech.calaverita.sfast_xpress.services;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.persistence.Tuple;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.repositories.UsuarioRepository;

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

    public ArrayList<UsuarioModel> findByGerenciaAndStatus(String gerencia, boolean status) {
        return this.repo.findByGerenciaAndStatus(gerencia, status);
    }

    public UsuarioModel findByUsuarioAndPin(String usuario, int pin) {
        return this.repo.findByUsuarioAndPinAndStatus(usuario, pin, true);
    }

    public UsuarioModel findByGerenciaAndTipo(String gerencia, String tipo) {
        return this.repo.findByGerenciaAndTipo(gerencia, tipo).get(0);
    }

    public ArrayList<UsuarioModel> findByGerencia(String gerencia) {
        return this.repo.findByGerenciaInnerJoinUsuarioGerenciaModel(gerencia);
    }

    public UsuarioModel findByPin(Integer pin) {
        return this.repo.findByPin(pin);
    }

    public ArrayList<UsuarioModel> findAll() {
        return (ArrayList<UsuarioModel>) this.repo.findAll();
    }

    public ArrayList<Tuple> findByGerenciasTipoAndStatus(ArrayList<String> gerencias) {
        String tipo = "Gerente";
        boolean status = true;
        return this.repo.findByGerenciasAndTipoAndStatus(gerencias, tipo, status);
    }

    public UsuarioModel findByGerenciaInnerJoinUsuarioGerenciaModel(String gerencia, String tipo, boolean status) {
        return this.repo.findByGerenciaInnerJoinUsuarioGerenciaModel(gerencia, tipo, status).get(0);
    }

    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByGerenciaTipoAndStatusAsync(String gerencia, String tipo,
            boolean status) {
        return CompletableFuture.completedFuture(this.repo.findByGerenciaAndTipoAndStatus(gerencia, tipo, status));
    }

    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByAgenciaTipoAndStatusAsync(String agencia, String tipo,
            boolean status) {
        return CompletableFuture.completedFuture(this.repo.findByAgenciaAndTipoAndStatus(agencia, tipo, status));
    }

    @Async("asyncExecutor")
    public CompletableFuture<UsuarioModel> findByIdAsync(int usuarioId) {
        return CompletableFuture.completedFuture(this.repo.findById(usuarioId).orElseThrow());
    }

    @Async("asyncExecutor")
    public CompletableFuture<ArrayList<Tuple>> findNombreCompletoByGerenciaTipoAndStatusAsync(String gerencia,
            String tipo,
            boolean status) {
        return CompletableFuture.completedFuture(this.repo.findNombreCompletoByGerenciaAndTipoAndStatus(gerencia, tipo,
                status));
    }
}

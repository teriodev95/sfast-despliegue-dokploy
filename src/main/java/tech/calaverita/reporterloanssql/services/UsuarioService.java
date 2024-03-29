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

    public UsuarioService(
            UsuarioRepository repo
    ) {
        this.repo = repo;
    }

    public boolean existsByUsuario(String usuario) {
        return this.repo.existsByUsuario(usuario);
    }

    public Boolean existsByAgencia(String agencia) {
        return this.repo.existsByAgencia(agencia);
    }

    public boolean existsByUsuarioGerente(String usuario) {
        String tipo = "Gerente";
        return this.repo.existsByUsuarioAndTipo(usuario, tipo);
    }

    public boolean existsByUsuarioActivo(String usuario) {
        boolean status = true;
        return this.repo.existsByUsuarioAndStatus(usuario, status);
    }

    public boolean existsByUsuarioGerenteActivo(String usuario) {
        String tipo = "Gerente";
        boolean status = true;
        return this.repo.existsByUsuarioAndTipoAndStatus(usuario, tipo, status);
    }

    public UsuarioModel findById(int id) {
        return this.repo.findById(id).orElseThrow();
    }

    public UsuarioModel findByUsuario(String usuario) {
        return this.repo.findByUsuario(usuario);
    }

    public UsuarioModel findByAgencia(String agencia) {
        boolean status = true;
        return this.repo.findByAgenciaAndStatus(agencia, status);
    }

    public UsuarioModel findByUsuarioAndPin(String usuario, int pin) {
        return this.repo.findByUsuarioAndPin(usuario, pin);
    }

    public UsuarioModel findGerenteByGerencia(String gerencia) {
        String tipo = "Gerente";
        return this.repo.findByGerenciaAndTipo(gerencia, tipo).get(0);
    }

    public ArrayList<UsuarioModel> findByGerencia(String gerencia) {
        return this.repo.darrusuarEntFindByGerencia(gerencia);
    }

    public ArrayList<UsuarioModel> findAll() {
        return (ArrayList<UsuarioModel>) this.repo.findAll();
    }

    public ArrayList<String> findGerentesByGerencias(ArrayList<String> gerencias) {
        String tipo = "Gerente";
        boolean status = true;
        return this.repo.findGerentesByGerenciaAndTipoAndStatus(gerencias, tipo, status);
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
    public CompletableFuture<ArrayList<String>> findAgentesByGerenciaAsync(String gerencia) {
        String tipo = "Agente";
        boolean status = true;
        return CompletableFuture.completedFuture(this.repo.findAgentesByGerenciaAndTipoAndStatus(gerencia, tipo,
                status));
    }
}

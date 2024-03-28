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
        return this.repo.existsByUsuarioAndTipo(usuario, "Gerente");
    }

    public boolean existsByUsuarioActivo(String usuario) {
        return this.repo.existsByUsuarioAndStatus(usuario, true);
    }

    public boolean existsByUsuarioGerenteActivo(String usuario) {
        return this.repo.existsByUsuarioAndTipoAndStatus(usuario, "Gerente", true);
    }

    public UsuarioModel findById(int id) {
        return this.repo.findById(id).orElseThrow();
    }

    public UsuarioModel findByUsuario(String usuario) {
        return this.repo.findByUsuario(usuario);
    }

    public UsuarioModel findByAgencia(String agencia) {
        return this.repo.findByAgencia(agencia);
    }

    public UsuarioModel findByUsuarioAndPin(String usuario, int pin) {
        return this.repo.findByUsuarioAndPin(usuario, pin);
    }

    public UsuarioModel findGerenteByGerencia(String gerencia) {
        return this.repo.findByGerenciaAndTipo(gerencia, "Gerente").get(0);
    }

    public ArrayList<UsuarioModel> findByGerencia(String gerencia) {
        return this.repo.darrusuarEntFindByGerencia(gerencia);
    }

    public ArrayList<UsuarioModel> findAll() {
        return (ArrayList<UsuarioModel>) this.repo.findAll();
    }

    public ArrayList<String> findGerentesByGerencias(ArrayList<String> gerencias) {
        return this.repo.findGerentesByGerenciaAndTipo(gerencias, "Gerente");
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
    public CompletableFuture<ArrayList<String>> findAgentesByGerencia(String gerencia) {
        return CompletableFuture.completedFuture(this.repo.findAgentesByGerenciaAndTipo(gerencia, "Agente"));
    }
}

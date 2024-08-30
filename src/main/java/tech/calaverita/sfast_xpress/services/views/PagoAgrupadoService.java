package tech.calaverita.sfast_xpress.services.views;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import tech.calaverita.sfast_xpress.models.mariaDB.views.PagoAgrupadoModel;
import tech.calaverita.sfast_xpress.repositories.views.PagoAgrupadoRepository;

@Service
public class PagoAgrupadoService {
        private final PagoAgrupadoRepository repo;

        public PagoAgrupadoService(PagoAgrupadoRepository repo) {
                this.repo = repo;
        }

        @Async("asyncExecutor")
        public CompletableFuture<ArrayList<PagoAgrupadoModel>> findByAgenciaAnioSemanaAndEsPrimerPago(String agencia,
                        int anio, int semana,
                        boolean esPrimerPago) {
                return CompletableFuture.completedFuture(this.repo.findByAgenteAndAnioAndSemanaAndEsPrimerPago(agencia,
                                anio, semana, esPrimerPago));
        }

        public ArrayList<PagoAgrupadoModel> findByPrestamoIdOrderByAnioAscSemanaAsc(String prestamoId) {
                return this.repo.findByPrestamoIdOrderByAnioAscSemanaAsc(prestamoId);
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findDebitoTotalByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findDebitoTotalByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findExcedenteByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findExcedenteByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findCobranzaTotalByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findCobranzaTotalByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Integer> findClientesCobradosByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findClientesCobradosByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Integer> findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findClientesPagoCompletoByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Integer> findClientesTotalesByAgenciaAnioAndSemanaAsync(String agencia, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findClientesTotalesByAgenteAndAnioAndSemana(agencia, anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findDebitoTotalSemanaByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
                        String sucursal,
                        int anio, int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findDebitoTotalSemanaByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(
                                                gerencia, sucursal,
                                                anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findExcedenteByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
                        String sucursal,
                        int anio, int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findExcedenteByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(gerencia,
                                                sucursal, anio,
                                                semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findCobranzaTotalByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
                        String sucursal, int anio,
                        int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(gerencia,
                                                sucursal, anio,
                                                semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findDebitoTotalParcialByGerenciaSucursalAnioAndSemanaAsync(String gerencia,
                        String sucursal,
                        int anio, int semana) {
                return CompletableFuture.completedFuture(this.repo
                                .findDebitoTotalParcialByGerenciaAndSucursalAndAnioAndSemanaInnerJoinPrestamoModel(
                                                gerencia, sucursal,
                                                anio, semana));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findExcedenteByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
                        String sucursal,
                        int anio, int semana,
                        String fecha) {
                return CompletableFuture.completedFuture(
                                this.repo.findExcedenteByGerenciaAndSucursalAndAnioAndFechaInnerJoinPrestamoModel(
                                                gerencia, sucursal, anio,
                                                semana, fecha));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Double> findCobranzaTotalByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
                        String sucursal,
                        int anio, int semana,
                        String fecha) {
                return CompletableFuture.completedFuture(this.repo
                                .findCobranzaTotalByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(
                                                gerencia, sucursal,
                                                anio, semana, fecha));
        }

        @Async("asyncExecutor")
        public CompletableFuture<Integer> findClientesCobradosByGerenciaSucursalAnioSemanaAndFechaAsync(String gerencia,
                        String sucursal,
                        int anio,
                        int semana,
                        String fecha) {
                return CompletableFuture.completedFuture(this.repo
                                .findClientesCobradosByGerenciaAndSucursalAndAnioAndSemanaAndFechaInnerJoinPrestamoModel(
                                                gerencia,
                                                sucursal, anio, semana, fecha));
        }
}

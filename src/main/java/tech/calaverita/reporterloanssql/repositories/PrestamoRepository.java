package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

import java.util.ArrayList;

@Repository
public interface PrestamoRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT prestamo FROM PrestamoModel prestamo WHERE prestamo.prestamoId = :prestamoId AND prestamo.agente = :agencia")
    PrestamoModel getPrestamoModelsByPrestamoIdAndAgencia(String prestamoId, String agencia);

    @Query("SELECT prestamo FROM PrestamoModel prestamo WHERE prestamo.prestamoId = :prestamoId AND prestamo.gerencia = :gerencia")
    PrestamoModel getPrestamoModelsByPrestamoIdAndGerencia(String prestamoId, String gerencia);
}

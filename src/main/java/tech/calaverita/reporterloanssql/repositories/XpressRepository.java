package tech.calaverita.reporterloanssql.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.calaverita.reporterloanssql.models.PrestamoModel;

import java.util.Optional;

@Repository
public interface XpressRepository extends CrudRepository<PrestamoModel, String> {
    @Query("SELECT pr.agente AS agencia, " +
            "COUNT(pr) AS clientes, " +
            "SUM(CASE WHEN pr.diaDePago = 'MIERCOLES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoMiercoles, " +
            "SUM(CASE WHEN pr.diaDePago = 'JUEVES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoJueves, " +
            "SUM(CASE WHEN pr.diaDePago = 'VIERNES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoViernes, " +
            "SUM(CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END) AS debitoTotal," +
            "pr.gerencia AS Gerencia " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0 " +
            "AND pa.cierraCon <> pr.descuento")
    String getCobranzaByAgencia(String agencia, int anio, int semana);

    @Query("SELECT pr.agente AS agencia, " +
            "COUNT(pr) AS clientes, " +
            "SUM(CASE WHEN pr.diaDePago = 'MIERCOLES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoMiercoles, " +
            "SUM(CASE WHEN pr.diaDePago = 'JUEVES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoJueves, " +
            "SUM(CASE WHEN pr.diaDePago = 'VIERNES' THEN CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END ELSE 0 END) AS debitoViernes, " +
            "SUM(CASE WHEN pa.cierraCon < pr.tarifa THEN pa.cierraCon ELSE pr.tarifa END) AS debitoTotal " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pr.gerencia = :gerencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0 " +
            "AND pa.cierraCon <> pr.descuento " +
            "GROUP BY pa.agente")
    String[] getCobranzaByGerencia(String gerencia, int anio, int semana);

    @Query("SELECT pr.agente AS agencia, " +
//            "COUNT(pr.agente) AS clientes, " +
            "SUM(CASE WHEN pa.monto = 0 THEN 1 ELSE 0 END) AS noPagos, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN 1 ELSE 0 END) AS numeroLiquidaciones, " +
            "SUM(CASE WHEN pa.monto < pr.tarifa AND pa.cierraCon > 0 AND pa.monto > 0 THEN 1 ELSE 0 END) AS pagosReducidos, " +
//            "SUM(CASE WHEN pr.diaDePago = 'MIERCOLES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoMiercoles, " +
//            "SUM(CASE WHEN pr.diaDePago = 'JUEVES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoJueves, " +
//            "SUM(CASE WHEN pr.diaDePago = 'VIERNES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoViernes, " +
//            "SUM(CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END) AS debitoTotal, " +
//            "ROUND(((SUM(pa.monto) - SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END)) / SUM(CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END) * 100), 2) AS rendimiento, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN pr.descuento ELSE 0 END) AS totalDeDescuento, " +
            "SUM(pa.monto) - SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END) AS totalCobranzaPura, " +
            "SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END) AS montoExcedente, " +
            "SUM(pr.multas) AS multas, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN pa.monto - pr.tarifa ELSE 0 END) AS liquidaciones, " +
            "SUM(pa.monto) AS cobranzaTotal, " +
            "SUM(CASE WHEN pa.monto < pr.tarifa AND pa.abreCon > pr.tarifa THEN pr.tarifa - pa.monto " +
            "WHEN pa.abreCon < pr.tarifa AND pa.monto < pa.abreCon THEN pa.abreCon - pa.monto ELSE 0 END) AS montoDeDebitoFaltante," +
            "pr.gerencia AS Gerencia " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    String getDashboardByAgencia(String agencia, int anio, int semana);

    @Query("SELECT pr.agente AS agencia, " +
            "COUNT(pr.agente) AS clientes, " +
            "SUM(CASE WHEN pa.monto = 0 THEN 1 ELSE 0 END) AS noPagos, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN 1 ELSE 0 END) AS numeroLiquidaciones, " +
            "SUM(CASE WHEN pa.monto < pr.tarifa AND pa.cierraCon > 0 AND pa.monto > 0 THEN 1 ELSE 0 END) AS pagosReducidos, " +
            "SUM(CASE WHEN pr.diaDePago = 'MIERCOLES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoMiercoles, " +
            "SUM(CASE WHEN pr.diaDePago = 'JUEVES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoJueves, " +
            "SUM(CASE WHEN pr.diaDePago = 'VIERNES' THEN CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END ELSE 0 END) AS debitoViernes, " +
            "SUM(CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END) AS debitoTotal, " +
            "ROUND(((SUM(pa.monto) - SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END)) / SUM(CASE WHEN pa.abreCon < pr.tarifa THEN pa.abreCon ELSE pr.tarifa END) * 100), 2) AS rendimiento, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN pr.descuento ELSE 0 END) AS totalDeDescuento, " +
            "SUM(pa.monto) - SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END) AS totalCobranzaPura, " +
            "SUM(CASE WHEN pa.monto > pr.tarifa THEN pa.monto - pr.tarifa ELSE 0 END) AS montoExcedente, " +
            "SUM(pr.multas) AS multas, " +
            "SUM(CASE WHEN pr.descuento > 0 THEN pa.monto - pr.tarifa ELSE 0 END) AS liquidaciones, " +
            "SUM(pa.monto) AS cobranzaTotal, " +
            "SUM(CASE WHEN pa.monto < pr.tarifa AND pa.abreCon > pr.tarifa THEN pr.tarifa - pa.monto " +
            "WHEN pa.abreCon < pr.tarifa AND pa.monto < pa.abreCon THEN pa.abreCon - pa.monto ELSE 0 END) AS montoDeDebitoFaltante, " +
            "COUNT(pr) AS clientesCobrados " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoVistaModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pr.gerencia = :gerencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false " +
            "GROUP BY pa.agente")
    String[] getDashboardByGerencia(String gerencia, int anio, int semana);

    @Query("SELECT COUNT(distinct pr.prestamoId) AS clientesPorCobrar " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana - 1 " +
            "AND pa.cierraCon > 0 " +
            "AND pa.cierraCon <> pr.descuento")
    Integer getClientesPorCobrarByAgenciaAnioAndSemana(String agencia, int anio, int semana);

    @Query("SELECT COUNT(distinct pr.prestamoId) AS clientesCobrados " +
            "FROM PrestamoModel pr " +
            "INNER JOIN PagoModel pa " +
            "ON pr.prestamoId = pa.prestamoId " +
            "WHERE pa.agente = :agencia " +
            "AND pa.anio = :anio " +
            "AND pa.semana = :semana " +
            "AND pa.esPrimerPago = false")
    Integer getClientesCobradosByAgenciaAnioAndSemana(String agencia, int anio, int semana);
}
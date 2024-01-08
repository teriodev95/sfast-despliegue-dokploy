package tech.calaverita.reporterloanssql.itext;

import com.itextpdf.text.DocumentException;
import org.checkerframework.checker.units.qual.C;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.*;
import tech.calaverita.reporterloanssql.utils.CierreSemanalUtil;

import java.io.FileNotFoundException;

public class CierreSemanal {
    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();

        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        balanceAgenciaDTO.setZona("Ger003");
        balanceAgenciaDTO.setAgencia("AGD006");
        balanceAgenciaDTO.setGerente("");
        balanceAgenciaDTO.setAgente("");
        balanceAgenciaDTO.setRendimiento(0.0);
        balanceAgenciaDTO.setNivel("");
        balanceAgenciaDTO.setClientes(0);
        balanceAgenciaDTO.setPagosReducidos(0);
        balanceAgenciaDTO.setNoPagos(0);
        balanceAgenciaDTO.setLiquidaciones(0);

        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        egresosAgenteDTO.setAsignaciones(0.0);
        egresosAgenteDTO.setOtros(0.0);
        egresosAgenteDTO.setEfectivoEntregadoCierre(0.0);
        egresosAgenteDTO.setTotal(0.0);

        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        egresosGerenteDTO.setPorcentajeComisionCobranza(0);
        egresosGerenteDTO.setPorcentajeBonoMensual(0);
        egresosGerenteDTO.setPagoComisionCobranza(0.0);
        egresosGerenteDTO.setPagoComisionVentas(0.0);
        egresosGerenteDTO.setBonos(0.0);
        egresosGerenteDTO.setEfectivoRestanteCierre(0.0);

        IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
        ingresosAgenteDTO.setCobranzaPura(0.0);
        ingresosAgenteDTO.setMontoExcedente(0.0);
        ingresosAgenteDTO.setLiquidaciones(0.0);
        ingresosAgenteDTO.setMultas(0.0);
        ingresosAgenteDTO.setOtros(0.0);
        ingresosAgenteDTO.setTotal(0.0);

        cierreSemanalDTO.setAnio(2023);
        cierreSemanalDTO.setSemana(46);
        cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
        cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
        cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
        cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);

        CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);
    }
}

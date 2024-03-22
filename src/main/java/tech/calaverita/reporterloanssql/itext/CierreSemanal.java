package tech.calaverita.reporterloanssql.itext;

import com.itextpdf.text.DocumentException;
import tech.calaverita.reporterloanssql.dto.cierre_semanal.*;
import tech.calaverita.reporterloanssql.utils.CierreSemanalUtil;

import java.io.FileNotFoundException;

public class CierreSemanal {
    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();

        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        balanceAgenciaDTO.setZona("Ger003");
        balanceAgenciaDTO.setAgencia("AGD006");
        balanceAgenciaDTO.setGerente("KARINA GARCIA GARCIA");
        balanceAgenciaDTO.setAgente("ELVIRA GARCIA GARCIA");
        balanceAgenciaDTO.setRendimiento(90.8);
        balanceAgenciaDTO.setNivel("DIAMANTE");
        balanceAgenciaDTO.setClientes(100);
        balanceAgenciaDTO.setPagosReducidos(2);
        balanceAgenciaDTO.setNoPagos(5);
        balanceAgenciaDTO.setLiquidaciones(1);

        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        egresosAgenteDTO.setAsignaciones(25000.00);
        egresosAgenteDTO.setOtros(345.00);
        egresosAgenteDTO.setEfectivoEntregadoCierre(65000.00);
        egresosAgenteDTO.setTotal(23909.00);

        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        egresosGerenteDTO.setPorcentajeComisionCobranza(5500);
        egresosGerenteDTO.setPorcentajeBonoMensual(5);
        egresosGerenteDTO.setPagoComisionCobranza(2400.00);
        egresosGerenteDTO.setPagoComisionVentas(2300.00);
        egresosGerenteDTO.setBonos(1200.00);
        egresosGerenteDTO.setEfectivoRestanteCierre(5500.00);

        IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
        ingresosAgenteDTO.setCobranzaPura(65902.00);
        ingresosAgenteDTO.setMontoExcedente(4500.00);
        ingresosAgenteDTO.setLiquidaciones(2300.00);
        ingresosAgenteDTO.setMultas(450.00);
        ingresosAgenteDTO.setOtros(250.00);
        ingresosAgenteDTO.setTotal(250.00);

        cierreSemanalDTO.setAnio(2023);
        cierreSemanalDTO.setSemana(47);
        cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
        cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
        cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
        cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);

        CierreSemanalUtil.createCierreSemanalPDF(cierreSemanalDTO);
    }
}

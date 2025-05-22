package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.CierreAgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class ComisionesAPagarEnSemana {
    private Integer porcentajeComisionCobranza;
    private Integer porcentajeBonoMensual;
    private Double pagoComisionCobranza;
    private Double pagoComisionVentas;
    private Double bonos;

    public ComisionesAPagarEnSemana() {
        this.porcentajeComisionCobranza = 0;
        this.porcentajeBonoMensual = 0;
        this.pagoComisionCobranza = 0.0;
        this.pagoComisionVentas = 0.0;
        this.bonos = 0.0;
    }

    public ComisionesAPagarEnSemana(CierreAgenciaModel cierreAgenciaModel) {
        this();

        if (cierreAgenciaModel != null) {
            this.porcentajeComisionCobranza = cierreAgenciaModel.getPorcentajePorCobranzaPagadoEnSemana();
            this.porcentajeBonoMensual = cierreAgenciaModel.getPorcentajePorBonoMensualPagadoEnSemana();
            this.pagoComisionCobranza = cierreAgenciaModel.getComisionCobranzaPagadaEnSemana();
            this.pagoComisionVentas = cierreAgenciaModel.getComisionVentasPagadaEnSemana();
            this.bonos = cierreAgenciaModel.getBonosPagadosEnSemana();
        }
    }

    public ComisionesAPagarEnSemana(ComisionModel comisionModel) {
        this();

        if (comisionModel != null) {
            this.porcentajeComisionCobranza = comisionModel.getPorcentajeComisionCobranza();
            this.pagoComisionCobranza = comisionModel.getComisionCobranza();
            this.pagoComisionVentas = comisionModel.getComisionVentas();
        }
    }

    public ComisionesAPagarEnSemana bonos(List<CierreAgenciaModel> cierreAgenciaModels,
            List<ComisionModel> comisionModels, String fechaIngresoAgente) {
        int clientesPagoCompleto = 0;
        double rendimiento = 0.0;
        double cobranzaTotal = 0.0;
        for (int i = 0; i < cierreAgenciaModels.size(); i++) {
            CierreAgenciaModel cierreAgenciaModel = cierreAgenciaModels.get(i);
            ComisionModel comisionModel = comisionModels.get(i);

            if (cierreAgenciaModel != null && comisionModel != null) {
                clientesPagoCompleto += cierreAgenciaModel.getClientes()
                        - cierreAgenciaModel.getPagosReducidos()
                        - cierreAgenciaModel.getNoPagos();
                rendimiento += comisionModel.getRendimiento();
                cobranzaTotal += comisionModel.getCobranzaTotal()
                        - cierreAgenciaModel.getLiquidaciones();
            }
        }

        rendimiento = rendimiento / cierreAgenciaModels.size();

        this.porcentajeBonoMensual = getPorcentajeBonoMensual(clientesPagoCompleto, rendimiento, fechaIngresoAgente);
        this.bonos = MyUtil.getDouble(cobranzaTotal / 100 * this.porcentajeBonoMensual);

        return this;
    }

    private static int getPorcentajeBonoMensual(int clientesPagoCompleto, double rendimiento,
            String fechaIngresoAgente) {
        int porcentajeBonoMensual = 0;
        int antiguedadAgente = getAntiguedadAgenteEnMeses(fechaIngresoAgente);

        if (clientesPagoCompleto >= 90 && rendimiento >= 95 && antiguedadAgente >= 12) {
            porcentajeBonoMensual = 3;
        } else if (clientesPagoCompleto >= 60 && rendimiento >= 90 && antiguedadAgente >= 6) {
            porcentajeBonoMensual = 2;
        } else if (clientesPagoCompleto >= 30 && rendimiento >= 80 && antiguedadAgente >= 3) {
            porcentajeBonoMensual = 1;
        }

        return porcentajeBonoMensual;
    }

    private static int getAntiguedadAgenteEnMeses(String fechaIngreso) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaIngresoAgente;

        try {
            fechaIngresoAgente = format.parse(fechaIngreso);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 30 dia/mes x 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
        return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000L * 60 * 60 * 24 * 30));
    }
}

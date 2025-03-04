package tech.calaverita.sfast_xpress.pojos;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.models.mariaDB.views.PrestamoViewModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class CobranzaAgencia {
    private Double debito;
    private Double cobranzaPura;
    private Double faltante;
    private Double eficiencia;
    private Double ventas;
    private Double descuentoPorLiquidacion;
    private Integer clientes;
    private Integer noPagos;
    private Integer pagosReducidos;
    private Integer clientesLiquidados;
    private Integer semana;
    private Integer anio;
    private String agencia;
    private String agente;
    private String gerencia;

    public CobranzaAgencia() {
        this.debito = 0.0;
        this.cobranzaPura = 0.0;
        this.faltante = 0.0;
        this.eficiencia = 0.0;
        this.ventas = 0.0;
        this.descuentoPorLiquidacion = 0.0;
        this.clientes = 0;
        this.noPagos = 0;
        this.pagosReducidos = 0;
        this.clientesLiquidados = 0;
        this.agencia = "";
        this.agente = "";
        this.semana = 0;
        this.anio = 0;
        this.gerencia = "";
    }

    public CobranzaAgencia(List<CobranzaAgencia> cobranzaAgencias) {
        this();

        int agenciasActivas = 0;
        for (CobranzaAgencia cobranzaAgencia : cobranzaAgencias) {
            this.debito += cobranzaAgencia.getDebito();
            this.cobranzaPura += cobranzaAgencia.getCobranzaPura();
            this.faltante += cobranzaAgencia.getFaltante();
            this.eficiencia += cobranzaAgencia.getEficiencia();
            this.ventas += cobranzaAgencia.getVentas();
            this.descuentoPorLiquidacion += cobranzaAgencia.getDescuentoPorLiquidacion();
            this.clientes += cobranzaAgencia.getClientes();
            this.noPagos += cobranzaAgencia.getNoPagos();
            this.pagosReducidos += cobranzaAgencia.getPagosReducidos();
            this.clientesLiquidados += cobranzaAgencia.getClientesLiquidados();

            if (cobranzaAgencia.getDebito() > 0) {
                agenciasActivas++;
            }
        }
        this.eficiencia = this.eficiencia / agenciasActivas;
        this.anio = cobranzaAgencias.get(0).getAnio();
        this.semana = cobranzaAgencias.get(0).getSemana();
        this.gerencia = cobranzaAgencias.get(0).getGerencia();

        formatDoubles();
    }

    @SuppressWarnings("unchecked")
    public CobranzaAgencia(AlmacenObjects almacenObjects) {
        this();

        this.gerencia = (String) almacenObjects.getObject("gerencia");
        this.agencia = (String) almacenObjects.getObject("agencia");
        this.agente = (String) almacenObjects.getObject("agente");
        this.semana = (Integer) almacenObjects.getObject("semana");
        this.anio = (Integer) almacenObjects.getObject("anio");

        List<PagoDynamicModel> pagoDynamicModels = (List<PagoDynamicModel>) almacenObjects
                .getObject("pagoDynamicModels");
        List<PrestamoViewModel> prestamoViewModels = (List<PrestamoViewModel>) almacenObjects
                .getObject("prestamoViewModels");
        List<VentaModel> ventaModels = (List<VentaModel>) almacenObjects.getObject("ventaModels");

        this.clientes = prestamoViewModels.size();
        setDebito(prestamoViewModels);
        setResultadosCobranza(pagoDynamicModels);
        setVentas(ventaModels);
        formatDoubles();
    }

    private void setDebito(List<PrestamoViewModel> prestamoViewModels) {
        for (PrestamoViewModel prestamoViewModel : prestamoViewModels) {
            // To easy code
            double saldo = prestamoViewModel.getSaldoAlIniciarSemana();
            double tarifa = prestamoViewModel.getTarifa();
            double tarifaCalculada = saldo < tarifa ? saldo : tarifa;

            this.debito += tarifaCalculada;
        }
    }

    private void setResultadosCobranza(List<PagoDynamicModel> pagoDynamicModels) {
        for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
            // To easy code
            double abreCon = pagoDynamicModel.getAbreCon();
            double tarifa = pagoDynamicModel.getTarifa();
            double tarifaCalculada = abreCon < tarifa ? abreCon : tarifa;
            double monto = pagoDynamicModel.getMonto();
            String tipo = pagoDynamicModel.getTipo();

            if (monto >= tarifaCalculada) {
                this.cobranzaPura += tarifaCalculada;
            } else {
                this.cobranzaPura += monto;
                this.pagosReducidos++;
            }

            switch (tipo) {
                case "Liquidacion":
                    this.clientesLiquidados++;
                    this.descuentoPorLiquidacion += abreCon - monto;
                    break;
                case "No_pago":
                    this.noPagos++;
                    break;
                default:
                    break;
            }
        }

        if (this.debito == 0.0) {
            this.eficiencia = 0.0;
        } else {
            this.eficiencia = this.cobranzaPura / this.debito * 100;
        }

        this.faltante += this.debito - this.cobranzaPura;
    }

    private void setVentas(List<VentaModel> ventaModels) {
        for (VentaModel ventaModel : ventaModels) {
            this.ventas += ventaModel.getMonto();
        }
    }

    private void formatDoubles() {
        this.debito = MyUtil.getDouble(this.debito);
        this.cobranzaPura = MyUtil.getDouble(this.cobranzaPura);
        this.faltante = MyUtil.getDouble(this.faltante);
        this.eficiencia = MyUtil.getDouble(this.eficiencia);
        this.ventas = MyUtil.getDouble(this.ventas);
        this.descuentoPorLiquidacion = MyUtil.getDouble(this.descuentoPorLiquidacion);
    }
}

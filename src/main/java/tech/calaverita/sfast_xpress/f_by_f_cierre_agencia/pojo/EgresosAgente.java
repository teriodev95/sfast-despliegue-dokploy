package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.pojo;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.f_by_f_cierre_agencia.CierreAgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.AsignacionModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class EgresosAgente {
    private Double asignaciones;
    private Double otros;
    private String motivoOtros;
    private Double efectivoEntregadoCierre;
    private Double total;
    private HashMap<String, Double> asignaciones_desglose;

    public EgresosAgente() {
        this.asignaciones = 0.0;
        this.otros = 0.0;
        this.motivoOtros = "";
        this.efectivoEntregadoCierre = 0.0;
        this.total = 0.0;

        HashMap<String, Double> asignaciones_desglose = new HashMap<>();
        asignaciones_desglose.put("aSeguridad", 0.0);
        asignaciones_desglose.put("aGerente", 0.0);
        this.asignaciones_desglose = asignaciones_desglose;
    }

    public EgresosAgente(CierreAgenciaModel cierreAgenciaModel) {
        this();
        this.asignaciones = cierreAgenciaModel.getAsignaciones();
        this.otros = cierreAgenciaModel.getOtrosEgresos();
        this.motivoOtros = cierreAgenciaModel.getMotivoOtrosEgresos();
        this.efectivoEntregadoCierre = cierreAgenciaModel.getEfectivoEntregadoCierre();
        this.total = cierreAgenciaModel.getTotalEgresosAgente();
    }

    public EgresosAgente(List<AsignacionModel> asignacionModels, Double cobranzaTotal) {
        this();
        // To easy code
        double asignaciones = asignacionModels.stream().mapToDouble(AsignacionModel::getMonto).sum();
        double asignacionesASeguridad = asignacionModels.stream()
                .filter(asignacionModel -> asignacionModel.getRecibioUsuarioModel().getTipo().equals("Seguridad"))
                .mapToDouble(AsignacionModel::getMonto).sum();
        double asignacionesAGerente = asignacionModels.stream()
                .filter(asignacionModel -> asignacionModel.getRecibioUsuarioModel().getTipo().equals("Gerente"))
                .mapToDouble(AsignacionModel::getMonto).sum();

        HashMap<String, Double> asignacionesDesglose = new HashMap<>();
        asignacionesDesglose.put("aSeguridad", asignacionesASeguridad);
        asignacionesDesglose.put("aGerente", asignacionesAGerente);

        this.asignaciones = asignaciones;
        this.asignaciones_desglose = asignacionesDesglose;
        this.efectivoEntregadoCierre = MyUtil.getDouble(cobranzaTotal - asignaciones);
    }
}

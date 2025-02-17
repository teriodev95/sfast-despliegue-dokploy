package tech.calaverita.sfast_xpress.DTOs.detalle_cierre_semanal;

import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class DetalleCierreSemanalGastosDto {
    private Double gasolina;
    private Double casetas;
    private Double apoyoAutomovil;
    private Double otros;
    private Double total;

    public DetalleCierreSemanalGastosDto() {
        this.gasolina = 0D;
        this.casetas = 0D;
        this.apoyoAutomovil = 0D;
        this.otros = 0D;
        this.total = 0D;
    }

    public DetalleCierreSemanalGastosDto(List<GastoModel> gastoModels) {
        this();

        for (GastoModel gastoModel : gastoModels) {
            // To easy code
            String tipo = gastoModel.getTipoGasto();
            Double gasto = gastoModel.getMonto();

            switch (tipo) {
                case "GASOLINA":
                    this.gasolina += gasto;
                    break;
                case "CASETAS":
                    this.casetas += gasto;
                    break;
                case "MANTENIMIENTO_VEHICULAR":
                    this.apoyoAutomovil += gasto;
                    break;
                default:
                    this.otros += gasto;
                    break;
            }

            this.total += gasto;

        }

        formatDoubles();
    }

    private void formatDoubles() {
        this.gasolina = MyUtil.getDouble(this.gasolina);
        this.casetas = MyUtil.getDouble(this.casetas);
        this.apoyoAutomovil = MyUtil.getDouble(this.apoyoAutomovil);
        this.otros = MyUtil.getDouble(this.otros);
        this.total = MyUtil.getDouble(this.total);
    }
}

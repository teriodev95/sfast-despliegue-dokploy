package tech.calaverita.sfast_xpress.DTOs.flujo_efectivo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.GastoModel;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@Data
public class FlujoEfectivoGastosFlujoEfectivoDto {
    @JsonProperty(value = "subtotal")
    private Double subTotalGastos;
    @JsonProperty(value = "gastos")
    private List<HashMap<String, Object>> gastoFlujoEfectivoDtos;

    public FlujoEfectivoGastosFlujoEfectivoDto() {
        this.subTotalGastos = 0D;
        this.gastoFlujoEfectivoDtos = new ArrayList<>();
    }

    public FlujoEfectivoGastosFlujoEfectivoDto(List<GastoModel> gastoModels) {
        this();

        for (GastoModel gastoModel : gastoModels) {
            this.subTotalGastos += gastoModel.getMonto();
            HashMap<String, Object> gasto = new HashMap<>();

            if (gastoModel.getTipoGasto().equals("GASOLINA")) {
                gasto.put("fecha", MyUtil.getLocalDateFromString(gastoModel.getFecha()));
                gasto.put("tipo", gastoModel.getTipoGasto());
                gasto.put("concepto", gastoModel.getConcepto());
                gasto.put("monto", gastoModel.getMonto());
                gasto.put("litros", gastoModel.getLitros());
            } else {
                gasto.put("fecha", MyUtil.getLocalDateFromString(gastoModel.getFecha()));
                gasto.put("tipo", gastoModel.getTipoGasto());
                gasto.put("concepto", gastoModel.getConcepto());
                gasto.put("monto", gastoModel.getMonto());
            }

            this.gastoFlujoEfectivoDtos.add(gasto);
        }

        this.subTotalGastos = MyUtil.getDouble(this.subTotalGastos);
    }
}

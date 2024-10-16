package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;
import tech.calaverita.sfast_xpress.utils.MyUtil;

import java.util.ArrayList;

@Data
public class ActivosDTO {
    private String tipoVivienda;
    private String numeroPisosVivienda;
    private String colorVivienda;
    private String tieneAutomovil;
    private String marcaAutomovil;
    private String modeloAutomovil;
    private String colorAutomovil;
    private String placaAutomovil;
    private String serieAutomovil;
    private String yearAutomovil;
    private ArrayList<ActivoDTO> activosLB;
    private ArrayList<ActivoDTO> activosElectr;
    private Double otrosActivosValor;

    public ActivosDTO(String tipoVivienda, String numeroPisosVivienda, String colorVivienda, String tieneAutomovil,
            String marcaAutomovil, String modeloAutomovil, String colorAutomovil, String placaAutomovil,
            String serieAutomovil, String yearAutomovil, ArrayList<ActivoDTO> activosLB,
            ArrayList<ActivoDTO> activosElectr, String otrosActivosValor) {
        this.tipoVivienda = tipoVivienda;
        this.numeroPisosVivienda = numeroPisosVivienda;
        this.colorVivienda = colorVivienda;
        this.tieneAutomovil = tieneAutomovil;
        this.marcaAutomovil = marcaAutomovil;
        this.modeloAutomovil = modeloAutomovil;
        this.colorAutomovil = colorAutomovil;
        this.placaAutomovil = placaAutomovil;
        this.serieAutomovil = serieAutomovil;
        this.yearAutomovil = yearAutomovil;
        this.activosLB = activosLB;
        this.activosElectr = activosElectr;
        this.otrosActivosValor = MyUtil.monetaryToDouble(otrosActivosValor);
    }
}

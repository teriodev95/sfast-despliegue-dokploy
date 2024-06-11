package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

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
    private String otrosActivosVaror;
}

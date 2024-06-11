package tech.calaverita.sfast_xpress.DTOs.solicitud;

import lombok.Data;

@Data
public class DatosPersonaDTO {
    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private String fechaNacimiento;
    private Character sexo;
    private String curp;
    private String rfc;
    private String telefono;
    private Boolean whatsapp;
    private String nacionalidad;
    private String correo;
    private String estadoCivil;
    private String nombreConyuge;
    private String apPaternoConyuge;
    private String apMaternoConyuge;
    private String celularConyuge;
    private String regimenMatrimonial;
}

package tech.calaverita.sfast_xpress.models.mariaDB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "personas")
public class PersonaModel {
    @Id
    private String id;
    private String xpressId;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String genero;
    private String estadoNacimiento;
    private String curp;
    private String rfc;
    private String nacionalidad;
    private String correo;
    private String telefono;
    private Boolean tieneWhatsapp;
    private String estadoCivil;
    private String nombreConyugue;
    private String celularConyugue;
    private String regimenMatrimonial;
    private String calle;
    private String noExterior;
    private String noInterior;
    private String colonia;
    private String codigoPostal;
    private String municipio;
    private String estado;
    private String referenciaDir;
    private String tipoPropiedad;
    private String residencia_desde;
}

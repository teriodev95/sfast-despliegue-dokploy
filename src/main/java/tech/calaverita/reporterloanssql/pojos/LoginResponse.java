package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;

import java.util.ArrayList;

@Data
public class LoginResponse {
    private UsuarioEntity solicitante;
    private ArrayList<UsuarioEntity> involucrados = new ArrayList<>();
}

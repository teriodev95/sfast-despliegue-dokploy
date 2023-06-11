package tech.calaverita.reporterloanssql.pojos;

import lombok.Data;
import tech.calaverita.reporterloanssql.models.UsuarioModel;

import java.util.ArrayList;

@Data
public class LoginResponse {
    private UsuarioModel solicitante;
    private ArrayList<UsuarioModel> involucrados = new ArrayList<>();
}

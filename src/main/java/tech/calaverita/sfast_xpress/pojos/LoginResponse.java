package tech.calaverita.sfast_xpress.pojos;

import lombok.Data;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

import java.util.ArrayList;

@Data
public class LoginResponse {
    private UsuarioModel solicitante;
    private ArrayList<UsuarioModel> involucrados = new ArrayList<>();
}

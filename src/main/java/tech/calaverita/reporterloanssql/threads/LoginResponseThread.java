package tech.calaverita.reporterloanssql.threads;

import tech.calaverita.reporterloanssql.models.UsuarioModel;
import tech.calaverita.reporterloanssql.pojos.LoginResponse;
import tech.calaverita.reporterloanssql.services.UsuarioService;

import java.util.ArrayList;

public class LoginResponseThread implements Runnable {
    private LoginResponse loginResponse;
    private int opc;

    public LoginResponseThread(LoginResponse loginResponse, int opc) {
        this.loginResponse = loginResponse;
        this.opc = opc;
    }

    @Override
    public void run() {
        switch (opc) {
            case 0 -> getUsuarioModelGerencia();
            case 1 -> getUsuarioModelRegional();
            case 2 -> getUsuarioModelsSeguridad();
        }
    }

    public void getUsuarioModelGerencia() {
        UsuarioModel usuarioModel = UsuarioService.findOneByUsuario(loginResponse.getSolicitante().getGerencia());

        loginResponse.getInvolucrados().add(usuarioModel);
    }

    public void getUsuarioModelRegional() {
        UsuarioModel usuarioModel = UsuarioService.findOneByUsuarioIdFromGerenciaIdOfGerenciaModel(loginResponse.getSolicitante().getGerencia());

        loginResponse.getInvolucrados().add(usuarioModel);
    }

    public void getUsuarioModelsSeguridad() {
        ArrayList<UsuarioModel> usuarioModels = UsuarioService.findManyByTipo("Seguridad");

        for (UsuarioModel usuarioModel : usuarioModels) {
            loginResponse.getInvolucrados().add(usuarioModel);
        }
    }
}

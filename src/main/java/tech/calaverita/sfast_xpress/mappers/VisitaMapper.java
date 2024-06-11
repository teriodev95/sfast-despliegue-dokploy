package tech.calaverita.sfast_xpress.mappers;

import com.google.gson.Gson;
import tech.calaverita.sfast_xpress.DTOs.VisitaDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.VisitaModel;

import java.util.ArrayList;

public class VisitaMapper implements IMapper<VisitaModel, VisitaDTO> {
    @Override
    public VisitaDTO mapOut(VisitaModel out) {
        return null;
    }

    @Override
    public VisitaModel mapIn(VisitaDTO in) {
        VisitaModel visitaModel = new VisitaModel();
        visitaModel.setVisitaId(in.getVisitaId());
        visitaModel.setPrestamoId(in.getPrestamoId());
        visitaModel.setSemana(in.getSemana());
        visitaModel.setAnio(in.getAnio());
        visitaModel.setCliente(in.getCliente());
        visitaModel.setAgente(in.getAgente());
        visitaModel.setFecha(in.getFecha());
        visitaModel.setLat(in.getLat());
        visitaModel.setLng(in.getLng());
        if (in.getLog() != null) {
            visitaModel.setLog(new Gson().toJson(in.getLog()));
        }
        return visitaModel;
    }

    @Override
    public ArrayList<VisitaDTO> mapOuts(ArrayList<VisitaModel> outs) {
        return null;
    }

    @Override
    public ArrayList<VisitaModel> mapIns(ArrayList<VisitaDTO> ins) {
        return null;
    }
}

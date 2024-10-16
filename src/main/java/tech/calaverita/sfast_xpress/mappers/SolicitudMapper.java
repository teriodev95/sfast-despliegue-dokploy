package tech.calaverita.sfast_xpress.mappers;

import java.util.ArrayList;

import com.google.gson.Gson;

import tech.calaverita.sfast_xpress.DTOs.solicitud.SolicitudDTO;
import tech.calaverita.sfast_xpress.DTOs.solicitud.StatusSolicitudDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.SolicitudModel;

public class SolicitudMapper implements IMapper<SolicitudModel, SolicitudDTO> {
    @Override
    public SolicitudDTO mapOut(SolicitudModel out) {
        SolicitudDTO solicitudDTO = new Gson().fromJson(out.getSolicitud(), SolicitudDTO.class);
        solicitudDTO.setHistorial(new Gson().fromJson(out.getHistorial(), StatusSolicitudDTO[].class));
        return solicitudDTO;
    }

    @Override
    public SolicitudModel mapIn(SolicitudDTO in) {
        SolicitudModel solicitudModel = new SolicitudModel();
        {
            solicitudModel.setId(in.getSolicitudId());
            solicitudModel.setAgencia(in.getAgencia());
            solicitudModel.setGerencia(in.getGerencia());
            solicitudModel.setSemana(in.getSemana());
            solicitudModel.setAnio(in.getAnio());
            solicitudModel.setStatus(in.getStatus());
            solicitudModel.setFechaSolicitud(in.getFechaSolicitud());
            solicitudModel.setHistorial(new Gson().toJson(in.getHistorial()));
            solicitudModel.setSolicitud(new Gson().toJson(in));
        }
        return solicitudModel;
    }

    @Override
    public ArrayList<SolicitudDTO> mapOuts(ArrayList<SolicitudModel> outs) {
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        outs.forEach(out -> solicitudDTOs.add(this.mapOut(out)));
        return solicitudDTOs;
    }

    @Override
    public ArrayList<SolicitudModel> mapIns(ArrayList<SolicitudDTO> ins) {
        return null;
    }
}

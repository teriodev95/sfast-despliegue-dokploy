package tech.calaverita.reporterloanssql.mappers;

import com.google.gson.Gson;
import tech.calaverita.reporterloanssql.dto.solicitud.SolicitudDTO;
import tech.calaverita.reporterloanssql.models.mariaDB.SolicitudModel;

import java.util.ArrayList;

public class SolicitudMapper implements IMapper<SolicitudModel, SolicitudDTO> {
    @Override
    public SolicitudDTO mapOut(SolicitudModel out) {
        return new Gson().fromJson(out.getSolicitud(), SolicitudDTO.class);
    }

    @Override
    public SolicitudModel mapIn(SolicitudDTO in) {
        SolicitudModel solicitudModel = new SolicitudModel();
        {
            solicitudModel.setId(in.getSolicitudId());
            solicitudModel.setAgente(in.getAgente());
            solicitudModel.setGerente(in.getGerente());
            solicitudModel.setSemana(in.getSemana());
            solicitudModel.setAnio(in.getAnio());
            solicitudModel.setStatus(in.getStatus());
            solicitudModel.setFechaSolicitud(in.getFechaSolicitud());
            solicitudModel.setSolicitud(new Gson().toJson(in));
        }
        return solicitudModel;
    }

    public ArrayList<SolicitudDTO> mapOuts(ArrayList<SolicitudModel> outs) {
        ArrayList<SolicitudDTO> solicitudDTOs = new ArrayList<>();
        outs.forEach(out -> solicitudDTOs.add(this.mapOut(out)));
        return solicitudDTOs;
    }
}

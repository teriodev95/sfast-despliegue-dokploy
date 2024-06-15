package tech.calaverita.sfast_xpress.mappers;

import tech.calaverita.sfast_xpress.DTOs.ReporteCallCenterDTO;
import tech.calaverita.sfast_xpress.models.mariaDB.ReporteCallCenterLiteModel;

import java.util.ArrayList;

public class ReporteCallCenterMapper implements IMapper<ReporteCallCenterLiteModel, ReporteCallCenterDTO> {

    @Override
    public ReporteCallCenterDTO mapOut(ReporteCallCenterLiteModel out) {
        ReporteCallCenterDTO reporteCallCenterDTO = new ReporteCallCenterDTO();
        reporteCallCenterDTO.setReporteId(out.getId());
        reporteCallCenterDTO.setNombre_atiende_aval(out.getNombreAtiendeAval());
        reporteCallCenterDTO.setNombre_atiende_cliente(out.getNombreAtiendeCliente());
        reporteCallCenterDTO.setNombres_aval(out.getNombreAval());
        reporteCallCenterDTO.setNombres_cliente(out.getNombreCliente());
        reporteCallCenterDTO.setNum_llamadas_aval(out.getNumLlamadasAval());
        reporteCallCenterDTO.setNum_llamadas_cliente(out.getNumLlamadasCliente());
        reporteCallCenterDTO.setObservaciones_aval(out.getObservacionesAval());
        reporteCallCenterDTO.setObservaciones_cliente(out.getObservacionesCliente());
        reporteCallCenterDTO.setPrestamoId(out.getPrestamoId());
        reporteCallCenterDTO.setStatus_llamada_aval(out.getStatusLlamadaAval());
        reporteCallCenterDTO.setStatus_llamada_cliente(out.getStatusLlamadaCliente());
        reporteCallCenterDTO.setUrl_llamada_aval(out.getUrlLlamadaAval());
        reporteCallCenterDTO.setUrl_llamada_cliente(out.getUrlLlamadaCliente());
        reporteCallCenterDTO.setPreguntas_cliente(new PreguntaCallCenterMapper()
                .mapOuts(out.getPreguntasCliente()));
        reporteCallCenterDTO.setPreguntas_aval(new PreguntaCallCenterMapper().mapOuts(out.getPreguntas_aval()));
        reporteCallCenterDTO.setReportar_seguridad(out.getReportarSeguridad());
        reporteCallCenterDTO.setAnio(out.getAnio());
        reporteCallCenterDTO.setSemana(out.getSemana());
        reporteCallCenterDTO.setTieneVisitas(out.getTieneVisitas());
        reporteCallCenterDTO.setGerencia(out.getGerencia());
        reporteCallCenterDTO.setAgencia(out.getAgencia());
        return reporteCallCenterDTO;
    }

    @Override
    public ReporteCallCenterLiteModel mapIn(ReporteCallCenterDTO in) {
        return null;
    }

    @Override
    public ArrayList<ReporteCallCenterDTO> mapOuts(ArrayList<ReporteCallCenterLiteModel> outs) {
        ArrayList<ReporteCallCenterDTO> reportesCallCenterDTO = new ArrayList<>();
        outs.forEach(out -> reportesCallCenterDTO.add(mapOut(out)));
        return reportesCallCenterDTO;
    }

    @Override
    public ArrayList<ReporteCallCenterLiteModel> mapIns(ArrayList<ReporteCallCenterDTO> ins) {
        return null;
    }
}

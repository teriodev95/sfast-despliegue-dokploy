package tech.calaverita.reporterloanssql.utils;

import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.ObjectsContainer;
import tech.calaverita.reporterloanssql.threads.DashboardThread;

import java.util.ArrayList;

public class DashboardUtil implements Runnable {
    public ObjectsContainer objectsContainer;

    public DashboardUtil(ObjectsContainer objectsContainer) {
        this.objectsContainer = objectsContainer;
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[7];

        for (int i = 0; i < 7; i++) {
            threads[i] = new Thread(new DashboardThread(objectsContainer, i, threads));
            threads[i].setPriority(1);
        }

        for (int i = 0; i < 7; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 7; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Dashboard getDashboardGeneral(Dashboard[] dashboards){
        Dashboard dashboardResponse = getDashboardWithEmptyValues();

        for(Dashboard dashboard : dashboards){
            if(dashboardResponse.getGerencia() == null){
                dashboardResponse.setGerencia(dashboard.getGerencia());
                dashboardResponse.setAnio(dashboard.getAnio());
                dashboardResponse.setSemana(dashboard.getSemana());
            }

            dashboardResponse.setClientes(dashboardResponse.getClientes() + dashboard.getClientes());
            dashboardResponse.setClientesCobrados(dashboardResponse.getClientesCobrados() + dashboard.getClientesCobrados());
            dashboardResponse.setNoPagos(dashboardResponse.getNoPagos() + dashboard.getNoPagos());
            dashboardResponse.setNumeroLiquidaciones(dashboardResponse.getNumeroLiquidaciones() + dashboard.getNumeroLiquidaciones());
            dashboardResponse.setPagosReducidos(dashboardResponse.getPagosReducidos() + dashboard.getPagosReducidos());
            dashboardResponse.setDebitoMiercoles(dashboardResponse.getDebitoMiercoles() + dashboard.getDebitoMiercoles());
            dashboardResponse.setDebitoJueves(dashboardResponse.getDebitoJueves() + dashboard.getDebitoJueves());
            dashboardResponse.setDebitoViernes(dashboardResponse.getDebitoViernes() + dashboard.getDebitoViernes());
            dashboardResponse.setDebitoTotal(dashboardResponse.getDebitoTotal() + dashboard.getDebitoTotal());
            dashboardResponse.setTotalDeDescuento(dashboardResponse.getTotalDeDescuento() + dashboard.getTotalDeDescuento());
            dashboardResponse.setTotalCobranzaPura(dashboardResponse.getTotalCobranzaPura() + dashboard.getTotalCobranzaPura());
            dashboardResponse.setMontoExcedente(dashboardResponse.getMontoExcedente() + dashboard.getMontoExcedente());
            dashboardResponse.setMultas(dashboardResponse.getMultas() + dashboard.getMultas());
            dashboardResponse.setLiquidaciones(dashboardResponse.getLiquidaciones() + dashboard.getLiquidaciones());
            dashboardResponse.setCobranzaTotal(dashboardResponse.getCobranzaTotal() + dashboard.getCobranzaTotal());
            dashboardResponse.setMontoDeDebitoFaltante(dashboardResponse.getMontoDeDebitoFaltante() + dashboard.getMontoDeDebitoFaltante());
        }

        dashboardResponse.setRendimiento(Math.round(dashboardResponse.getTotalCobranzaPura() / dashboardResponse.getDebitoTotal() * 100.0) / 100.0 * 100.0);

        return dashboardResponse;
    }

    public static Dashboard getDashboardWithEmptyValues(){
        Dashboard dashboardResponse = new Dashboard();

        dashboardResponse.setClientes(0);
        dashboardResponse.setClientesCobrados(0);
        dashboardResponse.setNoPagos(0);
        dashboardResponse.setNumeroLiquidaciones(0);
        dashboardResponse.setPagosReducidos(0);
        dashboardResponse.setDebitoMiercoles(0.0);
        dashboardResponse.setDebitoJueves(0.0);
        dashboardResponse.setDebitoViernes(0.0);
        dashboardResponse.setDebitoTotal(0.0);
        dashboardResponse.setRendimiento(0.0);
        dashboardResponse.setTotalDeDescuento(0.0);
        dashboardResponse.setTotalCobranzaPura(0.0);
        dashboardResponse.setMontoExcedente(0.0);
        dashboardResponse.setMultas(0.0);
        dashboardResponse.setLiquidaciones(0.0);
        dashboardResponse.setCobranzaTotal(0.0);
        dashboardResponse.setMontoDeDebitoFaltante(0.0);

        return dashboardResponse;
    }
}
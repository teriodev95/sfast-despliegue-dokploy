package tech.calaverita.sfast_xpress.utils;

import tech.calaverita.sfast_xpress.pojos.Dashboard;
import tech.calaverita.sfast_xpress.pojos.ObjectsContainer;
import tech.calaverita.sfast_xpress.threads.DashboardThread;

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

    public static Dashboard dashboard(Dashboard[] dashboards_I) {
        Dashboard dashboardResponse_O = dashboardWithEmptyValues();

        for (Dashboard dashboard : dashboards_I) {
            if (dashboardResponse_O.getGerencia() == null) {
                dashboardResponse_O.setGerencia(dashboard.getGerencia());
                dashboardResponse_O.setAnio(dashboard.getAnio());
                dashboardResponse_O.setSemana(dashboard.getSemana());
            }

            dashboardResponse_O.setClientes(dashboardResponse_O.getClientes() + dashboard.getClientes());
            dashboardResponse_O.setClientesCobrados(dashboardResponse_O.getClientesCobrados() + dashboard
                    .getClientesCobrados());
            dashboardResponse_O.setNoPagos(dashboardResponse_O.getNoPagos() + dashboard.getNoPagos());
            dashboardResponse_O.setNumeroLiquidaciones(dashboardResponse_O.getNumeroLiquidaciones() + dashboard
                    .getNumeroLiquidaciones());
            dashboardResponse_O.setPagosReducidos(dashboardResponse_O.getPagosReducidos() + dashboard
                    .getPagosReducidos());
            dashboardResponse_O.setDebitoMiercoles(dashboardResponse_O.getDebitoMiercoles() + dashboard
                    .getDebitoMiercoles());
            dashboardResponse_O.setDebitoJueves(dashboardResponse_O.getDebitoJueves() + dashboard.getDebitoJueves());
            dashboardResponse_O.setDebitoViernes(dashboardResponse_O.getDebitoViernes() + dashboard.getDebitoViernes());
            dashboardResponse_O.setDebitoTotal(dashboardResponse_O.getDebitoTotal() + dashboard.getDebitoTotal());
            dashboardResponse_O.setTotalDeDescuento(dashboardResponse_O.getTotalDeDescuento() + dashboard
                    .getTotalDeDescuento());
            dashboardResponse_O.setTotalCobranzaPura(dashboardResponse_O.getTotalCobranzaPura() + dashboard
                    .getTotalCobranzaPura());
            dashboardResponse_O.setMontoExcedente(dashboardResponse_O.getMontoExcedente() + dashboard
                    .getMontoExcedente());
            dashboardResponse_O.setMultas(dashboardResponse_O.getMultas() + dashboard.getMultas());
            dashboardResponse_O.setLiquidaciones(dashboardResponse_O.getLiquidaciones() + dashboard.getLiquidaciones());
            dashboardResponse_O.setCobranzaTotal(dashboardResponse_O.getCobranzaTotal() + dashboard.getCobranzaTotal());
            dashboardResponse_O.setMontoDeDebitoFaltante(dashboardResponse_O.getMontoDeDebitoFaltante() + dashboard
                    .getMontoDeDebitoFaltante());
        }

        dashboardResponse_O.setRendimiento(Math.round(dashboardResponse_O.getTotalCobranzaPura() / dashboardResponse_O
                .getDebitoTotal() * 100.0) / 100.0 * 100.0);

        return dashboardResponse_O;
    }

    public static Dashboard dashboardWithEmptyValues() {
        Dashboard dashboardResponse_O = new Dashboard();

        dashboardResponse_O.setClientes(0);
        dashboardResponse_O.setClientesCobrados(0);
        dashboardResponse_O.setNoPagos(0);
        dashboardResponse_O.setNumeroLiquidaciones(0);
        dashboardResponse_O.setPagosReducidos(0);
        dashboardResponse_O.setDebitoMiercoles(0.0);
        dashboardResponse_O.setDebitoJueves(0.0);
        dashboardResponse_O.setDebitoViernes(0.0);
        dashboardResponse_O.setDebitoTotal(0.0);
        dashboardResponse_O.setRendimiento(0.0);
        dashboardResponse_O.setTotalDeDescuento(0.0);
        dashboardResponse_O.setTotalCobranzaPura(0.0);
        dashboardResponse_O.setMontoExcedente(0.0);
        dashboardResponse_O.setMultas(0.0);
        dashboardResponse_O.setLiquidaciones(0.0);
        dashboardResponse_O.setCobranzaTotal(0.0);
        dashboardResponse_O.setMontoDeDebitoFaltante(0.0);

        return dashboardResponse_O;
    }
}
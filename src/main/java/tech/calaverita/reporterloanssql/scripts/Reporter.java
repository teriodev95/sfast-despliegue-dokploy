package tech.calaverita.reporterloanssql.scripts;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.calaverita.reporterloanssql.models.PagoModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.pojos.PrestamoPago;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.PrestamoService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Reporter {
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    static String agencia = "AGD043";
    static int anio = 2023;
    static int semana = 4;
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    static PrestamoService prestamoService = retrofit.create(PrestamoService.class);
    static PagoService pagoService = retrofit.create(PagoService.class);
    static Dashboard dashboard = new Dashboard();
    static ArrayList<PagoModel> pagos = new ArrayList<>();
    static ArrayList<PrestamoModel> prestamos = new ArrayList<>();
    static ArrayList<PrestamoPago> prestamoPagos = new ArrayList<>();


    public static void asignarPrestamos() {
        Call<List<PagoModel>> pagos = pagoService.getPagos(agencia, anio, semana);

        pagos.enqueue(new Callback<List<PagoModel>>() {
            @Override
            public void onResponse(Call<List<PagoModel>> call, Response<List<PagoModel>> response) {
                for (PagoModel pago : response.body()) {
                    Reporter.pagos.add(pago);
                    System.out.println(Reporter.pagos.get(0).getMonto());
                }
            }

            @Override
            public void onFailure(Call<List<PagoModel>> call, Throwable t) {
                System.out.println("Ha ocurrido un error");
            }
        });

        for (PagoModel pago: Reporter.pagos){
            System.out.println(pago.getMonto());
        }

        Call<ArrayList<PrestamoModel>> prestamos = prestamoService.getPrestamos(Reporter.pagos);

        prestamos.enqueue(new Callback<ArrayList<PrestamoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PrestamoModel>> call, Response<ArrayList<PrestamoModel>> response) {
                Reporter.prestamos = response.body();
                for(PrestamoModel prestamo: Reporter.prestamos){
                    System.out.println(prestamo.getAnio());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PrestamoModel>> call, Throwable t) {
                System.out.println("Ha ocurrido un error");
            }
        });

        for (PrestamoModel prestamo : Reporter.prestamos) {
            PrestamoPago prestamoPago = new PrestamoPago();
            for (PagoModel pago : Reporter.pagos) {
                if (prestamo.getPrestamoId().equalsIgnoreCase(pago.getPrestamoId())) {
                    prestamoPago.setPrestamo(prestamo);
                    prestamoPago.setPago(pago);
                    prestamoPagos.add(prestamoPago);
                }
            }
        }
    }

    public static void imprimirPrestamos(int indice) {
        String nameCliente = prestamoPagos.get(indice).getPrestamo().getNombres();
        String apellido1Cliente = prestamoPagos.get(indice).getPrestamo().getApellidoPaterno();
        String apellido2Cliente = prestamoPagos.get(indice).getPrestamo().getApellidoMaterno();
        String nombreCompletoCliente = nameCliente + " " + apellido1Cliente + " " + apellido2Cliente;

        System.out.println("Cliente: " + nombreCompletoCliente);
        System.out.println("Tarifa: " + prestamoPagos.get(indice).getPrestamo().getTarifa());
        System.out.println("Descuento: " + prestamoPagos.get(indice).getPrestamo().getDescuento());
        System.out.println("Pago: " + prestamoPagos.get(indice).getPago().getMonto());
        System.out.println("Saldo restante: " + prestamoPagos.get(indice).getPrestamo().getSaldo());
        System.out.println("Cobrado: " + prestamoPagos.get(indice).getPrestamo().getCobrado());
        System.out.println("Saldo total: " + prestamoPagos.get(indice).getPrestamo().getTotalAPagar());
        System.out.println(" ");
    }

    public static void verificarDatosSemanales() {
        for (int i = 0; i < prestamoPagos.size(); i++) {
            PrestamoModel prestamo = prestamoPagos.get(i).getPrestamo();
            PagoModel pago = prestamoPagos.get(i).getPago();

            //No. de Clientes
            dashboard.setClientes(dashboard.getClientes() + 1);

            //Ajsute de Tarifa
            if (pago.getMonto() == prestamo.getSaldo() + pago.getMonto() && pago.getMonto() < prestamo.getTarifa())
                prestamo.setTarifa(pago.getMonto());
            if (prestamo.getSaldo() + pago.getMonto() < prestamo.getTarifa())
                prestamo.setTarifa(prestamo.getSaldo() + pago.getMonto());

            //No pagos
            if (pago.getTarifa() == 0)
                dashboard.setNumeroDeNoPagos(dashboard.getNumeroDeNoPagos() + 1);

            //No. Liquidaciones
            if (pago.getMonto() > prestamo.getTarifa() && prestamo.getDescuento() > 0) {
                dashboard.setNumeroDeLiquidaciones(dashboard.getNumeroDeLiquidaciones() + 1);
                //Total de Descuento
                dashboard.setTotalDescuento(dashboard.getTotalDescuento() + prestamo.getDescuento());
                //Liquidaciones
                double liquidacion = pago.getMonto() - prestamo.getTarifa();
                dashboard.setLiquidacionesCobranza(dashboard.getLiquidacionesCobranza() + liquidacion);
            }

            //Pagos Reducidos
            if (pago.getMonto() < prestamo.getTarifa() && pago.getMonto() > 0)
                dashboard.setNumeroDePagosReducidos(dashboard.getNumeroDePagosReducidos() + 1);

            //Debito Miercoles
            if (prestamo.getDiaDePago().equalsIgnoreCase("Miercoles"))
                dashboard.setDebitoMiercoles(dashboard.getDebitoMiercoles() + prestamo.getTarifa());

            //Debito Jueves
            if (prestamo.getDiaDePago().equalsIgnoreCase("Jueves"))
                dashboard.setDebitoJueves(dashboard.getDebitoJueves() + prestamo.getTarifa());

            //Debito Viernes
            if (prestamo.getDiaDePago().equalsIgnoreCase("Viernes"))
                dashboard.setDebitoViernes(dashboard.getDebitoViernes() + prestamo.getTarifa());

            //Monto Excedente
            if (pago.getMonto() > prestamo.getTarifa()) {
                double excedente = pago.getMonto() - prestamo.getTarifa();
                dashboard.setMontoExcedente(dashboard.getMontoExcedente() + excedente);
            }

            //Cobranza Total
            dashboard.setCobranzaTotal(dashboard.getCobranzaTotal() + pago.getMonto());

            //Monto Debito Faltante
            if (pago.getMonto() < prestamo.getTarifa()) {
                double faltante = prestamo.getTarifa() - pago.getMonto();
                dashboard.setMontoDebitoFaltante(dashboard.getMontoDebitoFaltante() + faltante);
            }

            imprimirPrestamos(i);
        }

        //Debito Total
        dashboard.setDebitoTotal(dashboard.getDebitoMiercoles() + dashboard.getDebitoJueves() + dashboard.getDebitoViernes());
        //Cobranza Pura
        dashboard.setCobranzaPura(dashboard.getCobranzaTotal() - dashboard.getMontoExcedente());
        //Rendimiento
        dashboard.setRendimientoCobranza(dashboard.getCobranzaPura() / dashboard.getDebitoTotal() * 100);
    }

    public static void imprimirDashboard() {
        System.out.println("No. de clientes: " + dashboard.getClientes());
        System.out.println("No pagos: " + dashboard.getNumeroDeNoPagos());
        System.out.println("No. liquidaciones: " + dashboard.getNumeroDeLiquidaciones());
        System.out.println("Pagos reducidos: " + dashboard.getNumeroDePagosReducidos());
        System.out.println("Debito Miercoles: " + dashboard.getDebitoMiercoles());
        System.out.println("Debito Jueves: " + dashboard.getDebitoJueves());
        System.out.println("Debito Viernes: " + dashboard.getDebitoViernes());
        System.out.println("Debito total: " + decfor.format(dashboard.getDebitoTotal()));
        System.out.println("Rendimiento: " + decfor.format(dashboard.getRendimientoCobranza()) + "%");
        System.out.println("Total de descuento: " + decfor.format(dashboard.getTotalDescuento()));
        System.out.println("Total cobranza pura: " + decfor.format(dashboard.getCobranzaPura()));
        System.out.println("Monto excedente:" + decfor.format(dashboard.getMontoExcedente()));
        System.out.println("Multas: ");
        System.out.println("Liquidaciones: " + decfor.format(dashboard.getLiquidacionesCobranza()));
        System.out.println("Cobranza total: " + decfor.format(dashboard.getCobranzaTotal()));
        System.out.println("Monto de Debito faltante: " + decfor.format(dashboard.getMontoDebitoFaltante()));
    }

    public static void main(String[] args) {
        asignarPrestamos();
        verificarDatosSemanales();
        imprimirDashboard();
    }
}

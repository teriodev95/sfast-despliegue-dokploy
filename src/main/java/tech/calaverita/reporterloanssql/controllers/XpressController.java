package tech.calaverita.reporterloanssql.controllers;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.resolver.resource.UriResolver;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.reporterloanssql.models.AsignacionModel;
import tech.calaverita.reporterloanssql.models.PrestamoModel;
import tech.calaverita.reporterloanssql.pojos.Cobranza;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.repositories.AsignacionRepository;
import tech.calaverita.reporterloanssql.repositories.PagoRepository;
import tech.calaverita.reporterloanssql.repositories.XpressRepository;
import tech.calaverita.reporterloanssql.repositories.PrestamoRepository;
import tech.calaverita.reporterloanssql.services.FileManagerService;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

@RestController()
@RequestMapping(path = "/xpress/v1")
public class XpressController {
    @Autowired
    private XpressRepository prestamoPagoRespository;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private AsignacionRepository asignacionRepository;
    @Autowired
    private FileManagerService fileManagerService;

    @GetMapping(path = "/cobranza-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Cobranza getCobranzaByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByAgencia(agencia, anio, semana);
        String result = prestamoPagoRespository.getCobranzaByAgencia(agencia, anio, semana);

        String[] texto = result.split(",");

        Cobranza cobranza = new Cobranza();

        cobranza.setGerencia(texto[6]);
        cobranza.setAnio(anio);
        cobranza.setSemana(semana);
        cobranza.setAgencia(texto[0]);
        cobranza.setClientes(Integer.parseInt(texto[1]));
        cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
        cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
        cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
        cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
        cobranza.setPrestamos(prestamoModels);

        return cobranza;
    }

    @GetMapping(path = "/cobranza-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Cobranza> getCobranzaByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Cobranza> cobranzas = new ArrayList<>();

        ArrayList<PrestamoModel> prestamoModels = prestamoRepository.getPrestamoModelsForCobranzaByGerencia(gerencia, anio, semana);
        String[] agencias = prestamoPagoRespository.getCobranzaByGerencia(gerencia, anio, semana);

        for (int i = 0; i < agencias.length; i++) {
            String[] texto = agencias[i].split(",");

            ArrayList<PrestamoModel> prestamoModelsAux = new ArrayList<>();

            for (PrestamoModel prestamo : prestamoModels) {
                if (prestamo.getAgente().equalsIgnoreCase(texto[0]))
                    prestamoModelsAux.add(prestamo);
            }

            Cobranza cobranza = new Cobranza();

            cobranza.setGerencia(gerencia);
            cobranza.setAnio(anio);
            cobranza.setSemana(semana);
            cobranza.setAgencia(texto[0]);
            cobranza.setClientes(Integer.parseInt(texto[1]));
            cobranza.setDebitoMiercoles(Double.parseDouble(texto[2]));
            cobranza.setDebitoJueves(Double.parseDouble(texto[3]));
            cobranza.setDebitoViernes(Double.parseDouble(texto[4]));
            cobranza.setDebitoTotal(Double.parseDouble(texto[5]));
            cobranza.setPrestamos(prestamoModelsAux);

            cobranzas.add(cobranza);
        }

        return cobranzas;
    }

    @GetMapping(path = "/dashboard-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody Dashboard getDashboardByAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){

        String result = prestamoPagoRespository.getDashboardByAgencia(agencia, anio, semana);

        String[] texto = result.split(",");

        Dashboard dashboard = new Dashboard();

        dashboard.setGerencia(texto[17]);
        dashboard.setAnio(anio);
        dashboard.setSemana(semana);
        dashboard.setAgencia(texto[0]);
        dashboard.setClientes(Integer.parseInt(texto[1]));
        dashboard.setNoPagos(Integer.parseInt(texto[2]));
        dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[3]));
        dashboard.setPagosReducidos(Integer.parseInt(texto[4]));
        dashboard.setDebitoMiercoles(Double.parseDouble(texto[5]));
        dashboard.setDebitoJueves(Double.parseDouble(texto[6]));
        dashboard.setDebitoViernes(Double.parseDouble(texto[7]));
        dashboard.setDebitoTotal(Double.parseDouble(texto[8]));
        dashboard.setRendimiento(Double.parseDouble(texto[9]));
        dashboard.setTotalDeDescuento(Double.parseDouble(texto[10]));
        dashboard.setTotalCobranzaPura(Double.parseDouble(texto[11]));
        dashboard.setMontoExcedente(Double.parseDouble(texto[12]));
        dashboard.setMultas(Double.parseDouble(texto[13]));
        dashboard.setLiquidaciones(Double.parseDouble(texto[14]));
        dashboard.setCobranzaTotal(Double.parseDouble(texto[15]));
        dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[16]));

        return dashboard;
    }

    @GetMapping(path = "/dashboard-gerencia/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Dashboard> getDashboardByGerencia(@PathVariable("gerencia") String gerencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        String[] agencias = prestamoPagoRespository.getDashboardByGerencia(gerencia, anio, semana);

        for (String agencia : agencias) {
            String[] texto = agencia.split(",");

            Dashboard dashboard = new Dashboard();

            dashboard.setGerencia(gerencia);
            dashboard.setAnio(anio);
            dashboard.setSemana(semana);
            dashboard.setAgencia(texto[0]);
            dashboard.setClientes(Integer.parseInt(texto[1]));
            dashboard.setNoPagos(Integer.parseInt(texto[2]));
            dashboard.setNumeroLiquidaciones(Integer.parseInt(texto[3]));
            dashboard.setPagosReducidos(Integer.parseInt(texto[4]));
            dashboard.setDebitoMiercoles(Double.parseDouble(texto[5]));
            dashboard.setDebitoJueves(Double.parseDouble(texto[6]));
            dashboard.setDebitoViernes(Double.parseDouble(texto[7]));
            dashboard.setDebitoTotal(Double.parseDouble(texto[8]));
            dashboard.setRendimiento(Double.parseDouble(texto[9]));
            dashboard.setTotalDeDescuento(Double.parseDouble(texto[10]));
            dashboard.setTotalCobranzaPura(Double.parseDouble(texto[11]));
            dashboard.setMontoExcedente(Double.parseDouble(texto[12]));
            dashboard.setMultas(Double.parseDouble(texto[13]));
            dashboard.setLiquidaciones(Double.parseDouble(texto[14]));
            dashboard.setCobranzaTotal(Double.parseDouble(texto[15]));
            dashboard.setMontoDeDebitoFaltante(Double.parseDouble(texto[16]));

            dashboards.add(dashboard);
        }

        return dashboards;
    }

    @GetMapping(path = "/balance-de-agencia/{agencia}/{anio}/{semana}")
    public @ResponseBody ResponseEntity<byte[]> getBalanceDeAgencia(@PathVariable("agencia") String agencia, @PathVariable("anio") int anio, @PathVariable("semana") int semana){

        String uri = "src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\pdf\\balance-de-agencia_" + agencia + "_" + anio + "_" + semana + ".pdf";
        String fileName = "balance-de-agencia_" + agencia + "_" + anio + "_" + semana + ".pdf";
        File file = new File(uri);

        if(file.exists())
            return fileManagerService.getPdf(uri, fileName);

//        if(ruta.exists())
//            return uri;

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "    \n" +
                "    <style>\n" +
                "        * {\n" +
                "            margin: 0px;\n" +
                "            padding: 0px;\n" +
                "            box-sizing: border-box;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "    \n" +
                "        div {\n" +
                "            margin: 10px 0px;\n" +
                "        }\n" +
                "    \n" +
                "        table, td {\n" +
                "            border: 3px solid black;\n" +
                "            border-collapse: collapse;\n" +
                "            padding: 5px;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            padding: 5px;\n" +
                "        }\n" +
                "    \n" +
                "        .alignLeft{\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "    \n" +
                "        .alignRight{\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "    </style>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div style=\"width: 80%; margin: auto; margin-top: 20px;\">\n" +
                "        <table style=\"border: none; width: 100%;\">\n" +
                "            <col style=\"width: 49%;\"><col style=\"width: 2%;\"><col style=\"width: 49%;\">\n" +
                "            <tr>\n" +
                "                <td colspan=\"1\" style=\"border: none; vertical-align: top;\">\n" +
                "                    <div style=\"width: 100%;\">\n" +
                "                        <div>\n" +
                "                            <table style=\"border: none; width: 100%;\">\n" +
                "                                <col style=\"width: 10%;\"><col style=\"width: 10%;\"><col style=\"width: 10%;\"><col style=\"width: 10%;\"><col style=\"width: 60%;\">\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"5\" style=\"text-align: center;\">\n" +
                "                                        <h2 style=\"font-weight: lighter;\">\n" +
                "                                            CIERRE SEMANAL\n" +
                "                                        </h2>\n" +
                "                                        <br>\n" +
                "                                        <h3>\n" +
                "                                            BALANCE DE AGENCIA\n" +
                "                                        </h3>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                   <td style=\"border: none;\">\n" +
                "\n" +
                "                                   </td> \n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\">\n" +
                "\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        ZONA:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: 2px solid black\">\n" +
                "                                        ((zona))\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        GERENTE:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none; border-bottom: 2px solid black;\">\n" +
                "                                        ((gerente))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border: none\">\n" +
                "\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\">\n" +
                "\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        AG:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: 2px solid black;\">\n" +
                "                                        ((agencia))\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        AGENTE:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none; border-bottom: 2px solid black;\">\n" +
                "                                        ((agente))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border: none;\">\n" +
                "\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                            <table style=\"border: none; width: 100%;\">\n" +
                "                                <col style=\"width: 50%;\"><col style=\"width: 20%;\"><col style=\"width: 10%;\"><col style=\"width: 10%;\"><col style=\"width: 10%;\">\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\">\n" +
                "\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        % DE AG:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: 2px solid black;\">\n" +
                "                                        ((rendimiento))\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: none;\" class=\"alignRight\">\n" +
                "                                        NIVEL:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" style=\"border: 2px solid black;\">\n" +
                "                                        ((nivel))\n" +
                "                                    </td>                                    \n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                        <div style=\"width: 100%;\">\n" +
                "                            <table style=\"width: 100%;\" class=\"alignRight\">\n" +
                "                                <col style=\"width: 75%;\"><col style=\"width: 25%;\">\n" +
                "                                <tr style=\"text-align: center;\">\n" +
                "                                    <td colspan=\"2\">\n" +
                "                                        INGRESOS DEL AGENTE\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        COBRANZA PURA:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((cobranzaPura))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        MONTO EXCEDENTE:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((montoExcedente))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        LIQUIDACIONES:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((liquidaciones))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        MULTAS:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((multas))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        OTROS: (((otros1)))\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((otrosIngresos))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        TOTAL DE INGRESOS:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        $((totalIngresos))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                        <div style=\"width: 100%;\">\n" +
                "                            <table style=\"width: 100%;\" class=\"alignRight\">\n" +
                "                                <col style=\"width: 75%;\"><col style=\"width: 25%;\">\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        % DE COMISION POR COBRANZA:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        ((comision))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td colspan=\"1\">\n" +
                "                                        % DE BONO MENSUAL:\n" +
                "                                    </td>\n" +
                "                                    <td colspan=\"1\" class=\"alignLeft\">\n" +
                "                                        ((bono))\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </td>\n" +
                "\n" +
                "                <td colspan=\"1\" style=\"border: none; vertical-align: top;\">\n" +
                "\n" +
                "                </td>\n" +
                "\n" +
                "                <td colspan=\"1\" style=\"border: none; vertical-align: top;\">\n" +
                "                    <div style=\"width: 100%;\">\n" +
                "                        <table style=\"border: none;\">\n" +
                "                            <col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\">\n" +
                "                            <col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\">\n" +
                "                            <col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\">\n" +
                "                            <col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\"><col style=\"width: 5%;\">\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"13\" style=\"border: none\">\n" +
                "\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: none\">\n" +
                "                                    SEM.:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: 2px solid black;\">\n" +
                "                                    ((semana))\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: none\">\n" +
                "                                    AÑO:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: 2px solid black;\">\n" +
                "                                    ((anio))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"15\" style=\"border: none;\">\n" +
                "\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: none\">\n" +
                "                                    Fecha:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    D\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: none; border-bottom: 2px solid black;\">\n" +
                "                                    ((dia))\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    /M\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\" style=\"border: none; border-bottom: 2px solid black;\">\n" +
                "                                    ((mes))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td style=\"border: none;\">\n" +
                "\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"18\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    Total de clientes de la Agencia:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: 2px solid black;\">\n" +
                "                                    #((clientes))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"18\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    Total de clientes con \"PAGO REDUCIDO\":\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: 2px solid black;\">\n" +
                "                                    #((pagosReducidos))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"18\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    Total de clientes con \"NO PAGO\":\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: 2px solid black;\">\n" +
                "                                    #((noPagos))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"18\" style=\"border: none\" class=\"alignRight\">\n" +
                "                                    Total de clientes liquidados:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"2\" style=\"border: 2px solid black;\">\n" +
                "                                    #((numeroLiquidaciones))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                    <div style=\"width: 100%;\">\n" +
                "                        <table style=\"width: 100%;\">\n" +
                "                            <col style=\"width: 75%;\"><col style=\"width: 25%;\">\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"2\" style=\"text-align: center;\">\n" +
                "                                    EGRESOS DEL AGENTE  \n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    ASIGNACIONES PREVIAS DE EFECTIVO:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((asignaciones))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    OTROS:(((otros2)))\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((otrosEgresos)) \n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    EFECTIVO ENTREGADO EN CIERRE:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((efectivoCierre))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    TOTAL DE EGRESOS:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((totalEgresos))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                    <div style=\"width: 100%;\">\n" +
                "                        <table style=\"width: 100%;\">\n" +
                "                            <col style=\"width: 75%;\"><col style=\"width: 25%;\">\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"2\" style=\"text-align: center\">\n" +
                "                                    EGRESOS DEL GERENTE (Pagos a agente en CIERRE)\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    PAGO DE COMISIÓN POR COBRANZA:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((pagoComisionCobranza))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    PAGO DE COMISIÓN POR VENTAS:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((pagoComisionVentas))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    BONOS:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((bonos))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td colspan=\"1\" class=\"alignRight\">\n" +
                "                                    EFECTIVO RESTANTE DE CIERRE:\n" +
                "                                </td>\n" +
                "                                <td colspan=\"1\">\n" +
                "                                    $((bonosEgresos))\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        Dashboard dashboard = getDashboardByAgencia(agencia, anio, semana);
        ArrayList<AsignacionModel> asignaciones = asignacionRepository.getAsignacionModelByAgenciaAnioAndSemana(agencia, anio, semana);
        Double asignacionesMonto = 0.0;
        for(AsignacionModel asignacion: asignaciones){
            asignacionesMonto =+ asignacion.getMonto();
        }

        java.util.Date fecha = new Date();

        html = html.replace("((gerente))", dashboard.getGerencia());
        html = html.replace("((agencia))", dashboard.getAgencia());
        html = html.replace("((agente))", "Prueba de agente");
        html = html.replace("((rendimiento))", String.valueOf(dashboard.getRendimiento()));
        html = html.replace("((nivel))", "Plata");
        html = html.replace("((cobranzaPura))", String.valueOf(dashboard.getTotalCobranzaPura()));
        html = html.replace("((montoExcedente))", String.valueOf(dashboard.getMontoExcedente()));
        html = html.replace("((liquidaciones))", String.valueOf(dashboard.getLiquidaciones()));
        html = html.replace("((multas))", String.valueOf(dashboard.getMultas()));
        html = html.replace("((otro1))", "Otros");
        html = html.replace("((totalIngresos))", String.valueOf(dashboard.getCobranzaTotal()));
        html = html.replace("((comision))", "7%");
        html = html.replace("((bono))", "2%");
        html = html.replace("((semana))", String.valueOf(semana));
        html = html.replace("((anio))", String.valueOf(anio));
        html = html.replace("((dia))", String.valueOf(fecha.getDay()));
        html = html.replace("((mes))", String.valueOf(fecha.getMonth()));
        html = html.replace("((clientes))", String.valueOf(dashboard.getClientes()));
        html = html.replace("((pagosReducidos))", String.valueOf(dashboard.getPagosReducidos()));
        html = html.replace("((noPagos))", String.valueOf(dashboard.getNoPagos()));
        html = html.replace("((numeroLiquidaciones))", String.valueOf(dashboard.getNumeroLiquidaciones()));
        html = html.replace("((asignaciones))", String.valueOf(asignacionesMonto));
        html = html.replace("((otros2))", "Otros");
        html = html.replace("((otrosEgresos))", "0");
        html = html.replace("((efectivoCierre))", "0");
        html = html.replace("((totalEgresos))", "0");
        html = html.replace("((pagoComisionCobranza))", "0");
        html = html.replace("((pagoComisionVentas))", "0");
        html = html.replace("((bonos))", "0");
        html = html.replace("((bonosEgresos))", "0");

        try{
            Path path = Paths.get("src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\html\\balance-de-agencia_" + agencia + "_" + anio + "_" + semana + ".html");
            Files.writeString(path, html);

            FileOutputStream fileOutputStream = new FileOutputStream("src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\pdf\\balance-de-agencia_" + agencia + "_" + anio + "_" + semana + ".pdf");
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(fileOutputStream));
            pdfDocument.setDefaultPageSize(PageSize.LEDGER);

            FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\tech\\calaverita\\reporterloanssql\\resources\\balancesDeAgencias\\html\\balance-de-agencia_" + agencia + "_" + anio + "_" + semana + ".html");

            HtmlConverter.convertToPdf(
              fileInputStream,
              pdfDocument
            );

        } catch (Exception e){

        }
        return fileManagerService.getPdf(uri, fileName);
    }
}
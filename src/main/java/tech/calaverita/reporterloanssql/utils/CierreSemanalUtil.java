package tech.calaverita.reporterloanssql.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import tech.calaverita.reporterloanssql.itext.PdfStyleManager;
import tech.calaverita.reporterloanssql.itext.fonts.Fonts;
import tech.calaverita.reporterloanssql.persistence.dto.cierre_semanal.*;
import tech.calaverita.reporterloanssql.persistence.entities.UsuarioEntity;
import tech.calaverita.reporterloanssql.persistence.entities.cierre_semanal.*;
import tech.calaverita.reporterloanssql.pojos.Dashboard;
import tech.calaverita.reporterloanssql.services.PagoService;
import tech.calaverita.reporterloanssql.services.cierre_semanal.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CierreSemanalUtil {
    private static BalanceAgenciaService balanceAgenciaService;
    private static CierreSemanalService cierreSemanalService;
    private static EgresosAgenteService egresosAgenteService;
    private static EgresosGerenteService egresosGerenteService;
    private static IngresosAgenteService ingresosAgenteService;
    private static PagoService pagoService;
    private static Fonts fuentes = new Fonts();

    public CierreSemanalUtil(
            BalanceAgenciaService balanceAgenciaService,
            CierreSemanalService cierreSemanalService,
            EgresosAgenteService egresosAgenteService,
            EgresosGerenteService egresosGerenteService,
            IngresosAgenteService ingresosAgenteService,
            PagoService pagoService
    ) {
        CierreSemanalUtil.balanceAgenciaService = balanceAgenciaService;
        CierreSemanalUtil.cierreSemanalService = cierreSemanalService;
        CierreSemanalUtil.egresosAgenteService = egresosAgenteService;
        CierreSemanalUtil.egresosGerenteService = egresosGerenteService;
        CierreSemanalUtil.ingresosAgenteService = ingresosAgenteService;
        CierreSemanalUtil.pagoService = pagoService;
    }

    public static CierreSemanalDTO getCierreSemanalDTO(
            CierreSemanalEntity cierreSemanalEntity
    ) throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = CierreSemanalUtil.cierreSemanalService
                .getCierreSemanalDTO(cierreSemanalEntity);

        CompletableFuture<Optional<BalanceAgenciaEntity>> balanceAgenciaEntity = CierreSemanalUtil.balanceAgenciaService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<EgresosAgenteEntity>> egresosAgenteEntity = CierreSemanalUtil.egresosAgenteService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<EgresosGerenteEntity>> egresosGerenteEntity = CierreSemanalUtil.egresosGerenteService
                .findById(cierreSemanalEntity.getId());
        CompletableFuture<Optional<IngresosAgenteEntity>> ingresosAgenteEntity = CierreSemanalUtil.ingresosAgenteService
                .findById(cierreSemanalEntity.getId());

        CompletableFuture.allOf(balanceAgenciaEntity, egresosAgenteEntity, egresosGerenteEntity,
                ingresosAgenteEntity);

        BalanceAgenciaDTO balanceAgenciaDTO = CierreSemanalUtil.balanceAgenciaService
                .getBalanceAgenciaDTO(balanceAgenciaEntity.get().get());
        EgresosAgenteDTO egresosAgenteDTO = CierreSemanalUtil.egresosAgenteService
                .getEgresosGerenteDTO(egresosAgenteEntity.get().get());
        EgresosGerenteDTO egresosGerenteDTO = CierreSemanalUtil.egresosGerenteService
                .getEgresosGerenteDTO(egresosGerenteEntity.get().get());
        IngresosAgenteDTO ingresosAgenteDTO = CierreSemanalUtil.ingresosAgenteService
                .getIngresosAgenteDTO(ingresosAgenteEntity.get().get());

        cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
        cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
        cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
        cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);
        cierreSemanalDTO.setIsAgenciaCerrada(true);

        return cierreSemanalDTO;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public static CierreSemanalDTO getCierreSemanalDTO(
            Dashboard dashboard,
            List<UsuarioEntity> darrusuarEnt,
            double asignaciones
    ) throws ExecutionException, InterruptedException {
        CierreSemanalDTO cierreSemanalDTO = new CierreSemanalDTO();

        // To easy code
        CompletableFuture<Integer> clientePagoCompleto = CierreSemanalUtil.pagoService
                .getClientesPagoCompletoByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());
        CompletableFuture<Double> cobranzaTotal = CierreSemanalUtil.pagoService
                .getCobranzaTotalByAgenciaAnioAndSemanaAsync(dashboard.getAgencia(), dashboard.getAnio(),
                        dashboard.getSemana());

        // To easy code
        UsuarioEntity agenteUsuarioEntity = darrusuarEnt.get(0);
        UsuarioEntity gerenteUsuarioEntity = darrusuarEnt.get(1);
        String nombreAgente = agenteUsuarioEntity.getNombre() + " " + agenteUsuarioEntity.getApellidoPaterno() + " "
                + agenteUsuarioEntity.getApellidoMaterno();
        String nombreGerente = gerenteUsuarioEntity.getNombre() + " " + gerenteUsuarioEntity.getApellidoPaterno()
                + " " + gerenteUsuarioEntity.getApellidoMaterno();

        BalanceAgenciaDTO balanceAgenciaDTO = new BalanceAgenciaDTO();
        {
            balanceAgenciaDTO.setZona(dashboard.getGerencia());
            balanceAgenciaDTO.setGerente(nombreGerente);
            balanceAgenciaDTO.setAgencia(dashboard.getAgencia());
            balanceAgenciaDTO.setAgente(nombreAgente);
            balanceAgenciaDTO.setRendimiento(dashboard.getRendimiento());
            balanceAgenciaDTO.setNivel(BalanceAgenciaUtil.getNivelAgente(dashboard.getClientes(),
                    dashboard.getRendimiento() / 100, agenteUsuarioEntity));
            balanceAgenciaDTO.setClientes(dashboard.getClientes());
            balanceAgenciaDTO.setPagosReducidos(dashboard.getPagosReducidos());
            balanceAgenciaDTO.setNoPagos(dashboard.getNoPagos());
            balanceAgenciaDTO.setLiquidaciones(dashboard.getNumeroLiquidaciones());
        }

        EgresosAgenteDTO egresosAgenteDTO = new EgresosAgenteDTO();
        {
            egresosAgenteDTO.setAsignaciones(asignaciones);

            cobranzaTotal.join();
            egresosAgenteDTO.setEfectivoEntregadoCierre(cobranzaTotal.get() - egresosAgenteDTO.getAsignaciones());
        }

        EgresosGerenteDTO egresosGerenteDTO = new EgresosGerenteDTO();
        {
            egresosGerenteDTO.setPorcentajeComisionCobranza(BalanceAgenciaUtil
                    .getPorcentajeComisionCobranza(balanceAgenciaDTO.getNivel()));

            clientePagoCompleto.join();

            // To easy code
            int porcentajeBonoMensual = BalanceAgenciaUtil.getPorcentajeBonoMensual(clientePagoCompleto.get(),
                    dashboard.getRendimiento(), agenteUsuarioEntity);

            egresosGerenteDTO.setPorcentajeBonoMensual(porcentajeBonoMensual);

            cobranzaTotal.join();

            egresosGerenteDTO.setPagoComisionCobranza(cobranzaTotal.get() / 100
                    * egresosGerenteDTO.getPorcentajeComisionCobranza());
            egresosGerenteDTO.setBonos(cobranzaTotal.get() / 100 * egresosGerenteDTO.getPorcentajeBonoMensual());
        }

        IngresosAgenteDTO ingresosAgenteDTO = new IngresosAgenteDTO();
        {
            ingresosAgenteDTO.setCobranzaPura(dashboard.getTotalCobranzaPura());
            ingresosAgenteDTO.setMontoExcedente(dashboard.getMontoExcedente());
            ingresosAgenteDTO.setLiquidaciones(dashboard.getLiquidaciones());
            ingresosAgenteDTO.setMultas(dashboard.getMultas());
        }

        cierreSemanalDTO.setSemana(dashboard.getSemana());
        cierreSemanalDTO.setAnio(dashboard.getAnio());
        cierreSemanalDTO.setBalanceAgencia(balanceAgenciaDTO);
        cierreSemanalDTO.setEgresosAgente(egresosAgenteDTO);
        cierreSemanalDTO.setEgresosGerente(egresosGerenteDTO);
        cierreSemanalDTO.setIngresosAgente(ingresosAgenteDTO);
        cierreSemanalDTO.setPinAgente(agenteUsuarioEntity.getPin());
        cierreSemanalDTO.setIsAgenciaCerrada(false);
        cierreSemanalDTO.setDia(LocalDate.now().getDayOfMonth());

        String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es",
                "ES"));
        String primeraLetra = mes.substring(0, 1);
        String mayuscula = primeraLetra.toUpperCase();
        String demasLetras = mes.substring(1, mes.length());
        mes = mayuscula + demasLetras;
        cierreSemanalDTO.setMes(mes);

        return cierreSemanalDTO;
    }

    public static void createCierreSemanalPDF(
            CierreSemanalDTO dto
    ) throws DocumentException, FileNotFoundException {
        Rectangle hoja = new Rectangle(612f, 360f);
        Document document = new Document(hoja, 0f, 0f, 5f, 5f);

        // To easy code
        String id = dto.getBalanceAgencia().getZona() + '-' + dto.getBalanceAgencia().getAgencia() + '-'
                + dto.getAnio() + '-' + dto.getSemana();

        PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.dir")
                + "/src/cierres_semanales/"+ id + ".pdf"));
        document.open();

        PdfPTable tablaCierreSemanal = new PdfPTable(3);
        tablaCierreSemanal.setWidths(new float[]{1.35f, .1f, 1.64f});

        PdfPCell columnaIzquierda = new PdfPCell();
        columnaIzquierda.setBorder(0);

        PdfPTable tablaColumnaIzquierda = new PdfPTable(1);
        tablaColumnaIzquierda.setWidthPercentage(100);

        tablaColumnaIzquierda.addCell(CierreSemanalUtil.getTitulo());
        tablaColumnaIzquierda.addCell(CierreSemanalUtil.getBalanceAgencia(dto.getBalanceAgencia()));
        tablaColumnaIzquierda.addCell(CierreSemanalUtil.getIngresosAgente(dto.getIngresosAgente()));
        tablaColumnaIzquierda.addCell(CierreSemanalUtil.getPorcentajesEgresosGerente(dto.getEgresosGerente()));

        columnaIzquierda.addElement(tablaColumnaIzquierda);

        PdfPCell columnaCentral = new PdfPCell();
        columnaCentral.setBorder(0);

        PdfPCell columnaDerecha = new PdfPCell();
        columnaDerecha.setBorder(0);

        PdfPTable tablaColumnaDerecha = new PdfPTable(1);
        tablaColumnaDerecha.setWidthPercentage(100);

        tablaColumnaDerecha.addCell(CierreSemanalUtil.getSemana(dto));
        tablaColumnaDerecha.addCell(CierreSemanalUtil.getCobranza(dto.getBalanceAgencia()));
        tablaColumnaDerecha.addCell(CierreSemanalUtil.getEgresosAgente(dto.getEgresosAgente()));
        tablaColumnaDerecha.addCell(CierreSemanalUtil.getEgresosGerente(dto.getEgresosGerente()));

        columnaDerecha.addElement(tablaColumnaDerecha);

        tablaCierreSemanal.setWidthPercentage(100);
        tablaCierreSemanal.addCell(columnaIzquierda);
        tablaCierreSemanal.addCell(columnaCentral);
        tablaCierreSemanal.addCell(columnaDerecha);

        document.add(tablaCierreSemanal);
        document.close();
    }

    private static PdfPCell getTitulo() {
        PdfPCell cellTitulo = new PdfPCell();
        PdfStyleManager.setStyleForCellTitles(cellTitulo);

        Paragraph titulo = fuentes.bold("CIERRE SEMANAL", 18, 1,
                PdfStyleManager.getWhiteBaseColor());
        Paragraph subTitulo = fuentes.bold("BALANCE DE AGENCIA", 11, 1,
                PdfStyleManager.getWhiteBaseColor());

        cellTitulo.addElement(titulo);
        cellTitulo.addElement(subTitulo);

        return cellTitulo;
    }

    private static PdfPCell getBalanceAgencia(
            BalanceAgenciaDTO balanceAgenciaDTO
    ) {
        PdfPCell cellBalanceAgencia = new PdfPCell();
        cellBalanceAgencia.setBorder(0);
        cellBalanceAgencia.setPadding(0);

        PdfPTable tablaBalanceAgencia = new PdfPTable(1);
        tablaBalanceAgencia.setWidthPercentage(100);

        PdfPCell cellZonaYGerente = new PdfPCell(fuentes.regular("ZONA: " + balanceAgenciaDTO.getZona()
                        + "   GERENTE: " + balanceAgenciaDTO.getGerente(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellZonaYGerente);

        PdfPCell cellAgenciaYAgente = new PdfPCell(fuentes.regular("AG: " + balanceAgenciaDTO.getAgencia()
                        + "   AGENTE: " + balanceAgenciaDTO.getAgente(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellAgenciaYAgente);

        PdfPCell cellRendimientoYNivel = new PdfPCell(fuentes.regular("% DE AG: " + balanceAgenciaDTO
                        .getRendimiento() + "   NIVEL: " + balanceAgenciaDTO.getNivel(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellRendimientoYNivel);

        tablaBalanceAgencia.addCell(cellZonaYGerente);
        tablaBalanceAgencia.addCell(cellAgenciaYAgente);
        tablaBalanceAgencia.addCell(cellRendimientoYNivel);

        cellBalanceAgencia.addElement(tablaBalanceAgencia);
        return cellBalanceAgencia;
    }

    private static PdfPCell getIngresosAgente(
            IngresosAgenteDTO ingresosAgenteDTO
    ) throws DocumentException {
        PdfPCell cellIngresosAgente = new PdfPCell();
        cellIngresosAgente.setBorder(0);
        cellIngresosAgente.setPadding(0);
        cellIngresosAgente.setPaddingTop(5f);

        PdfPTable tablaIngresosAgente = new PdfPTable(2);
        tablaIngresosAgente.setWidthPercentage(100);
        tablaIngresosAgente.setWidths(new float[]{1.3f, .7f});

        PdfPCell cellTitulo = new PdfPCell(fuentes.regular("INGRESOS DEL AGENTE", 11,
                PdfStyleManager.getWhiteBaseColor()));
        cellTitulo.setColspan(2);
        PdfStyleManager.setStyleForCellTitles(cellTitulo);

        PdfPCell cellCobranzaPura = new PdfPCell(fuentes.regular("COBRANZA PURA:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellCobranzaPura);

        PdfPCell cellMontoExcedente = new PdfPCell(fuentes.regular("MONTO EXCEDENTE:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellMontoExcedente);

        PdfPCell cellLiquidaciones = new PdfPCell(fuentes.regular("LIQUIDACIONES:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellLiquidaciones);

        PdfPCell cellMultas = new PdfPCell(fuentes.regular("MULTAS:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellMultas);

        PdfPCell cellOtros = new PdfPCell(fuentes.regular("OTROS (" + ingresosAgenteDTO.getMotivoOtros() + "):", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellOtros);

        PdfPCell cellTotalIngresos = new PdfPCell(fuentes.regular("TOTAL DE INGRESOS: $" + ingresosAgenteDTO
                .getTotal(), 11, PdfStyleManager.getPrimaryBasecolor()));
        cellTotalIngresos.setColspan(2);
        PdfStyleManager.setStyleFillColorCell(cellTotalIngresos);

        PdfPCell cellCobranzaPura2 = new PdfPCell(fuentes.regular("$" + ingresosAgenteDTO.getCobranzaPura(),
                9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellCobranzaPura2);

        PdfPCell cellMontoExcedente2 = new PdfPCell(fuentes.regular("$" + ingresosAgenteDTO.getMontoExcedente(),
                9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellMontoExcedente2);

        PdfPCell cellLiquidaciones2 = new PdfPCell(fuentes.regular("$" + ingresosAgenteDTO
                .getLiquidaciones(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellLiquidaciones2);

        PdfPCell cellMultas2 = new PdfPCell(fuentes.regular("$" + ingresosAgenteDTO.getMultas(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellMultas2);

        PdfPCell cellOtros2 = new PdfPCell(fuentes.regular("$" + ingresosAgenteDTO.getOtros(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellOtros2);

        tablaIngresosAgente.addCell(cellTitulo);
        tablaIngresosAgente.addCell(cellCobranzaPura);
        tablaIngresosAgente.addCell(cellCobranzaPura2);
        tablaIngresosAgente.addCell(cellMontoExcedente);
        tablaIngresosAgente.addCell(cellMontoExcedente2);
        tablaIngresosAgente.addCell(cellLiquidaciones);
        tablaIngresosAgente.addCell(cellLiquidaciones2);
        tablaIngresosAgente.addCell(cellMultas);
        tablaIngresosAgente.addCell(cellMultas2);
        tablaIngresosAgente.addCell(cellOtros);
        tablaIngresosAgente.addCell(cellOtros2);
        tablaIngresosAgente.addCell(cellTotalIngresos);

        cellIngresosAgente.addElement(tablaIngresosAgente);

        return cellIngresosAgente;
    }

    private static PdfPCell getPorcentajesEgresosGerente(
            EgresosGerenteDTO egresosGerenteDTO
    ) {
        PdfPCell cellPorcentajesEgresosGerente = new PdfPCell();
        cellPorcentajesEgresosGerente.setBorder(0);
        cellPorcentajesEgresosGerente.setPadding(0);
        cellPorcentajesEgresosGerente.setPaddingTop(5f);

        PdfPTable tablaPorcentajesEgresosGerente = new PdfPTable(1);
        tablaPorcentajesEgresosGerente.setWidthPercentage(100);

        PdfPCell cellComisionCobranza = new PdfPCell(fuentes.regular("% DE COMISIÓN POR COBRANZA: "
                        + egresosGerenteDTO.getPorcentajeComisionCobranza() + "%", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellComisionCobranza);

        PdfPCell cellBonoMensual = new PdfPCell(fuentes.regular("% DE BONO MENSUAL: " + egresosGerenteDTO
                .getPorcentajeBonoMensual() + "%", 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellBonoMensual);

        tablaPorcentajesEgresosGerente.addCell(cellComisionCobranza);
        tablaPorcentajesEgresosGerente.addCell(cellBonoMensual);

        cellPorcentajesEgresosGerente.addElement(tablaPorcentajesEgresosGerente);

        return cellPorcentajesEgresosGerente;
    }

    private static PdfPCell getSemana(
            CierreSemanalDTO dto
    ) {
        PdfPCell cellSemana = new PdfPCell();
        PdfStyleManager.setStyleForCellTitles(cellSemana);

        Paragraph semana = fuentes.bold("SEM: " + dto.getSemana() + " AÑO: " + dto.getAnio(), 11, 1,
                PdfStyleManager.getWhiteBaseColor());
        Paragraph fecha = fuentes.bold("FECHA: D " + dto.getDia() + " /M " + dto.getMes(), 9, 1,
                PdfStyleManager.getWhiteBaseColor());

        cellSemana.addElement(semana);
        cellSemana.addElement(fecha);

        return cellSemana;
    }

    private static PdfPCell getCobranza(
            BalanceAgenciaDTO balanceAgenciaDTO
    ) {
        PdfPCell cellCobranza = new PdfPCell();
        cellCobranza.setBorder(0);
        cellCobranza.setPadding(0);

        PdfPTable tablaBalanceAgencia = new PdfPTable(1);
        tablaBalanceAgencia.setWidthPercentage(100);

        PdfPCell cellClientes = new PdfPCell(fuentes.regular("Total de clientes de la agencia: "
                + balanceAgenciaDTO.getClientes(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellClientes);

        PdfPCell cellPagoReducido = new PdfPCell(fuentes.regular("Total de clientes con PAGO REDUCIDO: "
                + balanceAgenciaDTO.getPagosReducidos(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellPagoReducido);

        PdfPCell cellNoPago = new PdfPCell(fuentes.regular("Total de clientes con NO PAGO: "
                + balanceAgenciaDTO.getNoPagos(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellNoPago);

        PdfPCell cellLiquidaciones = new PdfPCell(fuentes.regular("Total de clientes liquidados: "
                + balanceAgenciaDTO.getLiquidaciones(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellLiquidaciones);

        tablaBalanceAgencia.addCell(cellClientes);
        tablaBalanceAgencia.addCell(cellPagoReducido);
        tablaBalanceAgencia.addCell(cellNoPago);
        tablaBalanceAgencia.addCell(cellLiquidaciones);

        cellCobranza.addElement(tablaBalanceAgencia);
        return cellCobranza;
    }

    private static PdfPCell getEgresosAgente(
            EgresosAgenteDTO egresosAgenteDTO
    ) throws DocumentException {
        PdfPCell cellIngresosAgente = new PdfPCell();
        cellIngresosAgente.setBorder(0);
        cellIngresosAgente.setPadding(0);
        cellIngresosAgente.setPaddingTop(5f);

        PdfPTable tablaIngresosAgente = new PdfPTable(2);
        tablaIngresosAgente.setWidthPercentage(100);
        tablaIngresosAgente.setWidths(new float[]{1.3f, .7f});

        PdfPCell cellTitulo = new PdfPCell(fuentes.regular("EGRESOS DEL AGENTE", 11,
                PdfStyleManager.getWhiteBaseColor()));
        cellTitulo.setColspan(2);
        PdfStyleManager.setStyleForCellTitles(cellTitulo);

        PdfPCell cellAsignaciones = new PdfPCell(fuentes.regular("ASIGNACIONES PREVIAS DE EFECTIVO:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellAsignaciones);

        PdfPCell cellOtros = new PdfPCell(fuentes.regular("OTROS (" + egresosAgenteDTO.getMotivoOtros() + "):", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellOtros);

        PdfPCell cellEfectivoEntregadoCierre = new PdfPCell(fuentes.regular("EFECTIVO ENTREGADO EN CIERRE:",
                9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellEfectivoEntregadoCierre);

        PdfPCell cellTotalEgresos = new PdfPCell(fuentes.regular("TOTAL DE EGRESOS: $" + egresosAgenteDTO
                .getTotal(), 9, PdfStyleManager.getPrimaryBasecolor()));
        cellTotalEgresos.setColspan(2);
        PdfStyleManager.setStyleFillColorCell(cellTotalEgresos);

        PdfPCell cellAsignaciones2 = new PdfPCell(fuentes.regular("$" + egresosAgenteDTO.getAsignaciones(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        cellTitulo.setColspan(2);
        PdfStyleManager.setStyleNormalCell(cellAsignaciones2);

        PdfPCell cellOtros2 = new PdfPCell(fuentes.regular("$" + egresosAgenteDTO.getOtros(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellOtros2);

        PdfPCell cellEfectivoEntregadoCierre2 = new PdfPCell(fuentes.regular("$" + egresosAgenteDTO
                .getEfectivoEntregadoCierre(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellEfectivoEntregadoCierre2);

        tablaIngresosAgente.addCell(cellTitulo);
        tablaIngresosAgente.addCell(cellAsignaciones);
        tablaIngresosAgente.addCell(cellAsignaciones2);
        tablaIngresosAgente.addCell(cellOtros);
        tablaIngresosAgente.addCell(cellOtros2);
        tablaIngresosAgente.addCell(cellEfectivoEntregadoCierre);
        tablaIngresosAgente.addCell(cellEfectivoEntregadoCierre2);
        tablaIngresosAgente.addCell(cellTotalEgresos);

        cellIngresosAgente.addElement(tablaIngresosAgente);

        return cellIngresosAgente;
    }

    private static PdfPCell getEgresosGerente(
            EgresosGerenteDTO egresosGerenteDTO
    ) throws DocumentException {
        PdfPCell cellIngresosAgente = new PdfPCell();
        cellIngresosAgente.setBorder(0);
        cellIngresosAgente.setPadding(0);
        cellIngresosAgente.setPaddingTop(5f);

        PdfPTable tablaIngresosAgente = new PdfPTable(2);
        tablaIngresosAgente.setWidthPercentage(100);
        tablaIngresosAgente.setWidths(new float[]{1.3f, .7f});

        PdfPCell cellTitulo = new PdfPCell(fuentes.regular("EGRESOS DEL GERENTE (Pagos a agente en CIERRE)",
                11, PdfStyleManager.getWhiteBaseColor()));
        cellTitulo.setColspan(2);
        PdfStyleManager.setStyleForCellTitles(cellTitulo);

        PdfPCell cellPagoComisionCobranza = new PdfPCell(fuentes.regular("PAGO DE COMISION POR COBRANZA:",
                9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellPagoComisionCobranza);

        PdfPCell cellPagoComisionVentas = new PdfPCell(fuentes.regular("PAGO DE COMISION POR VENTAS:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellPagoComisionVentas);

        PdfPCell cellBonos = new PdfPCell(fuentes.regular("BONOS:", 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellBonos);

        PdfPCell cellEfectivoRestanteCierre = new PdfPCell(fuentes.regular("EFECTIVO RESTANTE DE CIERRE: $"
                + egresosGerenteDTO.getEfectivoRestanteCierre(), 9, PdfStyleManager.getPrimaryBasecolor()));
        cellEfectivoRestanteCierre.setColspan(2);
        PdfStyleManager.setStyleFillColorCell(cellEfectivoRestanteCierre);

        PdfPCell cellPagoComisionCobranza2 = new PdfPCell(fuentes.regular("$" + egresosGerenteDTO
                .getPagoComisionCobranza(), 9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellPagoComisionCobranza2);

        PdfPCell cellPagoComisionVentas2 = new PdfPCell(fuentes.regular("$" + egresosGerenteDTO.getPagoComisionVentas(),
                9, PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleFillColorCell(cellPagoComisionVentas2);

        PdfPCell cellBonos2 = new PdfPCell(fuentes.regular("$" + egresosGerenteDTO.getBonos(), 9,
                PdfStyleManager.getPrimaryBasecolor()));
        PdfStyleManager.setStyleNormalCell(cellBonos2);

        tablaIngresosAgente.addCell(cellTitulo);
        tablaIngresosAgente.addCell(cellPagoComisionCobranza);
        tablaIngresosAgente.addCell(cellPagoComisionCobranza2);
        tablaIngresosAgente.addCell(cellPagoComisionVentas);
        tablaIngresosAgente.addCell(cellPagoComisionVentas2);
        tablaIngresosAgente.addCell(cellBonos);
        tablaIngresosAgente.addCell(cellBonos2);
        tablaIngresosAgente.addCell(cellEfectivoRestanteCierre);

        cellIngresosAgente.addElement(tablaIngresosAgente);

        return cellIngresosAgente;
    }
}

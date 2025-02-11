package tech.calaverita.sfast_xpress.controllers;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tech.calaverita.sfast_xpress.models.mariaDB.AgenciaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CalendarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.CierreSemanalConsolidadoV2Model;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;
import tech.calaverita.sfast_xpress.models.mariaDB.VentaModel;
import tech.calaverita.sfast_xpress.models.mariaDB.dynamic.PagoDynamicModel;
import tech.calaverita.sfast_xpress.services.AgenciaService;
import tech.calaverita.sfast_xpress.services.CalendarioService;
import tech.calaverita.sfast_xpress.services.CierreSemanalConsolidadoV2Service;
import tech.calaverita.sfast_xpress.services.ComisionService;
import tech.calaverita.sfast_xpress.services.UsuarioService;
import tech.calaverita.sfast_xpress.services.VentaService;
import tech.calaverita.sfast_xpress.services.dynamic.PagoDynamicService;
import tech.calaverita.sfast_xpress.services.views.PrestamoViewService;
import tech.calaverita.sfast_xpress.utils.BalanceAgenciaUtil;
import tech.calaverita.sfast_xpress.utils.MyUtil;

@CrossOrigin
@RestController
@RequestMapping(path = "/xpress/v1/comisiones")
public class ComisionController {
        private final CalendarioService calendarioService;
        private final AgenciaService agenciaService;
        private final PrestamoViewService prestamoViewService;
        private final PagoDynamicService pagoDynamicService;
        private final VentaService ventaService;
        private final UsuarioService usuarioService;
        private final ComisionService comisionService;
        private final CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service;

        public ComisionController(CalendarioService calendarioService, AgenciaService agenciaService,
                        PrestamoViewService prestamoViewService, PagoDynamicService pagoDynamicService,
                        VentaService ventaService, CierreSemanalConsolidadoV2Service cierreSemanalConsolidadoV2Service,
                        UsuarioService usuarioService, ComisionService comisionService) {
                this.calendarioService = calendarioService;
                this.agenciaService = agenciaService;
                this.prestamoViewService = prestamoViewService;
                this.pagoDynamicService = pagoDynamicService;
                this.ventaService = ventaService;
                this.usuarioService = usuarioService;
                this.comisionService = comisionService;
                this.cierreSemanalConsolidadoV2Service = cierreSemanalConsolidadoV2Service;
        }

        @PostMapping(path = "/create-one")
        public @ResponseBody ResponseEntity<ComisionModel> postCreateOne(@RequestBody ComisionModel comisionModel) {
                ComisionModel responseComisionModel = this.comisionService.save(comisionModel);
                HttpStatus responseHttpStatus = responseComisionModel != null ? HttpStatus.CREATED
                                : HttpStatus.CONFLICT;

                return new ResponseEntity<>(responseComisionModel, responseHttpStatus);
        }

        @GetMapping(path = "/currentWeek/by_agencia/{agencia}")
        public @ResponseBody ResponseEntity<ComisionModel> getCurrentWeek(@PathVariable String agencia) {
                ComisionModel comisionModel = new ComisionModel();

                CalendarioModel calendarioModel = this.calendarioService.findByFechaActual(LocalDate.now().toString());

                // To easy code
                int anio = calendarioModel.getAnio();
                int semana = calendarioModel.getSemana();
                String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "MX"));
                mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
                String fechaJueves = LocalDate.parse(calendarioModel.getDesde()).plusDays(1) + " 23:59:59";

                CompletableFuture<AgenciaModel> agenciaModelCF = this.agenciaService.findByIdAsync(agencia);
                CompletableFuture<UsuarioModel> agenteUsuarioModelCF = this.usuarioService
                                .findByAgenciaTipoAndStatusAsync(agencia, "Agente", true);
                CompletableFuture<Double> debitoTotalCF = this.prestamoViewService
                                .findDebitoTotalByAgenciaSaldoAlIniciarSemanaGreaterThanAndNotAnioAndSemana(agencia, 0D,
                                                anio, semana);
                CompletableFuture<Integer> clientesPagoCompletoCF = this.pagoDynamicService
                                .findClientesPagoCompletoByAgenciaAnioAndSemanaAsync(agencia, anio, semana);
                CompletableFuture<Double> cobranzaPuraHastaJuevesCF = this.pagoDynamicService
                                .findCobranzaPuraByAgenciaAnioSemanaAndFechaPagoLessThanEqual(agencia, anio, semana,
                                                fechaJueves);
                CompletableFuture<ArrayList<PagoDynamicModel>> pagoDynamicModelsCF = this.pagoDynamicService
                                .findByAgenciaAnioSemanaAndEsPrimerPago(agencia, anio, semana, false);
                CompletableFuture<ArrayList<VentaModel>> ventaModelsCF = this.ventaService
                                .findByAgenciaAnioAndSemanaAsync(
                                                agencia,
                                                anio, semana);

                ArrayList<PagoDynamicModel> pagoDynamicModels = pagoDynamicModelsCF.join();

                double cobranzaPura = 0;
                double montoExcedente = 0;
                double cobranzaTotal = 0;
                for (PagoDynamicModel pagoDynamicModel : pagoDynamicModels) {
                        if (pagoDynamicModel.getMonto() < pagoDynamicModel.getTarifa()) {
                                cobranzaPura += pagoDynamicModel.getMonto();
                                cobranzaTotal += pagoDynamicModel.getMonto();
                        } else {
                                cobranzaPura += pagoDynamicModel.getTarifa();
                                cobranzaTotal += pagoDynamicModel.getMonto();

                                if (!pagoDynamicModel.getTipo().equals("Liquidacion")) {
                                        montoExcedente += pagoDynamicModel.getMonto() - pagoDynamicModel.getTarifa();
                                }
                        }
                }

                ArrayList<VentaModel> ventaModels = ventaModelsCF.join();

                double primerosPagos = 0;
                for (VentaModel ventaModel : ventaModels) {
                        primerosPagos += ventaModel.getPrimerPago();
                }

                AgenciaModel agenciaModel = agenciaModelCF.join();
                UsuarioModel agenteUsuarioModel = agenteUsuarioModelCF.join();
                Double debitoTotal = debitoTotalCF.join();
                Integer clientesPagoCompleto = clientesPagoCompletoCF.join();
                Double cobranzaPuraHastaJueves = cobranzaPuraHastaJuevesCF.join();

                comisionModel.setSemana(semana);
                comisionModel.setMes(mes);
                comisionModel.setAnio(anio);
                comisionModel.setAgencia(agencia);
                comisionModel.setGerencia(agenciaModel.getGerenciaId());
                comisionModel.setDebito(MyUtil.getDouble(debitoTotal));
                comisionModel.setNivel(
                                BalanceAgenciaUtil.getNivelAgente(clientesPagoCompleto, cobranzaPuraHastaJueves,
                                                agenteUsuarioModel));
                comisionModel.setCobranzaPura(MyUtil.getDouble(cobranzaPura));
                comisionModel.setRendimiento(MyUtil.getDouble(cobranzaPura / debitoTotal * 100));
                comisionModel.setMontoExcedente(MyUtil.getDouble(montoExcedente));
                comisionModel.setCobranzaTotal(MyUtil.getDouble(cobranzaTotal));
                comisionModel.setPrimerosPagos(MyUtil.getDouble(primerosPagos));
                comisionModel.setPorcentajeComisionCobranza(
                                BalanceAgenciaUtil.getPorcentajeComisionCobranza(comisionModel.getNivel()));
                comisionModel.setPorcentajeBonoMensual(0);
                comisionModel
                                .setComisionCobranza(MyUtil.getDouble(
                                                (cobranzaTotal + primerosPagos) / 100
                                                                * comisionModel.getPorcentajeComisionCobranza()));
                comisionModel.setComisionVentas(ventaModels.size() * 100D);
                comisionModel.setBonos(0D);

                calcularPagoDeBonos(comisionModel, calendarioModel, agenteUsuarioModel);

                return new ResponseEntity<>(comisionModel, HttpStatus.OK);
        }

        public void calcularPagoDeBonos(ComisionModel comisionModel,
                        CalendarioModel semanaActualCalendarioModel,
                        UsuarioModel usuarioModel) {
                if (semanaActualCalendarioModel.isPagoBono()) {
                        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                                        "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
                        int indiceMes = 0;
                        for (int i = 0; i < meses.length; i++) {
                                if (semanaActualCalendarioModel.getMes().equals(meses[i])) {
                                        indiceMes = i - 1;
                                        break;
                                }
                        }

                        // To easy code
                        int anio = semanaActualCalendarioModel.getAnio();

                        ArrayList<CalendarioModel> semanasDelMes = this.calendarioService
                                        .findByAnioAndMesAsync(indiceMes < 0 ? anio - 1 : anio,
                                                        meses[indiceMes < 0 ? 11 : indiceMes])
                                        .join();

                        CierreSemanalConsolidadoV2Model cierreSemanalConsolidadoV2Model;
                        Double cobranzaTotal = 0D;
                        Double rendimiento = 0D;
                        Integer clientesPagoCompleto = 0;
                        for (int i = 0; i < semanasDelMes.size(); i++) {
                                cierreSemanalConsolidadoV2Model = this.cierreSemanalConsolidadoV2Service
                                                .findByAgenciaAnioAndSemana(comisionModel.getAgencia(),
                                                                semanasDelMes.get(i).getAnio(),
                                                                semanasDelMes.get(i).getSemana());
                                ComisionModel anteriorComisionModel = this.comisionService.findByAgenciaAnioAndSemana(
                                                comisionModel.getAgencia(), semanasDelMes.get(i).getAnio(),
                                                semanasDelMes.get(i).getSemana());

                                if (cierreSemanalConsolidadoV2Model != null && anteriorComisionModel != null) {
                                        cobranzaTotal += anteriorComisionModel.getCobranzaTotal()
                                                        + anteriorComisionModel.getPrimerosPagos()
                                                        - cierreSemanalConsolidadoV2Model.getLiquidaciones();

                                        rendimiento += anteriorComisionModel.getRendimiento();

                                        clientesPagoCompleto += cierreSemanalConsolidadoV2Model.getClientes()
                                                        - cierreSemanalConsolidadoV2Model.getPagosReducidos()
                                                        - cierreSemanalConsolidadoV2Model.getNoPagos();
                                }
                        }

                        rendimiento = rendimiento / semanasDelMes.size();
                        clientesPagoCompleto = clientesPagoCompleto / semanasDelMes.size();
                        cobranzaTotal = MyUtil.getDouble(cobranzaTotal);

                        comisionModel.setPromedioClientesPagoCompleto(clientesPagoCompleto);
                        comisionModel.setPorcentajeBonoMensual(
                                        BalanceAgenciaUtil.getPorcentajeBonoMensual(clientesPagoCompleto, rendimiento,
                                                        usuarioModel));
                        comisionModel.setBonos(MyUtil.getDouble(
                                        cobranzaTotal / 100 * comisionModel.getPorcentajeBonoMensual()));
                }
        }
}

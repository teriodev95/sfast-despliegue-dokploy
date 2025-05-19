package tech.calaverita.sfast_xpress.cierre_agencia.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Data;
import tech.calaverita.sfast_xpress.cierre_agencia.CierreAgenciaModel;
import tech.calaverita.sfast_xpress.dashboard_agencia.DashboardAgenciaDto;
import tech.calaverita.sfast_xpress.models.mariaDB.ComisionModel;
import tech.calaverita.sfast_xpress.models.mariaDB.UsuarioModel;

@Data
public class BalanceAgencia {
	private String zona;
	private String gerente;
	private String agencia;
	private String agente;
	private Double rendimiento;
	private String nivel;
	private String nivelCalculado;
	private Integer clientes;
	private Integer pagosReducidos;
	private Integer noPagos;
	private Integer liquidaciones;

	public BalanceAgencia() {
		this.zona = "";
		this.gerente = "";
		this.agencia = "";
		this.agente = "";
		this.rendimiento = 0.0;
		this.nivel = "";
		this.nivelCalculado = "";
		this.clientes = 0;
		this.pagosReducidos = 0;
		this.noPagos = 0;
		this.liquidaciones = 0;
	}

	public BalanceAgencia(CierreAgenciaModel cierreAgenciaModel, ComisionModel comisionModel) {
		this.setAgente(cierreAgenciaModel.getAgente());
		this.setGerente(cierreAgenciaModel.getGerente());
		this.setZona(cierreAgenciaModel.getGerencia());
		this.setAgencia(cierreAgenciaModel.getAgencia());
		if (comisionModel != null) {
			this.setRendimiento(comisionModel.getRendimiento());
			this.setNivelCalculado(comisionModel.getNivel());
		}
		this.setClientes(cierreAgenciaModel.getClientes());
		this.setPagosReducidos(cierreAgenciaModel.getPagosReducidos());
		this.setNoPagos(cierreAgenciaModel.getNoPagos());
		this.setLiquidaciones(cierreAgenciaModel.getClientesLiquidados());
	}

	public BalanceAgencia(DashboardAgenciaDto dashboardAgenciaDto, List<UsuarioModel> usuarioModels) {
		this();

		// To easy code
		UsuarioModel agenteUsuarioModel = usuarioModels.get(0);
		UsuarioModel gerenteUsuarioModel = usuarioModels.get(1);
		String nombreAgente = agenteUsuarioModel.getNombre() + " " + agenteUsuarioModel.getApellidoPaterno()
				+ " " + agenteUsuarioModel.getApellidoMaterno();
		String nombreGerente = gerenteUsuarioModel.getNombre() + " " + gerenteUsuarioModel.getApellidoPaterno()
				+ " " + gerenteUsuarioModel.getApellidoMaterno();

		this.zona = dashboardAgenciaDto.getGerencia();
		this.gerente = nombreGerente;
		this.agencia = dashboardAgenciaDto.getAgencia();
		this.agente = nombreAgente;
		this.rendimiento = dashboardAgenciaDto.getRendimiento();

		this.clientes = dashboardAgenciaDto.getClientes();
		this.pagosReducidos = dashboardAgenciaDto.getPagosReducidos();
		this.noPagos = dashboardAgenciaDto.getNoPagos();
		this.liquidaciones = dashboardAgenciaDto.getNumeroLiquidaciones();
	}

	private String getNivelAgente(int clientesPagoCompleto, double rendimientoAlJueves,
			String fechaIngresoAgente) {
		String nivel;
		int antiguedadEnSemanas = getAntiguedadAgenteEnSemanas(fechaIngresoAgente);

		if (antiguedadEnSemanas >= 52 && clientesPagoCompleto >= 80 && rendimientoAlJueves > .9) {
			nivel = "DIAMOND";
		} else if (antiguedadEnSemanas >= 26 && clientesPagoCompleto >= 50 && rendimientoAlJueves > .8) {
			nivel = "PLATINUM";
		} else if (antiguedadEnSemanas >= 13 && clientesPagoCompleto >= 30 && rendimientoAlJueves > .7) {
			nivel = "GOLD";
		} else {
			nivel = "SILVER";
		}

		return nivel;
	}

	private int getAntiguedadAgenteEnSemanas(String fechaIngreso) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaIngresoAgente;

		try {
			fechaIngresoAgente = format.parse(fechaIngreso);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		// 24 hr/dia x 60 min/hr x 60 seg/min x 1000 ms/seg
		return (int) ((new Date().getTime() - fechaIngresoAgente.getTime()) / (1000 * 60 * 60 * 24 * 7));
	}

	public BalanceAgencia nivel(int clientesPagoCompleto, double rendimientoAlJueves, String fechaIngresoAgente) {
		this.nivel = getNivelAgente(clientesPagoCompleto, rendimientoAlJueves, fechaIngresoAgente);
		return this;
	}

	public BalanceAgencia nivelCalculado(int clientesPagoCompleto, double rendimientoAlJueves,
			String fechaIngresoAgente) {
		this.nivelCalculado = getNivelAgente(clientesPagoCompleto, rendimientoAlJueves, fechaIngresoAgente);
		return this;
	}
}

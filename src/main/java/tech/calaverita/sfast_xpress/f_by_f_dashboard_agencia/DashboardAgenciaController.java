package tech.calaverita.sfast_xpress.f_by_f_dashboard_agencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;

@CrossOrigin
@RestController
@RequestMapping("/api/v2/dashboards-agencias")
public final class DashboardAgenciaController {
	private final DashboardAgenciaService dashboardAgenciaService;

	public DashboardAgenciaController(DashboardAgenciaService dashboardAgenciaService) {
		this.dashboardAgenciaService = dashboardAgenciaService;
	}

	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Version", Constants.VERSION);
		response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
	}

	@GetMapping("/agencia/{agencia}/anio/{anio}/semana/{semana}")
	public ResponseEntity<?> getByAgenciaAnioSemana(@PathVariable String agencia, @PathVariable int anio,
			@PathVariable int semana) {
		return this.dashboardAgenciaService.getByAgenciaAnioSemana(agencia, anio, semana);
	}
}
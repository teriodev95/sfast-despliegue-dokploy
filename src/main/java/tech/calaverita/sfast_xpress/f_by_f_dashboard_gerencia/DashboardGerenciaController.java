package tech.calaverita.sfast_xpress.f_by_f_dashboard_gerencia;

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
@RestController()
@RequestMapping(path = "/api/v2/dashboards-gerencias")
public final class DashboardGerenciaController {
	private final DashboardGerenciaService dashboardGerenciaService;

	public DashboardGerenciaController(DashboardGerenciaService dashboardGerenciaService) {
		this.dashboardGerenciaService = dashboardGerenciaService;
	}

	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Version", Constants.VERSION);
		response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
	}

	@GetMapping("/gerencia/{gerencia}/anio/{anio}/semana/{semana}")
	public ResponseEntity<?> getDashboardByGerencia(@PathVariable String gerencia, @PathVariable int anio,
			@PathVariable int semana) {
		return this.dashboardGerenciaService.getByGerenciaAnioSemana(gerencia, anio, semana);
	}
}
package tech.calaverita.sfast_xpress.f_by_f_cierre_agencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import tech.calaverita.sfast_xpress.Constants;

@CrossOrigin
@RestController
@RequestMapping("/api/v2/cierres-agencias")
public final class CierreAgenciaController {
	private final CierreAgenciaService cierreAgenciaService;

	public CierreAgenciaController(CierreAgenciaService cierreAgenciaService) {
		this.cierreAgenciaService = cierreAgenciaService;
	}

	@ModelAttribute
	public void setResponseHeader(HttpServletResponse response) {
		response.setHeader("Version", Constants.VERSION);
		response.setHeader("Last-Modified", Constants.LAST_MODIFIED);
	}

	@GetMapping("/agencia/{agencia}/current")
	public @ResponseBody ResponseEntity<?> getCurrentByAgencia(
			@RequestHeader(required = false) String staticToken, @RequestHeader String username,
			@PathVariable String agencia) {
		return this.cierreAgenciaService.getCurrentByUsuarioAgencia(username, agencia);
	}

	@GetMapping("/agencia/{agencia}/anio/{anio}/semana/{semana}")
	public @ResponseBody ResponseEntity<?> getByAgenciaAnioSemana(
			@RequestHeader(required = false) String staticToken, @RequestHeader String username,
			@PathVariable String agencia, @PathVariable int anio, @PathVariable int semana) {
		return this.cierreAgenciaService.getByUsuarioAgenciaAnioSemana(username, agencia, anio, semana);
	}

	@GetMapping("/comision-cobranza/agencia/{agencia}/anio/{anio}/semana/{semana}/nivel/{nivel}")
	public @ResponseBody ResponseEntity<?> getComisionCobranzaByAgenciaAnioSemanaNivel(
			@PathVariable String agencia, @PathVariable int anio, @PathVariable int semana,
			@PathVariable String nivel) {
		return this.cierreAgenciaService.getComisionCobranzaByAgenciaAnioSemanaNivel(agencia, anio, semana, nivel);
	}

	@PostMapping("/create-one")
	public @ResponseBody ResponseEntity<?> createOne(@RequestBody CierreAgenciaModel cierreAgenciaModel,
			@RequestHeader(required = false) String staticToken, @RequestHeader String username) {
		return this.cierreAgenciaService.create(cierreAgenciaModel, username);
	}
}

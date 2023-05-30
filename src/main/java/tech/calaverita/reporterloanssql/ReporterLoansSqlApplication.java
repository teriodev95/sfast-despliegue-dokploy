package tech.calaverita.reporterloanssql;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ReporterLoansSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReporterLoansSqlApplication.class, args);
	}
}

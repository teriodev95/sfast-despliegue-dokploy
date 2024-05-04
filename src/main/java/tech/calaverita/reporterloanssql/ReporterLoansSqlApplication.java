package tech.calaverita.reporterloanssql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tech.calaverita.reporterloanssql.controllers.ReporteController;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableCaching
public class ReporterLoansSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReporterLoansSqlApplication.class, args);
    }
}

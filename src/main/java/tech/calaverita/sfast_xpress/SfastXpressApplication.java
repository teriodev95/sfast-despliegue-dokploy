package tech.calaverita.sfast_xpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SfastXpressApplication {

    public static void main(String[] args) {
        SpringApplication.run(SfastXpressApplication.class, args);
    }
}

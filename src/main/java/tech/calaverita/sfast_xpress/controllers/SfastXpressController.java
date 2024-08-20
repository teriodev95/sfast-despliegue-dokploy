package tech.calaverita.sfast_xpress.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(path = "/")
public class SfastXpressController {
    @GetMapping(path = "/healthcheck")
    public ResponseEntity<String> getHealthCheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

package tech.calaverita.sfast_xpress.controllers.PGS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin
@RestController
@RequestMapping("/xpress/v1/pwa/hola/mundo")
public class PruebaEndpoints {

    @GetMapping("/getHola")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<>("Hola mundo", HttpStatus.OK);
    }

    @PostMapping("/postHola")
    public ResponseEntity<String> postMethodName(@RequestBody String string) {
        if (string.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        String respuesta = string;
        
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    
    
}

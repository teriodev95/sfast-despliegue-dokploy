package tech.calaverita.reporterloanssql.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
public class FileManagerService {
    public ResponseEntity<byte[]> getPdf(String uri, String fileName) {
        File file = new File(uri); // change to relative path
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
//        String fileName = "output.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = null;
        try {
            response = new org.springframework.http.ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}

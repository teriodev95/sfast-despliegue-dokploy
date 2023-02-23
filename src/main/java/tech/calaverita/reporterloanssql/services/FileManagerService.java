package tech.calaverita.reporterloanssql.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileManagerService {
    public ResponseEntity<byte[]> getPdf(String uri, String fileName){
        byte[] targetArray = new byte[0];

        try {
            FileInputStream fis= new FileInputStream(new File(uri));
            targetArray = new byte[fis.available()];
            fis.read(targetArray);


        }catch (IOException e){

        }

        byte[] contents = (targetArray);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);

        return response;
    }
}

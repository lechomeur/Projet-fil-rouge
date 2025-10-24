package com.omadi.projetfilrouge_v2.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("swagger")
@CrossOrigin("*")
public class SwaggerController {
    @GetMapping("/swagger.yaml")
    public ResponseEntity<ClassPathResource> getSwaggerFile() {
        ClassPathResource resource = new ClassPathResource("openapi.yml");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/yaml")
                .body(resource);
    }
}

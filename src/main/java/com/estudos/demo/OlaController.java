package com.estudos.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OlaController {
    @GetMapping("/")
    public String ola() {
        return "Olá! Seu primeiro projeto Spring Boot está funcionando.";
    }
}

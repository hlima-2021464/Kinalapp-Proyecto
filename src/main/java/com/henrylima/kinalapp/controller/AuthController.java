package com.henrylima.kinalapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth")
    public String auth() {
        return "Login correcto";
    }
}
//DetalleVentaController (controller)
//CustomUserDetailsService (security)
//DetaleVentaService (service)
//IDetalleVentaService (service)

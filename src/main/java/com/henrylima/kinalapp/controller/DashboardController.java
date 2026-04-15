package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuarioLogueado);
        return "dashboard";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
}
package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IUsuarioService;
import com.henrylima.kinalapp.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.listarUsuarios()
                .stream()
                .filter(u -> u.getUsername().equals(username) &&
                        u.getPassword().equals(password))
                .findFirst();

        if (usuarioOpt.isPresent() && usuarioOpt.get().getEstado() == 1) {
            session.setAttribute("usuarioLogueado", usuarioOpt.get());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Credenciales inválidas o usuario inactivo");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
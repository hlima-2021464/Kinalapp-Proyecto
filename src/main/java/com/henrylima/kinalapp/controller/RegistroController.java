package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IUsuarioService;
import com.henrylima.kinalapp.entity.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final IUsuarioService usuarioService;

    public RegistroController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute("usuario") Usuario usuario,
                            RedirectAttributes redirect) {
        try {
            // Por defecto, nuevos usuarios registrados tendrán rol USER
            usuario.setRol("USER");
            usuario.setEstado(1); // Activo por defecto

            usuarioService.guardar(usuario);
            redirect.addFlashAttribute("mensaje", "✅ ¡Registro exitoso! Ahora puedes iniciar sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/registro";
        }
    }
}
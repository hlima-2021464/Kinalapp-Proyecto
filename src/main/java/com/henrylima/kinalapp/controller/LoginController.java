package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IUsuarioService;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    public LoginController(IUsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
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
        
        // Buscar por username exacto
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Verificar password (en producción usar BCrypt)
            if (usuario.getPassword().equals(password)) {
                
                // Verificar estado
                if (usuario.getEstado() == 1) {
                    session.setAttribute("usuarioLogueado", usuario);
                    return "redirect:/dashboard";
                } else {
                    model.addAttribute("error", "⚠️ Usuario inactivo. Contacte al administrador.");
                }
            } else {
                model.addAttribute("error", "❌ Contraseña incorrecta");
            }
        } else {
            model.addAttribute("error", "❌ Usuario no encontrado");
        }
        
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}

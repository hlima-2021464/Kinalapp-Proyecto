package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.service.IUsuarioService;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    private final IUsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioWebController(IUsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "listarUsuarios";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("usuarioForm", new Usuario());
        return "formUsuarios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuarioForm") Usuario usuarioForm,
                          RedirectAttributes redirect) {
        try {
            usuarioService.guardar(usuarioForm);
            redirect.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditar(@PathVariable Long codigo,
                                   Model model,
                                   Authentication authentication,
                                   RedirectAttributes redirect) {
        cargarUsuario(authentication, model);

        return usuarioService.buscarPorCodigo(codigo)
                .map(u -> {
                    model.addAttribute("usuarioForm", u);
                    return "formUsuarios";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/web/usuarios";
                });
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Long codigo,
                           RedirectAttributes redirect) {
        try {
            usuarioService.eliminar(codigo);
            redirect.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    private void cargarUsuario(Authentication authentication, Model model) {
        if (authentication != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
}

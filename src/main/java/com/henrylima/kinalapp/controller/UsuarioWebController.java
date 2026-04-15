package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IUsuarioService;
import com.henrylima.kinalapp.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/usuarios")
public class UsuarioWebController {

    private final IUsuarioService usuarioService;

    public UsuarioWebController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private boolean sesionValida(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuarios/listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioForm", new Usuario());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuarioForm") Usuario usuarioForm,
                          RedirectAttributes redirect,
                          HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

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
                                   HttpSession session,
                                   RedirectAttributes redirect) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);

        return usuarioService.buscarPorCodigo(codigo)
                .map(u -> {
                    model.addAttribute("usuarioForm", u);
                    return "usuarios/form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Usuario no encontrado");
                    return "redirect:/web/usuarios";
                });
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Long codigo,
                           RedirectAttributes redirect,
                           HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        try {
            usuarioService.eliminar(codigo);
            redirect.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }
}
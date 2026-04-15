package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IClienteService;
import com.henrylima.kinalapp.entity.Cliente;
import com.henrylima.kinalapp.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    private final IClienteService clienteService;

    public ClienteWebController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    private boolean sesionValida(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente,
                          RedirectAttributes redirect,
                          HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        try {
            clienteService.guardar(cliente);
            redirect.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/clientes";
    }

    @GetMapping("/editar/{dpi}")
    public String formularioEditar(@PathVariable String dpi,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirect) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);

        return clienteService.buscarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    return "clientes/form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/web/clientes";
                });
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi,
                           RedirectAttributes redirect,
                           HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        try {
            clienteService.eliminar(dpi);
            redirect.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/clientes";
    }
}
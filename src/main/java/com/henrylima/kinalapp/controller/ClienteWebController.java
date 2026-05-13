package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.service.IClienteService;
import com.henrylima.kinalapp.entity.Cliente;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/clientes")
public class ClienteWebController {

    private final IClienteService clienteService;
    private final UsuarioRepository usuarioRepository;

    public ClienteWebController(IClienteService clienteService, UsuarioRepository usuarioRepository) {
        this.clienteService = clienteService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("clientes", clienteService.listarClientes());
        return "listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("cliente", new Cliente());
        return "form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente,
                          RedirectAttributes redirect) {
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
                                   Authentication authentication,
                                   RedirectAttributes redirect) {
        cargarUsuario(authentication, model);

        return clienteService.buscarPorDPI(dpi)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    return "form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/web/clientes";
                });
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi,
                           RedirectAttributes redirect) {
        try {
            clienteService.eliminar(dpi);
            redirect.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/clientes";
    }

    private void cargarUsuario(Authentication authentication, Model model) {
        if (authentication != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
}

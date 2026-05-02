package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.service.IVentaService;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final IVentaService ventaService;
    private final UsuarioRepository usuarioRepository;

    public VentaWebController(IVentaService ventaService, UsuarioRepository usuarioRepository) {
        this.ventaService = ventaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("ventas", ventaService.listarVentas());
        return "listarVentas";
    }

    @GetMapping("/ver/{codigo}")
    public String verDetalle(@PathVariable Long codigo,
                             Model model,
                             Authentication authentication,
                             RedirectAttributes redirect) {
        cargarUsuario(authentication, model);

        return ventaService.buscarPorCodigo(codigo)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    return "detalleVentas";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/web/ventas";
                });
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminarBloqueado(@PathVariable Long codigo,
                                    RedirectAttributes redirect) {
        redirect.addFlashAttribute("error", "⛔ BOTÓN NO DISPONIBLE - Las ventas no se pueden eliminar");
        return "redirect:/web/ventas";
    }

    private void cargarUsuario(Authentication authentication, Model model) {
        if (authentication != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
}

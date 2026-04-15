package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.service.IVentaService;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.entity.Venta;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final IVentaService ventaService;

    public VentaWebController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    private boolean sesionValida(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("ventas", ventaService.listarVentas());
        return "ventas/listar";
    }

    @GetMapping("/ver/{codigo}")
    public String verDetalle(@PathVariable Long codigo,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirect) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);

        return ventaService.buscarPorCodigo(codigo)
                .map(venta -> {
                    model.addAttribute("venta", venta);
                    return "ventas/detalle";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Venta no encontrada");
                    return "redirect:/web/ventas";
                });
    }

    // NO SE PERMITE EDITAR NI ELIMINAR VENTAS
    @GetMapping("/eliminar/{codigo}")
    public String eliminarBloqueado(@PathVariable Long codigo,
                                    RedirectAttributes redirect,
                                    HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        redirect.addFlashAttribute("error", " BOTÓN NO DISPONIBLE - Las ventas no se pueden eliminar");
        return "redirect:/web/ventas";
    }
}

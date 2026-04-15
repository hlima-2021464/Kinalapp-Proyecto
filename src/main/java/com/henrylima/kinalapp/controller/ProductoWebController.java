package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.Service.IProductoService;
import com.henrylima.kinalapp.entity.Producto;
import com.henrylima.kinalapp.entity.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/productos")
public class ProductoWebController {

    private final IProductoService productoService;

    public ProductoWebController(IProductoService productoService) {
        this.productoService = productoService;
    }

    private boolean sesionValida(HttpSession session) {
        return session.getAttribute("usuarioLogueado") != null;
    }

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("productos", productoService.listarProductos());
        return "productos/listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
                          RedirectAttributes redirect,
                          HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        try {
            productoService.guardar(producto);
            redirect.addFlashAttribute("mensaje", "Producto guardado exitosamente");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/productos";
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditar(@PathVariable Long codigo,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirect) {
        if (!sesionValida(session)) return "redirect:/login";

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);

        return productoService.buscarPorCodigo(codigo)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    return "productos/form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Producto no encontrado");
                    return "redirect:/web/productos";
                });
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Long codigo,
                           RedirectAttributes redirect,
                           HttpSession session) {
        if (!sesionValida(session)) return "redirect:/login";

        try {
            productoService.eliminar(codigo);
            redirect.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/productos";
    }
}
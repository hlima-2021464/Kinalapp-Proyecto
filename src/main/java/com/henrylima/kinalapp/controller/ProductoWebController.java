package com.henrylima.kinalapp.controller;

import com.henrylima.kinalapp.service.IProductoService;
import com.henrylima.kinalapp.entity.Producto;
import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/productos")
public class ProductoWebController {

    private final IProductoService productoService;
    private final UsuarioRepository usuarioRepository;

    public ProductoWebController(IProductoService productoService, UsuarioRepository usuarioRepository) {
        this.productoService = productoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("productos", productoService.listarProductos());
        return "listarProductos";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model, Authentication authentication) {
        cargarUsuario(authentication, model);
        model.addAttribute("producto", new Producto());
        return "formProductos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto,
                          RedirectAttributes redirect) {
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
                                   Authentication authentication,
                                   RedirectAttributes redirect) {
        cargarUsuario(authentication, model);

        return productoService.buscarPorCodigo(codigo)
                .map(producto -> {
                    model.addAttribute("producto", producto);
                    return "formProductos";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("error", "Producto no encontrado");
                    return "redirect:/web/productos";
                });
    }

    @GetMapping("/eliminar/{codigo}")
    public String eliminar(@PathVariable Long codigo,
                           RedirectAttributes redirect) {
        try {
            productoService.eliminar(codigo);
            redirect.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/web/productos";
    }

    private void cargarUsuario(Authentication authentication, Model model) {
        if (authentication != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            usuarioOpt.ifPresent(usuario -> model.addAttribute("usuario", usuario));
        }
    }
}

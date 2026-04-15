package com.henrylima.kinalapp.Service;

import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    public List<Usuario> listarPorEstado(int estado) {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getEstado() == estado)
                .toList();
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        // Validaciones básicas
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (usuario.getUsername().length() < 3) {
            throw new IllegalArgumentException("El username debe tener al menos 3 caracteres");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (usuario.getPassword().length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        // Validar username único (excluyendo el usuario actual si es edición)
        boolean usernameExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(usuario.getUsername())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (usernameExiste) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        // Validar email único (excluyendo el usuario actual si es edición)
        boolean emailExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(usuario.getEmail())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (emailExiste) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Asignar valores por defecto si no vienen
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("USER");
        }

        if (usuario.getEstado() == 0) {
            usuario.setEstado(1);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código: " + codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    public boolean existePorCodigo(Long codigo) {
        return usuarioRepository.existsById(codigo);
    }

    @Override
    public Usuario actualizar(Long codigo, Usuario usuario) {

        Usuario usuarioExistente = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Si no se envía password, mantener el existente
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            usuario.setPassword(usuarioExistente.getPassword());
        }

        usuario.setCodigoUsuario(codigo);
        
        return guardar(usuario);
    }
}

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

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        boolean usernameExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(usuario.getUsername())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (usernameExiste) {
            throw new IllegalArgumentException("El username ya existe");
        }

        boolean emailExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(usuario.getEmail())
                        && u.getCodigoUsuario() != usuario.getCodigoUsuario());

        if (emailExiste) {
            throw new IllegalArgumentException("El email ya existe");
        }

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

        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setPassword(usuario.getPassword());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setRol(usuario.getRol());
        usuarioExistente.setEstado(usuario.getEstado());

        return guardar(usuarioExistente);
    }
}
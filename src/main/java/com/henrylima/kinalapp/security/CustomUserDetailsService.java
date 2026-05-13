package com.henrylima.kinalapp.security;

import com.henrylima.kinalapp.entity.Usuario;
import com.henrylima.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        return new CustomUserDetails(usuarioOpt.get());
    }
}

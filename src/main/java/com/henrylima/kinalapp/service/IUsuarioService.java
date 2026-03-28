package com.henrylima.kinalapp.Service;

import com.henrylima.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarUsuarios();

    Optional<Usuario> buscarPorCodigo(Long codigo);

    List<Usuario> listarPorEstado(int estado);

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Long codigo, Usuario usuario);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);
}
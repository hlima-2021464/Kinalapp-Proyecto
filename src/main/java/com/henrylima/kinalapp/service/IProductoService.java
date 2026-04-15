package com.henrylima.kinalapp.Service;

import com.henrylima.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<Producto> listarProductos();

    List<Producto> listarPorEstado(int estado);

    List<Producto> listarConStock();

    List<Producto> listarSinStock();

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorCodigo(Long codigoProducto);

    Producto actualizar(Long codigoProducto, Producto producto);

    void eliminar(Long codigoProducto);

    boolean existePorCodigo(Long codigoProducto);
}
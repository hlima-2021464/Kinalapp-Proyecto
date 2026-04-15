package com.henrylima.kinalapp.Service;

import com.henrylima.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    List<Venta> listarVentas();

    Optional<Venta> buscarPorCodigo(Long codigoVenta);

    List<Venta> listarPorEstado(int estado);

    Venta guardar(Venta venta);

    Venta actualizar(Long codigoVenta, Venta venta);

    void eliminar(Long codigoVenta);

    boolean existePorCodigo(Long codigoVenta);
}
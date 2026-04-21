package com.henrylima.kinalapp.service;

import com.henrylima.kinalapp.entity.DetalleVenta;

import java.util.List;

public interface IDetalleVentaService {

    List<DetalleVenta> listarTodos();

    DetalleVenta guardar(DetalleVenta detalleVenta);

    DetalleVenta buscarPorId(Long id);

    void eliminar(Long id);
}

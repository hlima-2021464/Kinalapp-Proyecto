package com.henrylima.kinalapp.service;

import com.henrylima.kinalapp.entity.DetalleVenta;
import com.henrylima.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.service;

import java.util.List;

@Service
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public DetalleVenta buscarPorId(Long id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        detalleVentaRepository.deleteById(id);
    }
}

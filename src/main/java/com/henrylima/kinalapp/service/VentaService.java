package com.henrylima.kinalapp.Service;

import com.henrylima.kinalapp.entity.Venta;
import com.henrylima.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(Long codigoVenta) {
        return ventaRepository.findById(codigoVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarPorEstado(int estado) {
        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getEstado() == estado)
                .toList();
    }

    @Override
    public Venta guardar(Venta venta) {

        validarVenta(venta);

        if (venta.getEstado() == null || venta.getEstado() == 0) {
            venta.setEstado(1);
        }

        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizar(Long codigoVenta, Venta venta) {

        if (!ventaRepository.existsById(codigoVenta)) {
            throw new RuntimeException("Venta no encontrada");
        }

        venta.setCodigoVenta(codigoVenta);

        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigoVenta) {

        if (!ventaRepository.existsById(codigoVenta)) {
            throw new RuntimeException("Venta no encontrada");
        }

        ventaRepository.deleteById(codigoVenta);
    }

    @Override
    public boolean existePorCodigo(Long codigoVenta) {
        return ventaRepository.existsById(codigoVenta);
    }

    private void validarVenta(Venta venta) {

        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("Fecha obligatoria");
        }

        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total inválido");
        }

        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("Cliente obligatorio");
        }

        if (venta.getUsuario() == null) {
            throw new IllegalArgumentException("Usuario obligatorio");
        }
    }
}
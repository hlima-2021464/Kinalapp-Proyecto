package com.henrylima.kinalapp.service;

import com.henrylima.kinalapp.Service.IClienteService;
import com.henrylima.kinalapp.entity.Cliente;
import com.henrylima.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente guardar(Cliente cliente) {

        validarCliente(cliente);

        if (cliente.getEstado() == 0) {
            cliente.setEstado(1);
        }

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findByDpiCliente(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {

        if (!clienteRepository.existsByDpiCliente(dpi)) {
            throw new RuntimeException("El cliente no se encontró por el DPI: " + dpi);
        }

        cliente.setDpiCliente(dpi);

        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {

        if (!clienteRepository.existsByDpiCliente(dpi)) {
            throw new RuntimeException("El cliente no se encontró con el DPI: " + dpi);
        }

        clienteRepository.deleteByDpiCliente(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsByDpiCliente(dpi);
    }

    private void validarCliente(Cliente cliente) {

        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI es obligatorio");
        }

        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEstado(int estado) {
        return listarClientes().stream()
                .filter(c -> c.getEstado() == estado)
                .toList();
    }
}
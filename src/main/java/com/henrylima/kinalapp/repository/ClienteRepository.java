package com.henrylima.kinalapp.repository;

import com.henrylima.kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDpiCliente(String dpi);

    boolean existsByDpiCliente(String dpi);

    void deleteByDpiCliente(String dpi);
}

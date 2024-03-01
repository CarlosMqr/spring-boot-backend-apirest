package com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.dao;

import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente,Long> {
}

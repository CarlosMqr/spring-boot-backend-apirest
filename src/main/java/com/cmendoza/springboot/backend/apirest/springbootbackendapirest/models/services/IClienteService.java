package com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.services;

import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    Cliente findById(Long id);//buscar por id
    List<Cliente> findAll();//actualizar
    Page<Cliente> findAll(Pageable pageable);
    Cliente save(Cliente cliente);//guardar
     void  delete(Long id);//eliminar

}

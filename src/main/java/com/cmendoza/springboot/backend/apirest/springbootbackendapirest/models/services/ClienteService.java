package com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.services;

import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.Cliente;
import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.dao.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service//componente de servicio en Spring
public class ClienteService implements IClienteService{
    @Autowired
    private IClienteDao clienteDao;


    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();//clienteDao.findAll(); retorna un iterable y se tiene que hacer
                                                    //cast (List<Cliente>)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        System.out.println(cliente.getCratAt());
        return clienteDao.save(cliente) ;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);

    }
}

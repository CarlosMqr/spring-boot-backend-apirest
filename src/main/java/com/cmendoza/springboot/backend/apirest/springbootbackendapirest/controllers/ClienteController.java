package com.cmendoza.springboot.backend.apirest.springbootbackendapirest.controllers;
import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.Cliente;
import com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models.services.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = {"http://localhost:4200"})// para poder enviar y recibir datos
@RestController// indicar que es una clase restcontroller
@RequestMapping("/api")// aqui se genera la url

public class ClienteController {// estes es nuestro controlador mapeamos todos los metodos del rest
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes") // mapear la url para generar el empoid
    public List<Cliente> index(){//listar
        return clienteService.findAll();
    }


    @GetMapping("/clientes/page/{page}") // mapear la url para generar el empoid
    public Page<Cliente> index(@PathVariable Integer page){//listar
        return clienteService.findAll(PageRequest.of(page,4));
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){// mostrar
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = null;
        try {
            cliente = clienteService.findById(id);
        }catch (DataAccessException e){
            response.put("Mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getCause().getMessage());
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);

        }
        if (cliente == null){
            response.put("Mensaje", "el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
            return new ResponseEntity<>(response,NOT_FOUND);
        }
        return new ResponseEntity<>(cliente, OK);

    }

    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result){//crear @Valid para validar

        Cliente cl = null;
        Map<String,Object> response = new HashMap<>();

        if (result.hasErrors()){

            /*List<String> errors = new ArrayList<>();

            for(FieldError err : result.getFieldErrors()){
                errors.add("El campo  " + err.getField() + "' " + err.getDefaultMessage());
            }*/ // forma 1 de manejar los errores

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo  " + err.getField() + "' " + err.getDefaultMessage() )
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response,BAD_REQUEST);
        }// forma de manejar los errores atarvez de la apiLits de un arrayList

        try {
            System.out.println(cliente.getCratAt());
            cl = clienteService.save(cliente);

        }catch (DataAccessException e){
            response.put("Mensaje", "Error al realizar el INSERT en la BD");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);

        }
        response.put("Mensaje"," El cliente se guardo con éxito!");
        response.put("cliente",cl);
        return new ResponseEntity<>(response,CREATED);


    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> Update(@Valid @RequestBody Cliente cliente, BindingResult result , @PathVariable Long id){// para actualizar un cliente
        Cliente cliente1 = clienteService.findById(cliente.getId());
        Cliente clUp = null;

        Map<String,Object> response = new HashMap<>();

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo  " + err.getField() + "' " + err.getDefaultMessage() )
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response,BAD_REQUEST);
        }// forma de manejar los errores atarvez de la apiLits de un arrayList

        if (cliente1 == null){
            response.put("Mensaje", "Error:, no se puede editar, el cliente con ID: ".concat(id.toString().concat(" no existe en la BD")));
            return new ResponseEntity<>(response,NOT_FOUND);
        }

        try {
            cliente1.setApellido(cliente.getApellido());// se envia al clienteActual el nuevo appelido pero muestra el anterior
            cliente1.setNombre(cliente.getNombre());// se envia al clienteActual el nuevo nombre pero muestra el anterior
            cliente1.setEmail(cliente.getEmail());// se envia al clienteActual el nuevo email
            cliente1.setCratAt(cliente.getCratAt());

            clUp = clienteService.save(cliente1);

        }catch (DataAccessException e){
            response.put("Mensaje", "Error al realizar el update en la BD");
            response.put("Error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);

        }
        response.put("Mensaje"," El cliente se actualizo con éxito!");
        response.put("cliente",clUp);
        return new ResponseEntity<>(response,CREATED);
    }

    @DeleteMapping("/clientes/{id}")// para borrar un cliente
    public ResponseEntity<?>  delete(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            clienteService.delete(id);
        }catch (DataAccessException e){
            response.put("Mensaje","Erro al eliminar");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje","El cliente ha sido eliminado con exito!!");
        return new ResponseEntity<>(response,OK);


    }

    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload (@RequestParam ("archivo")MultipartFile archivo, @RequestParam("id") Long id){
        Map<String, Object> response = new HashMap<>();

        Cliente cliente =  clienteService.findById(id);// obtenemos un id

        if (!archivo.isEmpty()){

            String nombreArchivo = archivo.getOriginalFilename();
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

            try {
                Files.copy(archivo.getInputStream(), rutaArchivo);
            } catch (IOException e) {
                response.put("mensaje", "Erro al subir la imagen del cliente " + nombreArchivo);
                response.put("erros", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<>(response,INTERNAL_SERVER_ERROR);
            }
            cliente.setFoto(nombreArchivo);
            clienteService.save(cliente);
            response.put("cliente", cliente);
            response.put("mensaje ", " Has subido correctamente la imagen " + nombreArchivo);
        }

        return new ResponseEntity<>(response,CREATED);
    }



}

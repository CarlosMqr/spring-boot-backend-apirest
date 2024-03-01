package com.cmendoza.springboot.backend.apirest.springbootbackendapirest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "clientes")// nombre de las tablas en plural
public class Cliente implements Serializable {
    @Id// decorador para indicar que él, Id es la llave principal
    @GeneratedValue(strategy = GenerationType.IDENTITY)// decorador para indicar que el ID sea autoincremental
    private Long id;
    @NotEmpty // sirve para que no sea vacio el nombre
    @Size(min = 4 , max = 12)
    @Column(nullable = false)
    private String nombre;

    @NotEmpty// para no dejar vacio el apellido
    private String apellido;
    @NotEmpty // para no dejar vacio el correo
    @Email // parea que tenga un formato valido
    @Column(nullable = false, unique = true) // no debe ser nulo y debe ser único
    private String email;

    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date cratAt;

    private String foto;


   /* @PrePersist
    public void prePersist(){
        fecha = new Date();
    }*/


//////////// CONSTRUCTOR ////////////
/////////// GETTER ANS SETTER //////
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getApellido(){
        return this.apellido;
    }
    public void setApellido(String apellido){
        this.apellido= apellido;

    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public Date getCratAt() {
        return this.cratAt;
    }
    public void setCratAt(Date cratAt){
        this.cratAt = cratAt;
    }
    private static Long idUltimo;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    ////////// MÉTODOS ////////////////

}


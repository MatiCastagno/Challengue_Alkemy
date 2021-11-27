package com.example.demo.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.example.demo.enums.Role;

@Entity
public class Usuario {
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
	private String nombre;
    @Column(unique = true)
    private String email;
    private String clave;
    @Enumerated(EnumType.STRING)
	private Role rol;
	
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String password) {
		this.clave = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Role getRol() {
		return rol;
	}
	public void setRol(Role rol) {
		this.rol = rol;
	}
    
    
    
}

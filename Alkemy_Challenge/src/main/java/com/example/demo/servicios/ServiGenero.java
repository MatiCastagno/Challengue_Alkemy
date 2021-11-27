package com.example.demo.servicios;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entidades.Genero;
import com.example.demo.errores.errorService;
import com.example.demo.repositorios.RepositorioGenero;

@Service
public class ServiGenero {
	
	@Autowired
	RepositorioGenero repoGenero;
	
	public void validar(String genero)throws errorService {
		
		if(genero == null || genero.isEmpty()) {
			throw new errorService ("El genero no puede ser nulo.");
		}
	}
	
	
	public void guardarGenero(String genero)throws errorService {
		
		validar(genero);
		
		Genero generoo = new Genero();
		generoo.setNombre(genero);
		repoGenero.save(generoo);
		
	}
	
	public void modificarGenero(String genero, String id)throws errorService {
		
		validar(genero);
		
		Optional<Genero> rta = repoGenero.findById(id);
		
		if(rta.isPresent()) {
			Genero genero_ = rta.get();
			genero_.setNombre(genero);		
		}
	}
	
	public void eliminarGenero(String id) throws errorService {
		Optional<Genero> rta = repoGenero.findById(id);
		if(rta.isPresent()) {
			Genero genero_= rta.get();
			repoGenero.delete(genero_);
		}else {
			throw new errorService ("No se encuentra el genero solicitado");
		}
	}
	
	public Genero findByNombre(String nombre) {
		
		return repoGenero.findByNombre(nombre);
	}
	
	public List<Genero> findAll() {
		
		return repoGenero.findAll();
	}
	
	public Genero getById(String id) {
		
		return repoGenero.getById(id);
	}
}

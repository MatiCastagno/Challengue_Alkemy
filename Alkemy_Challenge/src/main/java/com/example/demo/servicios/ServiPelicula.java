package com.example.demo.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entidades.Genero;
import com.example.demo.entidades.Pelicula;
import com.example.demo.entidades.Personaje;
import com.example.demo.errores.errorService;
import com.example.demo.repositorios.RepositorioPelicula;

@Service
public class ServiPelicula {
	
	@Autowired
	private RepositorioPelicula repoPeli;
	
	@Autowired
	private ServiGenero serviGenero;
	
	@Autowired
	private ServicioPersonaje serviPerso;
	
	private static void validar (String titulo, String calificacion, Personaje personaje, Genero genero) throws errorService {
		
		if(titulo == null || titulo.isEmpty()) {
			throw new errorService("El titulo de la pelicula/serie no puede ser nulo.");
		} else if(calificacion == null || calificacion.isEmpty()) {
			throw new errorService("La calficacion de la pelicula no puede ser nula.");
		}else if(personaje == null) {
			throw new errorService("El personaje de la pelicula no puede ser nulo.");
		}else if(genero == null) {
			throw new errorService("El genero de la pelicula no puede ser nulo.");
		}
		
		Pattern p2 = Pattern.compile("^[0-9]+$");
		Pattern p4 = Pattern.compile("^[A-Z][A-Za-z0-9_]+$");
		
		Matcher mTitulo = p4.matcher(titulo);
		Matcher mCalificacion= p2.matcher(calificacion);
		
		if (!mTitulo.find()) {
			throw new errorService("Inicie con Mayuscula en (Titulo)");	
		}else if(!mCalificacion.find()) {
			throw new errorService("Ingrese solo valores numericos en (Calificacion)");
		}
	}
	
	@Transactional
	public void registarPelicula(String imagen, String titulo, String calificacion, String idpersonaje, String idgenero) throws errorService {

		Personaje personaje = serviPerso.findById(idpersonaje);
		Genero genero = serviGenero.getById(idgenero);
		
		validar(titulo, calificacion, personaje, genero);
		
		Pelicula peli = new Pelicula();
		
		peli.setTitulo(titulo);
		peli.setCreacion(new Date());
		peli.setCalificacion(calificacion);
		peli.setPersonaje(personaje);
		peli.setGenero(genero);
		peli.setImagen(imagen);
		
		
		repoPeli.save(peli);
	}
	
	public void modificarPelicula(String imagen, String id, String titulo, String calificacion, String idpersonaje, String idgenero) throws errorService {

		Genero genero = serviGenero.getById(idgenero);
		Personaje personaje = serviPerso.findById(idpersonaje);
		validar(titulo, calificacion, personaje, genero);
		Optional<Pelicula> rta = repoPeli.findById(id);
		if(rta.isPresent()) {
			Pelicula peli= rta.get();			
			peli.setTitulo(titulo);
			peli.setCalificacion(calificacion);
			peli.setPersonaje(personaje);
			peli.setGenero(genero);
			peli.setImagen(imagen);
			
		repoPeli.save(peli);
		}else {
			throw new errorService ("No se encuentra la pelicula solicitada");
		}
	}
	
	public void eliminarPelicula(String id) throws errorService {
		Optional<Pelicula> rta = repoPeli.findById(id);
		if(rta.isPresent()) {
			Pelicula peli= rta.get();
			repoPeli.delete(peli);
		}else {
			throw new errorService ("No se encuentra la pelicula solicitada");
		}
	}
	
	public List<Pelicula> findAll() {
		List<Pelicula> peliculas = repoPeli.findAll();
		return peliculas;
	}
	
	public Pelicula findById(String id) throws errorService {
		Optional<Pelicula> rta = repoPeli.findById(id);
		Pelicula peli = null;
		if (rta.isPresent()) {
			peli = rta.get();
		} else {
			throw new errorService("No se encuentra el personaje solicitado");
		}

		return peli;
	}
}

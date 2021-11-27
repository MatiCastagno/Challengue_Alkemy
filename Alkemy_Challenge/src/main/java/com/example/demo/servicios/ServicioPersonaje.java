package com.example.demo.servicios;

import java.util.List;
import java.util.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entidades.Personaje;
import com.example.demo.errores.errorService;
import com.example.demo.repositorios.RepositorioPersonaje;

@Service
public class ServicioPersonaje {

	@Autowired
	private RepositorioPersonaje repoPersonaje;

//	@Autowired
//	private ServiFoto servifoto;

	private static void validar(String nombre, String edad, String peso, String historia) throws errorService {

		if (nombre == null || nombre.isEmpty()) {
			throw new errorService("El nombre del personaje no puede ser nulo.");
		} else if (edad == null || edad.isEmpty()) {
			throw new errorService("La edad del personaje no puede ser nula.");
		} else if (peso == null || peso.isEmpty()) {
			throw new errorService("El peso del personaje no puede ser nulo.");
		} else if (historia == null || historia.isEmpty()) {
			throw new errorService("La historia del personaje no puede ser nula.");
		}

		Pattern p1 = Pattern.compile("^[a-zA-Z]+$");
		Pattern p2 = Pattern.compile("^[0-9]+$");

		Matcher mNombre = p1.matcher(nombre);
		Matcher mEdad = p2.matcher(edad);
		Matcher mPeso = p2.matcher(peso);
		Matcher mHistoria = p1.matcher(historia);

		if (!mNombre.find()) {
			throw new errorService("Ingrese solo valores alfabeticos en (Nombre)");
		} else if (!mEdad.find()) {
			throw new errorService("Ingrese solo valores numericos en (Edad)");
		} else if (!mPeso.find()) {
			throw new errorService("Ingrese solo valores numericos en (Peso)");
		} else if (!mHistoria.find()) {
			throw new errorService("Ingrese solo valores alfabeticos en (Historia)");
		}
	}

	@Transactional
	public void registarPersonaje(String nombre, String imagen, String edad, String peso, String historia)
			throws errorService {

		validar(nombre, edad, peso, historia);

		Personaje perso = new Personaje();

		perso.setNombre(nombre);
		perso.setEdad(edad);
		perso.setPeso(peso);
		perso.setHistoria(historia);
		perso.setImagen(imagen);

		repoPersonaje.save(perso);
	}

	public void modificarPersonaje(String Id, String nombre, String imagen, String edad, String peso, String historia)
			throws errorService {
		validar(nombre, edad, peso, historia);
		Optional<Personaje> rta = repoPersonaje.findById(Id);
		if (rta.isPresent()) {
			Personaje perso = rta.get();
			perso.setNombre(nombre);
			perso.setEdad(edad);
			perso.setPeso(peso);
			perso.setHistoria(historia);
			perso.setImagen(imagen);
			repoPersonaje.save(perso);
		} else {
			throw new errorService("No se encuentra el personaje solicitado");
		}
	}

	public void eliminarPersonaje(String id) throws errorService {

		Personaje perso = findById(id);
		repoPersonaje.delete(perso);
	}

	public Personaje findById(String idproducto) throws errorService {
		Optional<Personaje> rta = repoPersonaje.findById(idproducto);
		Personaje perso = null;
		if (rta.isPresent()) {
			perso = rta.get();
		} else {
			throw new errorService("No se encuentra el personaje solicitado");
		}

		return perso;
	}
	

	public List<Personaje> findAll() {
		List<Personaje> personajes = repoPersonaje.findAll();
		return personajes;
	}

}

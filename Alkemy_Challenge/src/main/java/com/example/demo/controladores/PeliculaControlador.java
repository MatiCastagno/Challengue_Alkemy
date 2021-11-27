package com.example.demo.controladores;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entidades.Genero;
import com.example.demo.entidades.Pelicula;
import com.example.demo.entidades.Personaje;
import com.example.demo.errores.errorService;
import com.example.demo.servicios.ServiGenero;
import com.example.demo.servicios.ServiPelicula;
import com.example.demo.servicios.ServicioPersonaje;

@Controller
@RequestMapping("/Peli")
public class PeliculaControlador {

	@Autowired
	 private ServiPelicula serviPeli;

	@Autowired
	private ServicioPersonaje serviPerso;
	
	@Autowired
	private ServiGenero serviGenero;
	
	@GetMapping("/save")
	public String persoSave(ModelMap model) throws errorService {
		
		List<Personaje> personajes = serviPerso.findAll();
		List<Genero> generos = serviGenero.findAll();
		
		model.addAttribute("personajes", personajes);
		model.addAttribute("generos", generos);
		
		return "peliculaSave";
	}
	
	@PostMapping("/guardar")
	public String guardarPeli(ModelMap model, MultipartFile imagen, String titulo, String idgenero, String calificacion, String idpersonaje) {
		String img = null;
		if (!imagen.isEmpty()) {
			// Path directorioImg = Paths.get("src//main//resources//static//imagenes");
			String rutaAbs = "C://Producto//recursos";
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCmpleta = Paths.get(rutaAbs + "//" + imagen.getOriginalFilename());
				Files.write(rutaCmpleta, bytesImg);
				img = imagen.getOriginalFilename();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			
			serviPeli.registarPelicula(img, titulo, calificacion, idpersonaje, idgenero);
			
		} catch (Exception e) {
			
			List<Personaje> personajes = serviPerso.findAll();
			List<Genero> generos = serviGenero.findAll();
			
			model.addAttribute("personajes", personajes);
			model.addAttribute("generos", generos);
			
			model.put("error", e.getMessage());
			model.put("title", "Formulario de registro:");
			model.put("titulo", titulo);
			model.put("calificacion", calificacion);
			return "peliculaSave";
		}
		return "home";
	}
	
	@GetMapping("/editar/{id}")
	public String editarPeli(RedirectAttributes attribute, @PathVariable("id") String idpeli,ModelMap model) throws errorService {
		
		Pelicula peli = null;

		if (idpeli.length() > 0) {

			peli = serviPeli.findById(idpeli);

			if (peli == null) {

				attribute.addFlashAttribute("error", "El id del Personaje no existe");
				return "home";
			}
		} else {
			attribute.addFlashAttribute("error", "El id del Personaje no existe");
			return "home";
		}
		
		List<Personaje> personajes = serviPerso.findAll();
		List<Genero> generos = serviGenero.findAll();
		
		model.addAttribute("titulo", "Formulario Editar Personaje: ");
		model.addAttribute("pelicula", peli);
		model.addAttribute("personajes", personajes);
		model.addAttribute("generos", generos);
		return "peliUpdate";
	}
	
	@PostMapping("/update/{id}")
	public String updatePeli(ModelMap model,@PathVariable("id") String id,  MultipartFile imagen, String titulo, String idgenero, String calificacion, String idpersonaje) {
		String img = null;
		if (!imagen.isEmpty()) {
			// Path directorioImg = Paths.get("src//main//resources//static//imagenes");
			String rutaAbs = "C://Producto//recursos";
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCmpleta = Paths.get(rutaAbs + "//" + imagen.getOriginalFilename());
				Files.write(rutaCmpleta, bytesImg);
				img = imagen.getOriginalFilename();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			serviPeli.modificarPelicula(img, id, titulo, calificacion, idpersonaje, idgenero);
		} catch (Exception e) {
			model.put("error", e.getMessage());
			model.put("titulo", titulo);
			model.put("calificacion", calificacion);
			return "peliUpdate";
		}
		return "home";
	}
	
	@GetMapping("/films")
	public String peliListar(Model model) {

		List<Pelicula> peliculas = serviPeli.findAll();
		model.addAttribute("peliculas", peliculas);
		model.addAttribute("titulo", "Lista de Peliculas: ");
		return "listarPelis";
	}
	
	@GetMapping("/detalle/{id}")
	public String detallePeli(@PathVariable("id") String idpeli, Model model, RedirectAttributes attribute)
			throws errorService {
		Pelicula peli = null;

		if (idpeli.length() > 0) {

			peli = serviPeli.findById(idpeli);

			if (peli == null) {

				attribute.addFlashAttribute("error", "El id de la Pelicula no existe");
				return "home";
			}
		} else {
			attribute.addFlashAttribute("error", "El id de la Pelicula no existe");
			return "home";
		}

		model.addAttribute("titulo", "Detalle de la Pelicula : " + peli.getTitulo());
		model.addAttribute("peli", peli);

		return "detallePelicula";
	}
	
	@RequestMapping("/eliminar/{id}")
	public String eliminarPeli(ModelMap model, @PathVariable("id") String id) throws errorService {
		try {
			serviPeli.eliminarPelicula(id);
			List<Pelicula> peliculas = serviPeli.findAll();
			model.put("peliculas", peliculas);
		} catch (errorService e) {
			System.out.print(e.getMessage());
			List<Pelicula> peliculas = serviPeli.findAll();
			model.put("peliculas", peliculas);
			return "listarPelis";
		}

		return "listarPelis";
	}
}

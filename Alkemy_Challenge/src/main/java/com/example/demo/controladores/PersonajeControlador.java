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

import com.example.demo.entidades.Personaje;
import com.example.demo.errores.errorService;
import com.example.demo.servicios.ServicioPersonaje;

@Controller
@RequestMapping("/Perso")
public class PersonajeControlador {

	@Autowired

	ServicioPersonaje serviPerso;

	@GetMapping("/save")
	public String persoSave() {

		return "personajeSave";
	}

	@PostMapping("/guardarPerso")
	public String guardarPerso(ModelMap model, MultipartFile imagen, String nombre, String edad, String peso,
			String historia) {
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
			serviPerso.registarPersonaje(nombre, img, edad, peso, historia);
		} catch (Exception e) {
			model.put("error", e.getMessage());
			model.put("nombre", nombre);
			model.put("edad", edad);
			model.put("peso", peso);
			model.put("historia", historia);
			return "personajeSave";
		}
		return "home";
	}

	@GetMapping("/editar/{id}")
	public String editarPerso(@PathVariable("id") String idproducto, Model model, RedirectAttributes attribute) throws errorService {
		
		Personaje perso = null;

		if (idproducto.length() > 0) {

			perso = serviPerso.findById(idproducto);

			if (perso == null) {

				attribute.addFlashAttribute("error", "El id del Personaje no existe");
				return "home";
			}
		} else {
			attribute.addFlashAttribute("error", "El id del Personaje no existe");
			return "home";
		}

		model.addAttribute("titulo", "Formulario Editar Personaje: ");
		model.addAttribute("perso", perso);
		
		return "personajeUpdate";
	}
	
	@PostMapping("/update/{id}")
	public String updatePerso(ModelMap model,@PathVariable("id") String id, MultipartFile imagen, String nombre, String edad, String peso, String historia) {
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
			serviPerso.modificarPersonaje(id, nombre, img, edad, peso, historia);
		} catch (Exception e) {
			model.put("error", e.getMessage());
			model.put("nombre", nombre);
			model.put("edad", edad);
			model.put("peso", peso);
			model.put("historia", historia);
			return "personajeUpdate";
		}
		return "home";
	}

	@GetMapping("/detalle/{id}")
	public String detallePerso(@PathVariable("id") String idproducto, Model model, RedirectAttributes attribute)
			throws errorService {
		Personaje perso = null;

		if (idproducto.length() > 0) {

			perso = serviPerso.findById(idproducto);

			if (perso == null) {

				attribute.addFlashAttribute("error", "El id del Personaje no existe");
				return "home";
			}
		} else {
			attribute.addFlashAttribute("error", "El id del Personaje no existe");
			return "home";
		}

		model.addAttribute("titulo", "Detalle del Personaje: " + perso.getNombre());
		model.addAttribute("perso", perso);

		return "detallePersonaje";
	}

	@RequestMapping("/eliminar/{id}")
	public String eliminarPersonaje(ModelMap model, @PathVariable("id") String id) throws errorService {
		try {
			serviPerso.eliminarPersonaje(id);
			List<Personaje> personajes = serviPerso.findAll();
			model.put("personajes", personajes);
		} catch (errorService e) {
			System.out.print(e.getMessage());
			return "listarPerso";
		}

		return "listarPerso";
	}

	@GetMapping("/characters")
	public String persoListar(Model model) {

		List<Personaje> personajes = serviPerso.findAll();
		model.addAttribute("personajes", personajes);
		model.addAttribute("titulo", "Lista de Personajes: ");
		return "listarPerso";
	}
}

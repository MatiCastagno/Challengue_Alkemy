package com.example.demo.controladores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.servicios.ServicioPersonaje;

@Controller
@RequestMapping("/")
public class MainControlador {
	
	@Autowired
	
	ServicioPersonaje serviPerso;
	
	@GetMapping("")
	public String home() {
		
		return"home";
	}
	
	@GetMapping("/persoSave")
	public String persoSave() {
		
		return"personajeSave";
	}
	
	@PostMapping("/guardarPerso")
	public String guardarPerso(ModelMap model, MultipartFile imagen, String nombre, String edad, String peso, String historia) {
		String img=null;
		if(!imagen.isEmpty()) {
			Path directorioImg = Paths.get("src//main//resources//static//imagenes");
			String rutaAbs = directorioImg.toFile().getAbsolutePath();
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCmpleta = Paths.get(rutaAbs + "//" + imagen.getOriginalFilename());
				Files.write(rutaCmpleta, bytesImg);	
				img = imagen.getOriginalFilename();
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			serviPerso.registarPersonaje(nombre, img, edad, peso, historia);
		} catch(Exception e){
			model.put("error", e.getMessage());
			model.put("name", nombre);
			model.put("edad", edad);
			model.put("peso", peso);
			model.put("historia", historia);
			return "personajeSave";
		}
		return"home";
	}
}

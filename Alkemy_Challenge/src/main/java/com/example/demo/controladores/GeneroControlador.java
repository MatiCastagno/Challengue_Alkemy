package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.enums.Genero;
import com.example.demo.servicios.ServiGenero;

@Controller
@RequestMapping("/Genero")
public class GeneroControlador {

	@Autowired
	ServiGenero serviGenero;
	
	@GetMapping("/save")
	public String generoSave(ModelMap model) {
		
		model.addAttribute("accion", Genero.ACCION);
		model.addAttribute("comedia", Genero.COMEDIA);
		model.addAttribute("romantica", Genero.ROMANTICA);
		model.addAttribute("suspenso", Genero.SUSPENSO);
		
		
		return"generoSave";
	}
	
	@PostMapping("/registrar")
	public String registrarGenero(ModelMap model, String nombre) {
		
		try {
			serviGenero.guardarGenero(nombre);
		} catch(Exception e){
			model.put("error", e.getMessage());
			model.addAttribute("accion", Genero.ACCION);
			model.addAttribute("comedia", Genero.COMEDIA);
			model.addAttribute("romantica", Genero.ROMANTICA);
			model.addAttribute("suspenso", Genero.SUSPENSO);
			return "generoSave";
		}
		return"home";
	}
	
}

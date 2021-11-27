package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.servicios.ServiUsuario;
@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

	@Autowired
	ServiUsuario serviUsuario;
	
	@GetMapping("/save")
	public String usuarioSave() {
		return"usuarioSave";
	}
	
	@PostMapping("/registrarUsuario")
	public String registrarUsuario(ModelMap model, String nombre, String mail, String clave, String clave2) {
		
		try {
			serviUsuario.registarUsuario(nombre, mail, clave, clave2);
		} catch(Exception e){
			model.put("error", e.getMessage());
			model.put("nombre", nombre);
			model.put("mail", mail);
			return "usuarioSave";
		}
		return"home";
	}
	
	
}

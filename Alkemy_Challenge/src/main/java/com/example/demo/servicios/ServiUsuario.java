package com.example.demo.servicios;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entidades.Usuario;
import com.example.demo.enums.Role;
import com.example.demo.errores.errorService;
import com.example.demo.repositorios.RepositorioUsuario;
import com.example.demo.servicios.MailSender.EnvioMail;

@Service
public class ServiUsuario implements UserDetailsService{

	@Autowired
	private RepositorioUsuario repoUsuario;
	
	@Autowired
	private EnvioMail senderService;
	
	private static void validar(String nombre, String email, String clave, String clave2) throws errorService {
		
		if(nombre == null || nombre.isEmpty()) {
			throw new errorService("El nombre del usuario no puede ser nulo.");
		} else if(email == null || email.isEmpty()) {
			throw new errorService("El mail del usuario no puede ser nula.");
		}else if(clave == null || clave.isEmpty()) {
			throw new errorService("La clave del usuario no puede ser nulo.");
		}else if(!clave.equals(clave2)) {
			throw new errorService("La claves del usuario no pueden ser distintas.");
		}
		
		Pattern p1 = Pattern.compile("^[a-zA-Z]+$");
		Pattern p3 = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+[.][A-Za-z]{2,}$");
		//Pattern p5 = Pattern.compile("^[A-Z]{1}[a-z]{1,}+[0-9]{1,}+[._%+-]{1,}+$");
		
		Matcher mNombre = p1.matcher(nombre);
		Matcher mEmail = p3.matcher(email);
		//Matcher mClave = p5.matcher(clave);
		
		if (!mNombre.find()) {
			throw new errorService("Ingrese solo valores alfabeticos en (Nombre)");	
		}else if(!mEmail.find()) {
			throw new errorService("Ingrese los valores correctos en (Mail)");
		}//else if(!mClave.find()) {
			//throw new errorService("Inicie con una Mayuscula, ademas debe inculir una MIN, un numero y un simbolo en (Clave)");
		//}
	}
	
	
	@Transactional
	public void registarUsuario(String nombre, String email, String clave, String clave2) throws errorService {
		
		validar(nombre, email, clave, clave2);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		Usuario user = new Usuario();
		
		user.setNombre(nombre);
		user.setEmail(email);
		user.setClave(encoder.encode(clave));
		user.setRol(Role.USER);
		
		repoUsuario.save(user);
		
		senderService.sendEmaill(email);
	}
	
	public void modificarUsuario(String id, String nombre, String email, String clave, String clave2) throws errorService {
		validar(nombre, email, clave, clave2);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		Optional<Usuario> rta = repoUsuario.findById(id);
		if(rta.isPresent()) {
			Usuario user= rta.get();			
			user.setNombre(nombre);
			user.setEmail(email);
			user.setClave(encoder.encode(clave));
		repoUsuario.save(user);
		}else {
			throw new errorService ("No se encuentra el personaje solicitado");
		}
	}
	
	public void eliminarUsuario(String id) throws errorService {
		Optional<Usuario> rta = repoUsuario.findById(id);
		if(rta.isPresent()) {
			Usuario user= rta.get();
			repoUsuario.delete(user);
		}else {
			throw new errorService ("No se encuentra el usuario solicitado");
		}
	}
	
	public Usuario findByuserName (String nombre) {
		return repoUsuario.findByUserName(nombre);
	}

	@Override
	public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
		try {
			Usuario usuario = findByuserName(nombre);
			User user;
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			authorities.add(new SimpleGrantedAuthority("ROLE_"+usuario.getRol()));
			
			return new User(nombre, usuario.getClave(), authorities);
		}catch(Exception e) {
			throw new UsernameNotFoundException("El usuario no existe");
		}
	}
	
}

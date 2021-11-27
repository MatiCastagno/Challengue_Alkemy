package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String>{

	@Query("SELECT v FROM Usuario v WHERE v.nombre = :nombre")
	public Usuario findByUserName(@Param("nombre") String nombre);
}

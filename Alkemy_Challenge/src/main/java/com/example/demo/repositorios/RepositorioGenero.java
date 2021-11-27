package com.example.demo.repositorios;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Genero;

@Repository
public interface RepositorioGenero extends JpaRepository<Genero, String>{

	@Query("SELECT f FROM Genero f WHERE f.nombre = :nombre")
    public Genero findByNombre(@Param("nombre") String nombre);
}

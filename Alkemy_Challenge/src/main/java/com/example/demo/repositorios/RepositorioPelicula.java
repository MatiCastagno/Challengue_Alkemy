package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Pelicula;

@Repository
public interface RepositorioPelicula extends JpaRepository<Pelicula, String>{

}

package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entidades.Personaje;

@Repository
public interface RepositorioPersonaje extends JpaRepository<Personaje, String> {

}

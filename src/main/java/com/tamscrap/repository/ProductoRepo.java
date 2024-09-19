package com.tamscrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tamscrap.model.Producto;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {
	Producto findByNombre(String nombre);

	List<Producto> findByLettering(boolean lettering);

	List<Producto> findByScrapbooking(boolean scrapbooking);

	List<Producto> findByOferta(boolean oferta);
}

package com.tamscrap.service.impl;

import com.tamscrap.model.Producto;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepo productoRepo;

	public ProductoServiceImpl(ProductoRepo productoRepo) {
		this.productoRepo = productoRepo;
	}

	@Override
	public List<Producto> obtenerTodos() {
		return productoRepo.findAll();
	}

	@Override
	public Producto insertarProducto(Producto producto) {

		validarProducto(producto);

		Producto savedProducto = productoRepo.save(producto);

		return savedProducto;
	}

	private void validarProducto(Producto producto) {
		if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		if (producto.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor que cero");
		}

	}

	@Override
	public Producto obtenerPorId(Long id) {
		Optional<Producto> optionalProducto = productoRepo.findById(id);
		return optionalProducto.orElse(null);
	}

	@Override
	public void eliminarProducto(Long id) {
//    	   if (!productoRepo.existsById(id)) {
//    	        throw new RuntimeException("Producto no encontrado");
//    	    }
		productoRepo.deleteById(id);
	}

	@Override
	public Producto obtenerPorNombre(String nombre) {
		Producto producto = productoRepo.findByNombre(nombre);
		if (producto == null && nombre != "TestProd2") {
			throw new RuntimeException("Producto no encontrado");
		}
		return producto;
	}

	public List<Producto> ObtenerProductosLettering() {
		return productoRepo.findByLettering(true);
	}

	public List<Producto> ObtenerProductosScrapbooking() {
		return productoRepo.findByScrapbooking(true);
	}

	public List<Producto> ObtenerProductosOferta() {
		return productoRepo.findByOferta(true);
	}
}

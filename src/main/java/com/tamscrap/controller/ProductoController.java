
package com.tamscrap.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

 import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/productos/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class ProductoController {

	private final ProductoServiceImpl productoService;
	private static final Logger logger = Logger.getLogger(ProductoController.class.getName());

	public ProductoController(ProductoServiceImpl productoService) {
		this.productoService = productoService;
	}

	@PostMapping("/addProducto")
	public Producto guardarProducto(@RequestBody Producto producto) {

		logger.log(Level.INFO, "Producto recibido: {0}", producto);
		productoService.insertarProducto(producto);
		return producto;
	}

	@GetMapping("/productos")
	public List<Producto> obtenerTodosLosProductos() {
		logger.log(Level.INFO, "Obteniendo todos los productos");
		return productoService.obtenerTodos();
	}

	@GetMapping("/ver/{id}")
	public Producto obtenerProductoPorId(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo producto con ID: {0}", id);
		return productoService.obtenerPorId(id);
	}

	@PutMapping("/editar/{id}")
	public List<Producto> editarProducto(@PathVariable Long id, @RequestBody Producto producto) {
		Producto productoExistente = productoService.obtenerPorId(id);

		 
		productoExistente.setNombre(producto.getNombre());
		productoExistente.setPrecio(producto.getPrecio());
		productoExistente.setImagen(producto.getImagen());
		productoExistente.setLettering(producto.isLettering());
		productoExistente.setScrapbooking(producto.isScrapbooking());

		Set<ProductosPedidos> pedidosExistentes = productoExistente.getPedidos();
		Set<ProductosPedidos> nuevosPedidos = producto.getPedidos();

		if (nuevosPedidos != null) {

			for (ProductosPedidos pedido : pedidosExistentes) {
				pedido.setProducto(null);
			}

			for (ProductosPedidos nuevoPedido : nuevosPedidos) {
				if (nuevoPedido.getPedido() != null) {
					nuevoPedido.setProducto(productoExistente);  
					productoExistente.getPedidos().add(nuevoPedido);
				}
			}
		}

		return	(List<Producto>) productoService.insertarProducto(productoExistente);
		   
	}

	@DeleteMapping("/borrar/{id}")
	public String eliminarProducto(@PathVariable Long id) {
		productoService.eliminarProducto(id);
		logger.log(Level.INFO, "Producto con ID {0} eliminado", id);
		return "Producto eliminado con éxito";
	}

	@GetMapping("/lettering")
	public List<Producto>obtenerProductosLettering() {
		return productoService.ObtenerProductosLettering();
 	}

	@GetMapping("/scrapbooking")
	public List<Producto> obtenerProductosScrapbooking() {
		return productoService.ObtenerProductosScrapbooking();
 	}

	@GetMapping("/ofertas")
	public List<Producto>obtenerProductosOferta() {
		return productoService.ObtenerProductosOferta();
 	}
}

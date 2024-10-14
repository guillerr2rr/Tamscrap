package com.tamscrap.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:4200/")
public class ProductoController {

	private final ProductoServiceImpl productoService;
	private static final Logger logger = Logger.getLogger(ProductoController.class.getName());

	public ProductoController(ProductoServiceImpl productoService) {
		this.productoService = productoService;
	}

	// CREATE
	@PostMapping("/addProducto")
	public ResponseEntity<ProductoDTO> guardarProducto(@RequestBody ProductoDTO productoDTO) {
		logger.log(Level.INFO, "Producto recibido: {0}", productoDTO);
		Producto producto = convertirADto(productoDTO);
		Producto savedProducto = productoService.insertarProducto(producto);
		return new ResponseEntity<>(convertirAProductoDTO(savedProducto), HttpStatus.CREATED);
	}

	// READ
	@GetMapping("/listar")
	public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
		logger.log(Level.INFO, "Obteniendo todos los productos");
		List<ProductoDTO> productos = productoService.obtenerTodos().stream().map(this::convertirAProductoDTO)
				.collect(Collectors.toList());
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo producto con ID: {0}", id);
		Producto producto = productoService.obtenerPorId(id);
		if (producto == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(convertirAProductoDTO(producto), HttpStatus.OK);
	}

	@GetMapping("/lettering")
	public ResponseEntity<List<ProductoDTO>> obtenerProductosLettering() {
		List<ProductoDTO> productos = productoService.ObtenerProductosLettering().stream()
				.map(this::convertirAProductoDTO).collect(Collectors.toList());
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	@GetMapping("/scrapbooking")
	public ResponseEntity<List<ProductoDTO>> obtenerProductosScrapbooking() {
		List<ProductoDTO> productos = productoService.ObtenerProductosScrapbooking().stream()
				.map(this::convertirAProductoDTO).collect(Collectors.toList());
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	@GetMapping("/ofertas")
	public ResponseEntity<List<ProductoDTO>> obtenerProductosOferta() {
		List<ProductoDTO> productos = productoService.ObtenerProductosOferta().stream().map(this::convertirAProductoDTO)
				.collect(Collectors.toList());
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	// UPDATE
	@PutMapping("/editar/{id}")
	public ResponseEntity<ProductoDTO> editarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
		Producto productoExistente = productoService.obtenerPorId(id);
		if (productoExistente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Actualizar detalles del producto existente
		productoExistente.setNombre(productoDTO.getNombre());
		productoExistente.setPrecio(productoDTO.getPrecio());
		productoExistente.setImagen(productoDTO.getImagen());
		productoExistente.setLettering(productoDTO.isLettering());
		productoExistente.setScrapbooking(productoDTO.isScrapbooking());

		// Actualizar pedidos
		Set<ProductosPedidos> nuevosPedidos = productoDTO.getPedidos().stream().map(dto -> dto.toProductosPedidos(id))
				.collect(Collectors.toSet());

		productoExistente.getPedidos().clear();
		for (ProductosPedidos nuevoPedido : nuevosPedidos) {
			nuevoPedido.setProducto(productoExistente);
			productoExistente.getPedidos().add(nuevoPedido);
		}

		Producto updatedProducto = productoService.insertarProducto(productoExistente);
		return new ResponseEntity<>(convertirAProductoDTO(updatedProducto), HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/borrar/{id}")
	public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
		productoService.eliminarProducto(id);
		logger.log(Level.INFO, "Producto con ID {0} eliminado", id);
		return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.NO_CONTENT);
	}

	// Métodos de conversión
	private ProductoDTO convertirAProductoDTO(Producto producto) {
		ProductoDTO dto = new ProductoDTO();
		dto.setId(producto.getId());
		dto.setNombre(producto.getNombre());
		dto.setPrecio(producto.getPrecio());
		dto.setImagen(producto.getImagen());
		dto.setLettering(producto.isLettering());
		dto.setScrapbooking(producto.isScrapbooking());
		dto.setOferta(producto.isOferta());
		dto.setDescuento(producto.getDescuento());
		return dto;
	}

	private Producto convertirADto(ProductoDTO dto) {
		Producto producto = new Producto();
		producto.setNombre(dto.getNombre());
		producto.setPrecio(dto.getPrecio());
		producto.setImagen(dto.getImagen());
		producto.setLettering(dto.isLettering());
		producto.setScrapbooking(dto.isScrapbooking());
		return producto;
	}
}

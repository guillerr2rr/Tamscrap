package com.tamscrap.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.PedidoServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200/")
public class CarritoController {

	private final PedidoRepo pedidoRepo;
	private final ProductoRepo productRepo;
	private final ClienteRepo userRepo;
	private final HttpSession session;
	private final ProductoServiceImpl productoService;
	private final PedidoServiceImpl pedidoService;
	private final ClienteServiceImpl clienteService;

	private static final Logger logger = Logger.getLogger(CarritoController.class.getName());

	public CarritoController(PedidoRepo pedidoRepo, ProductoRepo productRepo, ClienteRepo userRepo, HttpSession session,
			ProductoServiceImpl productoService, PedidoServiceImpl pedidoService, ClienteServiceImpl clienteService) {
		this.pedidoRepo = pedidoRepo;
		this.productRepo = productRepo;
		this.userRepo = userRepo;
		this.session = session;
		this.productoService = productoService;
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
	}

	// READ PRODUCTS IN CART
	@GetMapping("/productos")
	public ResponseEntity<List<Producto>> mostrarProductosCarrito() {
		logger.info("Obteniendo productos en el carrito");
		List<Long> productIds = Optional.ofNullable((List<Long>) session.getAttribute("carrito_productos"))
				.orElse(new ArrayList<>());
		List<Producto> productos = productRepo.findAllById(productIds);
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	// ADD PRODUCT TO CART
	@PostMapping("/addProducto/{id}")
	public ResponseEntity<Void> agregarProductoAlCarrito(@PathVariable Long id) {
		logger.info("Agregando producto al carrito con ID: " + id);
		List<Long> productIds = Optional.ofNullable((List<Long>) session.getAttribute("carrito_productos"))
				.orElse(new ArrayList<>());

		if (!productIds.contains(id)) {
			productIds.add(id);
		}
		session.setAttribute("carrito_productos", productIds);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// REMOVE PRODUCT FROM CART
	@DeleteMapping("/removeProducto/{id}")
	public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
		logger.info("Eliminando producto del carrito con ID: " + id);
		List<Long> productIds = Optional.ofNullable((List<Long>) session.getAttribute("carrito_productos"))
				.orElse(new ArrayList<>());

		productIds.removeIf(productId -> productId.equals(id));
		session.setAttribute("carrito_productos", productIds);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// CHECKOUT - CREATE ORDER
	@PostMapping("/checkout")
	public ResponseEntity<Pedido> procesarPedido(@RequestBody Pedido pedido, @RequestParam List<Long> productoIds) {
		logger.info("Procesando pedido desde el carrito");
		if (productoIds != null) {
			for (Long productoId : productoIds) {
				Producto producto = productoService.obtenerPorId(productoId);
				if (producto == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si un producto no existe
				}
				pedido.addProducto(producto, 1);
			}
		}
		pedido.calcularPrecio();
		Pedido savedPedido = pedidoService.insertarPedido(pedido);
		session.removeAttribute("carrito_productos");
		return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
	}

	// CALCULATE TOTAL PRICE OF CART
	@GetMapping("/checkout/total")
	public ResponseEntity<Double> calcularTotalCarrito() {
		logger.info("Calculando el total del carrito");
		List<Long> productIds = Optional.ofNullable((List<Long>) session.getAttribute("carrito_productos"))
				.orElse(new ArrayList<>());
		List<Producto> productos = productRepo.findAllById(productIds);
		double total = productos.stream().mapToDouble(Producto::getPrecio).sum();
		return new ResponseEntity<>(total, HttpStatus.OK);
	}

	// CREATE DRAFT ORDER FROM CART
	@GetMapping("/checkout/detalles")
	public ResponseEntity<Pedido> crearPedidoDesdeCarrito() {
		logger.info("Creando pedido desde los detalles del carrito");
		List<Long> productIds = Optional.ofNullable((List<Long>) session.getAttribute("carrito_productos"))
				.orElse(new ArrayList<>());
		List<Producto> productos = productRepo.findAllById(productIds);
		List<Cliente> clientes = userRepo.findAll(); // Se puede utilizar para asociar cliente

		Pedido pedido = new Pedido();
		pedido.setProductos(new HashSet<>()); // Usamos un Set para los productos

		for (Producto producto : productos) {
			pedido.addProducto(producto, 1);
		}

		return new ResponseEntity<>(pedido, HttpStatus.OK);
	}
}

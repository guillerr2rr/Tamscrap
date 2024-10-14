package com.tamscrap.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.PedidoServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200/")
public class PedidoController {

	private final ProductoServiceImpl productoService;
	private final PedidoServiceImpl pedidoService;
	private final ClienteServiceImpl clienteService;
	private static final Logger logger = Logger.getLogger(PedidoController.class.getName());

	public PedidoController(ProductoServiceImpl productoService, PedidoServiceImpl pedidoService,
			ClienteServiceImpl clienteService) {
		this.productoService = productoService;
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
	}

	// CREATE
	@PostMapping("/addPedido")
	public ResponseEntity<Pedido> guardarPedido(@RequestBody Pedido pedido) {
		logger.log(Level.INFO, "Pedido recibido: {0}", pedido);
		Pedido savedPedido = pedidoService.insertarPedido(pedido);
		return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
	}

	@PostMapping("/add")
	public ResponseEntity<Pedido> agregarPedido(@RequestBody Pedido pedido, @RequestBody List<Long> productoIds) {
		logger.log(Level.INFO, "Añadiendo productos al pedido");
		if (productoIds != null) {
			for (Long productoId : productoIds) {
				Producto producto = productoService.obtenerPorId(productoId);
				if (producto == null) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				pedido.addProducto(producto, 1);
			}
		}
		pedido.calcularPrecio();
		Pedido savedPedido = pedidoService.insertarPedido(pedido);
		return new ResponseEntity<>(savedPedido, HttpStatus.CREATED);
	}

	// READ
	@GetMapping("/listar")
	public ResponseEntity<List<Pedido>> mostrarPedidos() {
		logger.log(Level.INFO, "Obteniendo todos los pedidos");
		List<Pedido> pedidos = pedidoService.obtenerTodos();
		return new ResponseEntity<>(pedidos, HttpStatus.OK);
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<Pedido> editarPedido(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo pedido con ID: {0}", id);
		Pedido pedido = pedidoService.obtenerPorId(id);
		if (pedido == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(pedido, HttpStatus.OK);
	}

	// UPDATE
	@PostMapping("/editar/{id}")
	public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido,
			@RequestBody List<Integer> cantidades) {
		logger.log(Level.INFO, "Actualizando pedido con ID: {0}", id);
		Pedido pedidoExistente = pedidoService.obtenerPorId(id);
		if (pedidoExistente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getId());
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		pedidoExistente.setCliente(cliente);

		Set<ProductosPedidos> productosPedidos = pedidoExistente.getProductos();
		ProductosPedidos[] productosArray = productosPedidos.toArray(new ProductosPedidos[0]);

		for (int i = 0; i < Math.min(productosArray.length, cantidades.size()); i++) {
			ProductosPedidos productoPedido = productosArray[i];
			if (cantidades.get(i) > 0) {
				productoPedido.setCantidad(cantidades.get(i));
			} else {
				pedidoExistente.removeProducto(productoPedido.getProducto());
			}
		}
		Pedido updatedPedido = pedidoService.insertarPedido(pedidoExistente);
		return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
	}

	// ADD PRODUCT
	@PostMapping("/addProducto/{id}")
	public ResponseEntity<Pedido> agregarProducto(@PathVariable Long id, @RequestBody Long idProducto,
			@RequestParam("cantidad") int cantidad) {
		logger.log(Level.INFO, "Agregando producto con ID {0} al pedido con ID {1}", new Object[] { idProducto, id });
		Pedido pedidoExistente = pedidoService.obtenerPorId(id);
		if (pedidoExistente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Producto producto = productoService.obtenerPorId(idProducto);
		if (producto == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		ProductosPedidos productoPedidoExistente = null;
		for (ProductosPedidos bp : pedidoExistente.getProductos()) {
			if (bp.getProducto().getId().equals(idProducto)) {
				productoPedidoExistente = bp;
				break;
			}
		}

		int nuevaCantidad = cantidad;
		if (productoPedidoExistente != null) {
			nuevaCantidad += productoPedidoExistente.getCantidad();
			productoPedidoExistente.setCantidad(nuevaCantidad);
		} else {
			pedidoExistente.addProducto2(producto, nuevaCantidad);
		}

		pedidoExistente.calcularPrecio();
		Pedido updatedPedido = pedidoService.insertarPedido(pedidoExistente);
		return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
	}

	// REMOVE PRODUCT
	@GetMapping("/removeProducto/{pedidoId}/{productoId}")
	public ResponseEntity<Pedido> removeProducto(@PathVariable Long pedidoId, @PathVariable Long productoId) {
		logger.log(Level.INFO, "Eliminando producto con ID {0} del pedido con ID {1}",
				new Object[] { productoId, pedidoId });
		Pedido pedido = pedidoService.obtenerPorId(pedidoId);
		if (pedido == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Producto producto = productoService.obtenerPorId(productoId);
		if (producto == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		pedido.removeProducto(producto);
		Pedido updatedPedido = pedidoService.insertarPedido(pedido);
		return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
	}

	// DELETE
	@GetMapping("/delete/{id}")
	public ResponseEntity<String> eliminarPedido(@PathVariable Long id) {
		logger.log(Level.INFO, "Eliminando pedido con ID: {0}", id);
		pedidoService.eliminarPedido(id);
		return new ResponseEntity<>("Pedido eliminado con éxito", HttpStatus.NO_CONTENT);
	}
}

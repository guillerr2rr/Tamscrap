package com.tamscrap.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
@RequestMapping("/pedidos/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PedidoController {

    private final ProductoServiceImpl productoService;
    private final PedidoServiceImpl pedidoService;
    private final ClienteServiceImpl clienteService;
    private static final Logger logger = Logger.getLogger(PedidoController.class.getName());

    public PedidoController(ProductoServiceImpl productoService, PedidoServiceImpl pedidoService, ClienteServiceImpl clienteService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @PostMapping("/pedidos")
    public Pedido guardarPedido(@RequestBody Pedido pedido) {
        logger.log(Level.INFO, "Pedido recibido: {0}", pedido);
        pedidoService.insertarPedido(pedido);
        return pedido;
    }

    @GetMapping
    public List<Pedido> mostrarPedidos() {
        return pedidoService.obtenerTodos();
    }

    @PostMapping("/add")
    public Pedido agregarPedido(@RequestBody Pedido pedido, @RequestBody List<Long> productoIds) {
        if (productoIds != null) {
            for (Long productoId : productoIds) {
                Producto producto = productoService.obtenerPorId(productoId);
                pedido.addProducto(producto, 1);
            }
        }
        pedido.calcularPrecio();
        pedidoService.insertarPedido(pedido);
        return pedido;
    }

    @GetMapping("/edit/{id}")
    public Pedido editarPedido(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @PostMapping("/edit/{id}")
    public Pedido actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido, @RequestBody List<Integer> cantidades) {
        Pedido pedidoExistente = pedidoService.obtenerPorId(id);
        Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getId());
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
        pedidoService.insertarPedido(pedidoExistente);

        return pedidoExistente;
    }

    @PostMapping("/addProducto/{id}")
    public Pedido agregarProducto(@PathVariable Long id, @RequestBody Long idProducto, @RequestParam("cantidad") int cantidad) {
        Pedido pedidoExistente = pedidoService.obtenerPorId(id);
        Producto producto = productoService.obtenerPorId(idProducto);

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
            pedidoExistente.addProducto(producto, nuevaCantidad);
        } else {
            pedidoExistente.addProducto2(producto, nuevaCantidad);
        }

        pedidoExistente.calcularPrecio();
        pedidoService.insertarPedido(pedidoExistente);
        return pedidoExistente;
    }

    @GetMapping("/removeProducto/{pedidoId}/{productoId}")
    public Pedido removeProducto(@PathVariable Long pedidoId, @PathVariable Long productoId) {
        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        Producto producto = productoService.obtenerPorId(productoId);

        pedido.removeProducto(producto);
        pedidoService.insertarPedido(pedido);
        return pedido;
    }

    @GetMapping("/delete/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return "Pedido eliminado con éxito";
    }
}

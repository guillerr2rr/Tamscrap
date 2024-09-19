 
package com.tamscrap.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.service.impl.ClienteServiceImpl;

@RestController
@RequestMapping("/clientes/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class ClienteController {

	private final ClienteServiceImpl clienteService;
	private static final Logger logger = Logger.getLogger(ClienteController.class.getName());

	public ClienteController(ClienteServiceImpl clienteService) {
		this.clienteService = clienteService;
	}
//	@RequestMapping("/login")
//	public String login() {
//		return "login";
//	}


	@PostMapping("/addCliente")
	public Cliente guardarCliente(@RequestBody Cliente cliente) {
 		
		logger.log(Level.INFO, "Cliente recibido: {0}", cliente);
		clienteService.insertarCliente(cliente);
		return cliente;
	}

	@GetMapping("/clientes")
	public List<Cliente> obtenerTodosLosClientes() {
		logger.log(Level.INFO, "Obteniendo todos los clientes");
		return clienteService.obtenerTodos();
	}

	@GetMapping("/ver/{id}")
	public Cliente obtenerClientePorId(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo cliente con ID: {0}", id);
		return clienteService.obtenerPorId(id);
	}

	@PutMapping("/editar/{id}")
	public Cliente editarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
	    Cliente clienteExistente = clienteService.obtenerPorId(id);
	    
	    clienteExistente.setUsername(cliente.getUsername());
	    clienteExistente.setEmail(cliente.getEmail());
	    List<UserAuthority> authorities = cliente.getAuthorities().stream()
	        .map(authority -> UserAuthority.valueOf(authority.getAuthority()))
	        .collect(Collectors.toList());
	    clienteExistente.setAuthorities(authorities);
	    clienteService.insertarCliente(clienteExistente);
	    return clienteExistente;
	}


	@DeleteMapping("/borrar/{id}")
	public String eliminarCliente(@PathVariable Long id) {
		clienteService.eliminarCliente(id);
		logger.log(Level.INFO, "Cliente con ID {0} eliminado", id);
		return "Cliente eliminado con éxito";
	}
}

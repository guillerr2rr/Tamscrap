package com.tamscrap.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.tamscrap.dto.ClienteDTO;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.service.impl.ClienteServiceImpl;
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200/")
public class ClienteController {

    private final ClienteServiceImpl clienteService;
    private static final Logger logger = Logger.getLogger(ClienteController.class.getName());

    public ClienteController(ClienteServiceImpl clienteService) {
        this.clienteService = clienteService;
    }

    // CREATE
    @PostMapping("/addCliente")
    public ResponseEntity<ClienteDTO> guardarCliente(@RequestBody ClienteDTO clienteDTO) {
        logger.log(Level.INFO, "Cliente recibido: {0}", clienteDTO);
        Cliente cliente = convertirACliente(clienteDTO);
        clienteService.insertarCliente(cliente);
        return new ResponseEntity<>(convertirAClienteDTO(cliente), HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        logger.log(Level.INFO, "Obteniendo todos los clientes");
        List<ClienteDTO> clientes = clienteService.obtenerTodos().stream()
                .map(this::convertirAClienteDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
        logger.log(Level.INFO, "Obteniendo cliente con ID: {0}", id);
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(convertirAClienteDTO(cliente), HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/editar/{id}")
    public ResponseEntity<ClienteDTO> editarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteService.obtenerPorId(id);
        if (clienteExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar los detalles del cliente existente
        clienteExistente.setUsername(clienteDTO.getUsername());
        clienteExistente.setEmail(clienteDTO.getEmail());
        List<UserAuthority> authorities = clienteDTO.getAuthorities().stream()
                .map(UserAuthority::valueOf)
                .collect(Collectors.toList());
        clienteExistente.setAuthorities(authorities);

        clienteService.insertarCliente(clienteExistente);
        return new ResponseEntity<>(convertirAClienteDTO(clienteExistente), HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        logger.log(Level.INFO, "Cliente con ID {0} eliminado", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Métodos de conversión
    private ClienteDTO convertirAClienteDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setUsername(cliente.getUsername());
        dto.setEmail(cliente.getEmail());
        dto.setAuthorities(cliente.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return dto;
    }

    private Cliente convertirACliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setUsername(dto.getUsername());
        cliente.setEmail(dto.getEmail());
        List<UserAuthority> authorities = dto.getAuthorities().stream()
                .map(UserAuthority::valueOf)
                .collect(Collectors.toList());
        cliente.setAuthorities(authorities);
        return cliente;
    }
}
		
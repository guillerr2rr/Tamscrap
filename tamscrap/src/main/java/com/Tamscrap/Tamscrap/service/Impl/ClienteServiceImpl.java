package com.tamscrap.tamscrap.service.Impl;


import com.tamscrap.tamscrap.model.Cliente;
import com.tamscrap.tamscrap.repository.ClienteRepo;
import com.tamscrap.tamscrap.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {


	private final ClienteRepo clienteRepository;

	public ClienteServiceImpl(ClienteRepo clienteRepository) {
		this.clienteRepository = clienteRepository;
	}


	@Override
	public Cliente insertarCliente(Cliente cliente) {
		if (cliente.getUsername() == null || cliente.getUsername().isEmpty()) {
			throw new IllegalArgumentException("El nombre del cliente no puede ser nulo o vacío");
		}

		return clienteRepository.save(cliente);
	}

	@Override
	public List<Cliente> obtenerTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente obtenerPorId(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	public void eliminarCliente(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public Cliente obtenerPorUsername(String username) {
//		return clienteRepository.findByUsername(nombre).orElseThrow();
		return clienteRepository.findByUsername(username).orElse(null);
	}

}

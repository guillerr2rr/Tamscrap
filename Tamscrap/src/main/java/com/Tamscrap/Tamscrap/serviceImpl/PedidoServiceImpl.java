package com.Tamscrap.Tamscrap.serviceImpl;


import com.Tamscrap.Tamscrap.Model.Pedido;
import com.Tamscrap.Tamscrap.repository.PedidoRepo;
import com.Tamscrap.Tamscrap.service.PedidoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {


	private final PedidoRepo pedidoRepository;

	public PedidoServiceImpl(PedidoRepo pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	@Override
	public List<Pedido> obtenerTodos() {
		return pedidoRepository.findAll();
	}

	@Override
	public Pedido obtenerPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}

	@Override
	public Pedido insertarPedido(Pedido ped) {

		ped.calcularPrecio();
		return pedidoRepository.save(ped);
	}

	@Override
	public void eliminarPedido(Long id) {
		pedidoRepository.deleteById(id);
	}

}
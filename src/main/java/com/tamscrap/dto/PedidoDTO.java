 
package com.tamscrap.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PedidoDTO {
    private Long id;
    private double precio;
    private LocalDateTime fechaCreacion;
    private Long clienteId; // Referencia al id del cliente
    private Set<ProductoPedidoDTO> productos;

    public PedidoDTO() {
        productos = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Set<ProductoPedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoPedidoDTO> productos) {
        this.productos = productos;
    }
}

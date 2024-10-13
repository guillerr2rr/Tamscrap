package com.tamscrap.dto;

import com.tamscrap.model.ProductoPedidoId;
import com.tamscrap.model.ProductosPedidos;

import java.util.Objects;

public class ProductoPedidoDTO {
    private Long productoId;  
    private int cantidad;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ProductosPedidos toProductosPedidos(Long pedidoId) {
        ProductosPedidos productosPedidos = new ProductosPedidos();
        ProductoPedidoId id = new ProductoPedidoId(pedidoId, this.productoId);
        productosPedidos.setId(id);
        productosPedidos.setCantidad(this.cantidad);
        // Additional properties can be set here if needed
        return productosPedidos;
    }

    @Override
    public String toString() {
        return "ProductoPedidoDTO{" +
                "productoId=" + productoId +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoPedidoDTO)) return false;
        ProductoPedidoDTO that = (ProductoPedidoDTO) o;
        return cantidad == that.cantidad &&
                Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productoId, cantidad);
    }
}

package com.tamscrap.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Entity
@Table(name = "PEDIDOS")
public class Pedido {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "precio")
    private double precio;

    @Column(name = "fecha")
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = true)
    @JsonIgnore
    private Cliente cliente;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductosPedidos> productos;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido() {
        productos = new HashSet<ProductosPedidos>();
    }

    public Pedido(Cliente a) {
        cliente = a;
        productos = new HashSet<ProductosPedidos>();

    }

    public void addProducto(Producto b, int cantidad) {
        ProductosPedidos b_p = new ProductosPedidos(b, this, cantidad);
        if (productos.contains(b_p)) {
            productos.remove(b_p);
        }
        if (cantidad != 0) {
            productos.add(b_p);
        }
        b.getPedidos().add(b_p);
    }

    public void addProducto2(Producto b, int cantidad) {
        ProductosPedidos b_p = new ProductosPedidos(b, this, cantidad);
        if (productos.contains(b_p)) {
            productos.remove(b_p);
        }
        if (cantidad != 0) {
            productos.add(b_p);
        }
        // b.getPedidos().add(b_p);
    }

    public void removeProducto(Producto b) {
        for (Iterator<ProductosPedidos> iterator = productos.iterator();
             iterator.hasNext(); ) {
            ProductosPedidos b_p = iterator.next();

            if (b_p.getPedido().equals(this) &&
                    b_p.getProducto().equals(b)) {
                iterator.remove();
                b_p.getProducto().getPedidos().remove(b_p);
                b_p.setPedido(null);
                b_p.setProducto(null);
                b_p.setCantidad(0);
            }
        }
    }


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Set<ProductosPedidos> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductosPedidos> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return imprimirProductos();
    }
    public String imprimirProductos() {
        StringBuilder resultado = new StringBuilder("Productos del pedido " + id + "\n");

        if (productos.size() == 0) {
            resultado.append("No hay productos en este pedido.");
        } else {
            for (ProductosPedidos b : productos) {
                Producto producto = b.getProducto();
                int cantidad = b.getCantidad();

                resultado.append(producto.getNombre())
                        .append(" ---> Cantidad: ")
                        .append(cantidad)
                        .append(" | Precio Unitario: ")
                        .append(producto.getPrecio())
                        .append(" € | Total: ")
                        .append(producto.getPrecio() * cantidad)
                        .append(" €\n");
            }
            resultado.append("\n\n");
        }

        return resultado.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido(LocalDateTime fechaCreacion, Cliente cliente) {
        this.fechaCreacion = fechaCreacion;
        this.cliente = cliente;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void calcularPrecio() {
        double resultado = 0.0;

        for (ProductosPedidos b : productos) {
            resultado += b.getProducto().getPrecio() * b.getCantidad();
        }
        precio = resultado;
    }

}

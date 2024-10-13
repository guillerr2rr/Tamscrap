package com.tamscrap.dto;

import java.util.Set;

public class ProductoDTO {

    private Long id;
    private String nombre;
    private double precio;
    private String imagen;
    private boolean lettering;
    private boolean scrapbooking;
    private boolean oferta;
    private Integer descuento;
    private Set<ProductoPedidoDTO> pedidos;  

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isLettering() {
        return lettering;
    }

    public void setLettering(boolean lettering) {
        this.lettering = lettering;
    }

    public boolean isScrapbooking() {
        return scrapbooking;
    }

    public void setScrapbooking(boolean scrapbooking) {
        this.scrapbooking = scrapbooking;
    }

    public boolean isOferta() {
        return oferta;
    }

    public void setOferta(boolean oferta) {
        this.oferta = oferta;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public Set<ProductoPedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<ProductoPedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }
}

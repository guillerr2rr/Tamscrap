package com.tamscrap.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PRODUCTOS")
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre", unique = true, nullable = true)
	private String nombre;

	@Column(name = "precio")
	private double precio;

	@Column(name = "imagen")
	private String imagen;

	@Column(name = "lettering")
	private boolean lettering;

	@Column(name = "scrapbooking")
	private boolean scrapbooking;

	@Column(name = "oferta")
	private boolean oferta;

	@Column(name = "descuento")
	private Integer descuento;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductosPedidos> pedidos = new HashSet<>();

	public Producto() {
	}

	public Producto(String nombre, double precio, String imagen) {
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
	}

	public Producto(String nombre, double precio, String imagen, boolean lettering, boolean scrapbooking,
			boolean oferta, Integer descuento) {
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.lettering = lettering;
		this.scrapbooking = scrapbooking;
		this.oferta = oferta;
		this.descuento = descuento;
		this.pedidos = new HashSet<>();
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", imagen=" + imagen
				+ ", lettering=" + lettering + ", scrapbooking=" + scrapbooking + ", oferta=" + oferta
				+ ", descuento=" + descuento + ", pedidos=" + pedidos + "]";
	}

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

	public Set<ProductosPedidos> getPedidos() {
		return pedidos;
	}

	public void setPedidos(Set<ProductosPedidos> pedidos) {
		this.pedidos = pedidos;
	}

	public double getPrecioFinal() {
		if (oferta && descuento > 0) {
			return precio - (precio * descuento / 100);
		}
		return precio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, precio, imagen, lettering, scrapbooking, oferta, descuento, pedidos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Double.compare(other.precio, precio) == 0 && Objects.equals(imagen, other.imagen)
				&& lettering == other.lettering && scrapbooking == other.scrapbooking && oferta == other.oferta
				&& Objects.equals(descuento, other.descuento) && Objects.equals(pedidos, other.pedidos);
	}
}

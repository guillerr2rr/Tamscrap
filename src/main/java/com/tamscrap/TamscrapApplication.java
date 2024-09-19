package com.tamscrap;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.repository.ProductoRepo;

@SpringBootApplication
public class TamscrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(TamscrapApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClienteRepo clienteRepo, ProductoRepo productoRepo, PedidoRepo pedidoRepo, PasswordEncoder encoder) {
	    return args -> {
	        // Creación de clientes
	        Cliente user1 = new Cliente("user1", encoder.encode("1234"), "user1@localhost",
	                new ArrayList<>(List.of(UserAuthority.ADMIN, UserAuthority.USER)));
	        Cliente user2 = new Cliente("user2", encoder.encode("1234"), "user2@localhost",
	                new ArrayList<>(List.of(UserAuthority.USER)));
	        clienteRepo.save(user1);
	        clienteRepo.save(user2);

	        // Productos de Lettering con descuentos
	        List<Producto> productosLettering = List.of(
	                new Producto("Brush Pen", 3.99, "https://www.milbby.com/cdn/shop/products/33614921212_2_20_1000x1000.jpg?v=1709233766", true, false, true, 10),
	                new Producto("Rotuladores de punta fina", 5.99, "https://master.opitec.com/out/pictures/master/product/1/623095-000-000-VO-01-z.jpg", true, false, false, null),
	                new Producto("Guía de caligrafía", 8.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 15),
	                new Producto("Tinta para Lettering", 4.50, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null),
	                new Producto("Papel para Lettering", 6.00, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 5)
	        );

	        // Productos de Scrapbooking con descuentos
	        List<Producto> productosScrapbooking = List.of(
	                new Producto("Kit de papeles decorativos", 12.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 20),
	                new Producto("Tijeras con formas", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null),
	                new Producto("Pegamento para Scrapbooking", 3.50, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null),
	                new Producto("Troqueladora de formas", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10),
	                new Producto("Stickers decorativos", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null)
	        );

	        // Productos de Papelería General con descuentos
	        List<Producto> productosPapeleria = List.of(
	                new Producto("Cuaderno A5", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5),
	                new Producto("Bolígrafos de colores", 1.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
	                new Producto("Goma de borrar", 0.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
	                new Producto("Bloc de notas adhesivas", 3.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
	                new Producto("Lápices de grafito", 1.50, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 10)
	        );
 
	        productoRepo.saveAll(productosLettering);
	        productoRepo.saveAll(productosScrapbooking);
	        productoRepo.saveAll(productosPapeleria);

	      
	    };
	}


}
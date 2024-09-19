package com.tamscrap.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamscrap.config.SecurityConfig;
import com.tamscrap.model.Producto;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

@Import(SecurityConfig.class)
@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

	private static final String BASE_URL = "http://localhost:8082";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductoServiceImpl productoService;

	@MockBean
	private ClienteRepo clienteRepo;
	@MockBean
	private ClienteServiceImpl clienteService;

	@MockBean
	private ProductoRepo productoRepository;

	@Test
	public void testWhen_guardarProducto_resultIsForbiddenTest() throws Exception {
		final Producto producto = new Producto("nombre", 1.0, "imagen");

		Mockito.when(productoService.insertarProducto(producto)).thenReturn(producto);

		ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/productos/api/productos"));

		resultActions.andExpect(status().isForbidden());
		Mockito.verifyNoInteractions(productoService);
	}

	@Test
	public void guardarProducto_conAutenticacion_debeRetornarOk() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		final Producto producto = new Producto("nombre", 1.0, "imagen");

		Mockito.when(productoService.insertarProducto(producto)).thenReturn(producto);
	    ResultActions resultActions = mockMvc.perform(
		        post("/productos/api/productos")
		            .contentType(APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(producto))
		            .with(user("user1").password("1234").roles("USER", "ADMIN"))
		            .with(csrf())
		    );


		    resultActions
		        .andExpect(status().isOk())
		        .andExpect(result -> {
		            Producto productoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Producto.class);
		            assertThat(productoResponse).usingRecursiveComparison().isEqualTo(producto);
		        });

		    verify(productoService).insertarProducto(producto);
	}

}

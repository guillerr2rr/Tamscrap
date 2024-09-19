package com.tamscrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/login", "/logout", "/index.html", "/entreFechas.html").permitAll()
                        .requestMatchers("/formularioVenta.html").permitAll()
                        .requestMatchers("/conciertos.html", "/formularioComprador.html", "/formularioConcierto.html",
                                "/formularioSala.html", "/salas.html", "/ventas.html").permitAll()
                        .anyRequest().permitAll())
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"))
                .formLogin(form -> form.loginPage("/login").failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password"));

        return httpSecurity.build();
    }
}
 
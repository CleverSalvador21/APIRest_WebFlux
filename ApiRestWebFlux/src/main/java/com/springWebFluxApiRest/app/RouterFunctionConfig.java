package com.springWebFluxApiRest.app;

import com.springWebFluxApiRest.app.document.Product;
import com.springWebFluxApiRest.app.handler.ProductoHandler;
import com.springWebFluxApiRest.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import  org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

   @Bean
    public RouterFunction<ServerResponse> listar(ProductoHandler productoHandler){
      return route(GET("/api/v2/productos").or(GET("/api/v3/productos")), productoHandler::listar)
              .andRoute(GET("/api/v2/productos/{id}").and(contentType(MediaType.APPLICATION_JSON)),productoHandler::ver);
   }
}

package com.springWebFluxApiRest.app.handler;

import com.springWebFluxApiRest.app.document.Product;
import com.springWebFluxApiRest.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {
    @Autowired
    ProductService productService;

    public Mono<ServerResponse> listar(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }
    public Mono<ServerResponse> ver(ServerRequest request){
        String id = request.pathVariable("id");/*recibir po medio del request el id*/
        return productService.findById(id).flatMap( p ->{
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(p))
                    .switchIfEmpty(ServerResponse.notFound().build());
        });
    }
}

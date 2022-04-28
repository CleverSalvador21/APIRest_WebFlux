package com.springWebFluxApiRest.app.service;

import com.springWebFluxApiRest.app.document.Category;
import com.springWebFluxApiRest.app.document.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAll();
    Mono<Product> findById(String id);
    Mono<Product> save(Product product);
    Mono<Void> delete(Product product);
    /*Metodos para la categoria*/
    Flux<Category> findAllCat();
    Mono<Category> saveCat(Category category);
    Mono<Category> findByIdCat(String id);
}

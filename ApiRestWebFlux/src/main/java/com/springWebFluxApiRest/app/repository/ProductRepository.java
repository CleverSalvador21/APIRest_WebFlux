package com.springWebFluxApiRest.app.repository;

import com.springWebFluxApiRest.app.document.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product,String> {
}

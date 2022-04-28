package com.springWebFluxApiRest.app.repository;

import com.springWebFluxApiRest.app.document.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category,String> {
}

package com.springWebFluxApiRest.app.serviceImpl;

import com.springWebFluxApiRest.app.document.Category;
import com.springWebFluxApiRest.app.document.Product;
import com.springWebFluxApiRest.app.repository.CategoryRepository;
import com.springWebFluxApiRest.app.repository.ProductRepository;
import com.springWebFluxApiRest.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository pr;
    @Autowired
    private CategoryRepository cr;

    @Override
    public Flux<Product> findAll() {
        return pr.findAll();
    }

    @Override
    public Mono<Product> findById(String id) {
        return pr.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return pr.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return pr.delete(product);
    }

    @Override
    public Flux<Category> findAllCat() {
        return cr.findAll();
    }

    @Override
    public Mono<Category> saveCat(Category category) {
        return cr.save(category);
    }

    @Override
    public Mono<Category> findByIdCat(String id) {
        return cr.findById(id);
    }
}

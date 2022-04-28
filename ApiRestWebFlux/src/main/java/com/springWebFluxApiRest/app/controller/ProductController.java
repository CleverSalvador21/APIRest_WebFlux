package com.springWebFluxApiRest.app.controller;

import com.springWebFluxApiRest.app.document.Product;
import com.springWebFluxApiRest.app.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
     private ProductService productService;
    @Value("${config.uploads.path}")
    String path;
    @PostMapping("/v2")
    public Mono<ResponseEntity<Product>> saveToImage(Product product,@RequestPart FilePart filePart){
        if(product.getFecha()== null){
            product.setFecha(new Date());
        }
        product.setFoto(UUID.randomUUID().toString()+"-"+filePart.filename()
                .replace(" ","")
                .replace(":","")
                .replace("//",""));

        return filePart.transferTo(new File(path+product.getFoto())).then(productService.save(product))
                .map(p -> ResponseEntity.created(URI.create("/api/products".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }
    @PostMapping("/uploads/{id}")
    public Mono<ResponseEntity<Product>> upload(@PathVariable String id,@RequestPart FilePart filePart){
        return productService.findById(id).flatMap(p ->{
            p.setFoto(UUID.randomUUID().toString() + "-" + filePart.filename()
                    .replace(" ","")
                    .replace(":","")
                    .replace("//",""));
            return filePart.transferTo(new File(path+p.getFoto())).then(productService.save(p));
        }).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> findAll(){
        log.info("Entrando al metodo");
        return Mono.just(ResponseEntity.ok()/*Manejamos el status 200*/
                .contentType(MediaType.APPLICATION_JSON)/*Manejamos el contenido del cuerpo - JSON*/
                .body(productService.findAll()));/*Dentro del cuerpo colocamos el flujo*/
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable String id){
        return productService.findById(id).map(p -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
     @PostMapping
    public Mono<ResponseEntity<Product>> save(@RequestBody Product product){
         if(product.getFecha()== null){
             product.setFecha(new Date());
         }
        return productService.save(product).map(p -> ResponseEntity
                .created(URI.create("/api/products/".concat(p.getId())))/*status created es 201*/
                .contentType(MediaType.APPLICATION_JSON)
                .body(p));
     }
     /*Metodo para validar el post del registro del producto*/
     @PostMapping("/v3")
     public Mono<ResponseEntity<Map<String,Object>>> savev3(@Valid @RequestBody Mono<Product> productMono){
         Map<String,Object> respuesta = new HashMap<String,Object>();
         return productMono.flatMap(product ->{
             if(product.getFecha() == null){
                 product.setFecha(new Date());
             }
             return productService.save(product).map(c ->{
                 respuesta.put("product",c);
                 respuesta.put("message","Producto no encontrado");
                 return ResponseEntity
                         .created(URI.create("/api/products/".concat(c.getId())))
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(respuesta);
             });
         }).onErrorResume(t ->{
             return Mono.just(t).cast(WebExchangeBindException.class)
                     .flatMap(e -> Mono.just(e.getFieldErrors()))
                     .flatMapMany(errors -> Flux.fromIterable(errors))
                     .map(e -> "El campo" + e.getField() + " " + e.getDefaultMessage())
                     .collectList()
                     .flatMap(list ->{
                         respuesta.put("errors",list);
                         respuesta.put("status",HttpStatus.BAD_REQUEST.value());
                         return Mono.just(ResponseEntity.badRequest().body(respuesta));
                     });
         });
     }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> update(@RequestBody Product product, @PathVariable String id){
        return productService.findById(id).flatMap(p ->{
            p.setNombre(p.getNombre());
            p.setPrecio(p.getPrecio());
            p.setCategory(p.getCategory());
            p.setFecha(new Date());
            return productService.save(p); /*retornamos un mono producto*/
        }).map(p -> ResponseEntity.created(URI.create("/api/products/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        return productService.findById(id).flatMap(p->{
            return productService.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
/*version 02 de listar por ID*/
/*@GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Product>>> finById(@PathVariable(name = "id") String id){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findById(id)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }*/
package com.springWebFluxApiRest.app;

import com.springWebFluxApiRest.app.document.Category;
import com.springWebFluxApiRest.app.document.Product;
import com.springWebFluxApiRest.app.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;
@EnableEurekaClient
@SpringBootApplication
public class ApiRestWebFluxApplication implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(ApiRestWebFluxApplication.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private ReactiveMongoTemplate rmt;
	public static void main(String[] args) {
		SpringApplication.run(ApiRestWebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		rmt.dropCollection("Product").subscribe();
		rmt.dropCollection("category").subscribe();

		Category electronica = new Category(null,"Electronica");
		Category deporte = new Category(null,"Deporte");
		Category mueble = new Category(null,"Mueble");
		Category computacion = new Category(null,"Computacion");



		Flux.just(electronica,deporte,mueble,computacion)
				.flatMap(c -> productService.saveCat(c))
				.doOnNext(c -> log.info("Categoria creada :" + c.getNombre()))
				.thenMany(Flux.just(
								new Product(null,"tv" , 123.23,null,electronica,null),
								new Product(null,"Mouse",456.23,null,computacion,null),
								new Product(null,"Laptop",456.23,null,computacion,null),
								new Product(null,"Bicicleta",456.23,null,deporte,null),
								new Product(null,"Cajon de madera",456.23,null,mueble,null),
								new Product(null,"Computadora",456.23,null,computacion,null),
								new Product(null,"Circuito xp",456.23,null,electronica,null),
								new Product(null,"Motor gpr", 1300.00,null,electronica,null))
						.flatMap(producto ->{
							producto.setFecha(new Date());
							return productService.save(producto);
						}))
				.subscribe(e -> log.info("Producto Ingresado : " + e.getNombre() + "Precio: " + e.getPrecio()
				));
	}
}
/*new Product(null,"tv" , 123.23,null,electronica,null),
		new Product(null,"Mouse",456.23,null,computacion,null),
		new Product(null"Laptop",456.23,computacion),
		new Product("Bicicleta",456.23,deporte),
		new Product("Cajon de madera",456.23,mueble),
		new Product("Computadora",456.23,computacion),
		new Product("Circuito xp",456.23,electronica),
		new Product("Motor gpr", 1300.00,electronica))*/
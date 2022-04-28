package com.springWebFluxApiRest.app.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
@AllArgsConstructor
@Document(collection = "Product")
public class Product {
    @Id
    private String id;
    @NotEmpty
    private String nombre;
    @NotNull
    private Double precio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    @Valid
    private Category category;

    private String foto;


}

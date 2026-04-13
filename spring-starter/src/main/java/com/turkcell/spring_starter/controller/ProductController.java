package com.turkcell.spring_starter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ProductCreatedResponse;
import com.turkcell.spring_starter.dto.ProductForCreateDto;
import com.turkcell.spring_starter.model.Product;
import com.turkcell.spring_starter.service.ProductServiceImpl;

// Altın kural: Veritabanı nesneleri requestte de responseda da kullanılamaz. 
@RestController // Uygulamada gerektiğinde controlleri newle.
@RequestMapping("/api/product") 
public class ProductController {
    // In-Memory Çalış..
    private List<Product> productList = new ArrayList<>();


    @GetMapping()
    public List<Product> getAllProducts() {
        // Veritabanındaki Product nesnelerini listele.
        return productList;
    }
    //private final ProductServiceImpl productServiceImpl = new ProductServiceImpl();
    private final ProductServiceImpl productServiceImpl;

    @GetMapping("{id}")
    public Product getProductById(@PathVariable int id) 
    {
        // Listeden id == product.getId() ise onu yoksa null dön.
        return productList.stream().filter(i->i.getId() == id).findFirst().orElse(null);
    }

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }


    // Request-Response Pattern =>
    // Her istek-cevap kendine has bir modele sahip olmak zorunda!
    // Birebir başka bir istek-cevap çiftiyle aynı içeriğe sahip olsa dahil!
    @PostMapping
    public ProductCreatedResponse createProduct(@RequestBody ProductForCreateDto productDto) {
        // Veritabanına product nesnesini ekle..

        // Sen dışardan ProductForCreateDto alıyosun 
        // ama veritabanı Product ile çalışıyor
        if(productDto.getPrice() < 0)
            throw new RuntimeException("Para 0'dan küçük olamaz.");

        // Transfer => MANUAL MAPPING
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setId(new Random().nextInt(999));

        productList.add(product);

        // Domain Nesnesi -> Dto
        ProductCreatedResponse response = new ProductCreatedResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;

        // Ben controller olarak iş kodu çalıştıramam, ama bunu yapmam gerekli..
        // İş kodunu çalıştıracak olan yapıya BAĞIMLIYIM.
        // Bağımlılık Enjeksiyonu -> Dependency Injection
    }
    @PutMapping
    public void updateProduct(@RequestBody Product product) {
        ///..
        Product productToUpdate = productList.stream().filter(p -> p.getId() == product.getId()).findFirst().orElseThrow();

        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());
    }
    @DeleteMapping("{id}")
    public void deleteProduct(@PathVariable int id) {
        ///.. Todo..
    }

    public ProductCreatedResponse create(@RequestBody ProductForCreateDto productDto) {
        return this.productServiceImpl.create(productDto);
    }

}

// DTO => Data Transfer Object
// Entity ile X (controller,service) arası veri transferi için oluşturulan sınıflardır.
package com.turkcell.spring_starter.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.model.Product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping("/api/product") // bu controllera gelen istekler /api/product ile baslamali

public class ProductController {
    // kullancii ne zaman /api/product alanind istek atarsa bu fonkstondan donen cevap olsun.
    // api/proccut -> sayHi(); match
    // http method -> GET POST PUT DELETE PATCH

//http://localhost:8080/api/product?name=osman query string denir buna. Paramtreleri & ile ayrilir.
    @GetMapping("") // controllerin uzantisi +get in uzantisi ->/api/product
    public String sayHi(String name,int age){
        return "Hi, " + name + "You are " + age + " years old.";
    }

    //conrollerin uzantisi +geti in uzantisi -> /api/produc/hello
    @GetMapping("hello/{name}/{age}")
    public String sayHello(@PathVariable String name,@PathVariable int age){// ben bu name i path variable olarak almak istiyorum. /api/product/hello/osman
        return "Hello "+ name+ "yasiniz: "+age;
    }

    @PostMapping
    public Product add(@RequestBody Product product){ // JSON -> Java objesine
        return product;
    }
}

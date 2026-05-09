package com.turkcell.spring_starter.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;


@Entity
@Table(name="products")
public class Product {
    @Id
    // @GeneratedValue(strategy=GenerationType.IDENTITY) -> 1 er 1 er artan strateji

    private UUID id;

}

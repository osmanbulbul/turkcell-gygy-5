package com.turkcell.spring_starter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.spring_starter.entity.Product;

// JpaRepository<Entity, PrimaryKeyType>
// findAll, findById, save, deleteById gibi tüm temel metodları ücretsiz sağlar
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
package com.fiec.lpiiiback.models.repositories;

import com.fiec.lpiiiback.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByNameAndId (String name, Integer id);

}

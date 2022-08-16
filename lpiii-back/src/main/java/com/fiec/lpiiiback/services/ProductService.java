package com.fiec.lpiiiback.services;

import com.fiec.lpiiiback.models.entities.Product;

import java.util.List;

public interface ProductService {

    Product getProduct(String productId);

    List<Product> getAllProducts();

    Product signUpProduct(String name, Float price, String productfilename);

    Product updateProduct(Integer productId, String name, Float price );

    void deleteProduct(Integer productId);

    void assignProductImage(Integer productId, String productfilename);


}

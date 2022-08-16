package com.fiec.lpiiiback.services.impl;

import com.fiec.lpiiiback.models.entities.Product;
import com.fiec.lpiiiback.models.repositories.ProductRepository;
import com.fiec.lpiiiback.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;


    @Override
    public Product getProduct(String productId) {
        return productRepository.findById(Integer.parseInt(productId)).orElseThrow();
    }

    @Override
    public List<Product> getAllProducts() { return productRepository.findAll(); }

    @Override
    public Product signUpProduct(String name, Float price, String productfilename) {
        return productRepository.save(
                Product.builder()
                        .name(name)
                        .price(price)
                        .productImage(productfilename)
                        .build()
        );
    }

    @Override
    public Product updateProduct(Integer productId, String name, Float price) {
        Product currentProduct = productRepository.findById(productId).orElseThrow();
        currentProduct.setName(name);
        currentProduct.setPrice(price);
        return productRepository.save(currentProduct);
    }

    @Override
    public void deleteProduct(Integer productId) { productRepository.deleteById(productId); }

    @Override
    public void assignProductImage(Integer productId, String productfilename) {

        Product currentProduct = productRepository.findById(productId).orElseThrow();
        currentProduct.setProductImage(productfilename);
        productRepository.save(currentProduct);

    }
}

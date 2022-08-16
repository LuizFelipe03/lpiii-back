package com.fiec.lpiiiback.models.dto;

import com.fiec.lpiiiback.models.entities.Product;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductDto {

    Integer id;
    String name;
    Float price;
    String productImage;


    public static ProductDto convertToProductDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productImage(product.getProductImage())
                .build();
    }


}

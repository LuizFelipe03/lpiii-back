package com.fiec.lpiiiback.models.dto;

import lombok.Data;

@Data
public class CreateProductRequestDto {
    String name;
    Float price;
    String productImage;
}

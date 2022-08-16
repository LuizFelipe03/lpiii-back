package com.fiec.lpiiiback.controllers;

import com.fiec.lpiiiback.models.dto.CreateProductRequestDto;
import com.fiec.lpiiiback.models.dto.ProductDto;
import com.fiec.lpiiiback.models.entities.Product;
import com.fiec.lpiiiback.services.ProductService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {


    @Autowired
    ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(){

        return productService.getAllProducts().stream().map(ProductDto::convertToProductDto).collect(Collectors.toList());
    }

    public ProductDto signUpProduct(@RequestBody CreateProductRequestDto createProductRequestDto){

        return ProductDto.convertToProductDto(productService.signUpProduct(
                createProductRequestDto.getName(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getProductImage()
        ));
    }


    @GetMapping("/{products}")
    public ProductDto getProduct(@PathVariable("productId") String productId){

        return ProductDto.convertToProductDto(productService.getProduct(productId));
    }

    @PutMapping("/{products}")
    public ProductDto editProduct(@RequestBody CreateProductRequestDto createProductRequestDto, @PathVariable("/{products}") Integer productId){
        return ProductDto.convertToProductDto(productService.updateProduct(productId,
                createProductRequestDto.getName(),
                createProductRequestDto.getPrice()
        ));

    }

    @DeleteMapping("/{products}")
    public void deleteProduct(@PathVariable ("/{products}") Integer productId){
        productService.deleteProduct(productId);
    }


    @PutMapping(value = "/image/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void assignImageToProduct(@PathVariable("productId") Integer productId, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String productImage = UUID.randomUUID() + "_" + Long.toHexString(new Date().getTime());

        Path productfilename = Paths.get("uploads").resolve(productImage);
        Path thumbFilename = Paths.get("uploads").resolve("thumb_" + productImage);
        Thumbnails.of(multipartFile.getInputStream())
                .size(500, 500)
                .outputFormat("jpg")
                .toFile(new File(productfilename.toString()));
        Thumbnails.of(multipartFile.getInputStream())
                .size(100, 100)
                .outputFormat("jpg")
                .toFile(new File(thumbFilename.toString()));
        productService.assignProductImage(productId, productImage);
    }

    @PostMapping(value="/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createBulkOfProducts(@RequestParam("csvFile") MultipartFile multipartFile ) throws IOException, CsvException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), "UTF-8"));
        final int NAME=0, PRICE=1;
        try (Reader reader = fileReader) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                List<String[]> csvFields = csvReader.readAll();
                for (int i = 1; i < csvFields.size(); i++) {
                    Product newProduct = Product.builder()
                            .name(csvFields.get(i)[NAME])
                            .price(Float.valueOf(csvFields.get(i)[PRICE]))
                            .build();
                    productService.signUpProduct(newProduct.getName(), newProduct.getPrice(), newProduct.getProductImage());
                }

            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

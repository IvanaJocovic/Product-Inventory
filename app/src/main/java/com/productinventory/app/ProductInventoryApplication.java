package com.productinventory.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication(scanBasePackages = "com.productinventory")
@EntityScan(basePackages = "com.productinventory.domain.model")
public class ProductInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductInventoryApplication.class, args);
    }
}

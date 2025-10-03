package com.example.ecommerce.config;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.search.LuceneIndexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner load(ProductRepository repo, LuceneIndexService indexService) {
        return args -> {
            // sample products
            Product p1 = new Product(null, "Galaxy S21", "Smartphone Samsung Galaxy S21 with excellent camera", 799.0);
            Product p2 = new Product(null, "iPhone 13", "Apple smartphone with A15 chip", 999.0);
            Product p3 = new Product(null, "MacBook Pro", "Apple MacBook Pro M1 16-inch laptop", 1999.0);
            Product p4 = new Product(null, "Wireless Mouse", "Ergonomic wireless mouse", 29.99);

            p1 = repo.save(p1);
            p2 = repo.save(p2);
            p3 = repo.save(p3);
            p4 = repo.save(p4);

            // index them
            indexService.indexProduct(p1);
            indexService.indexProduct(p2);
            indexService.indexProduct(p3);
            indexService.indexProduct(p4);
        };
    }
}

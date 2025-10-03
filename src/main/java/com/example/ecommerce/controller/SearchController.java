package com.example.ecommerce.controller;

import com.example.ecommerce.dto.SearchEvent;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.search.LuceneIndexService;
import com.example.ecommerce.config.KafkaConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final LuceneIndexService lucene;
    private final KafkaTemplate<String, SearchEvent> kafka;
    private final ProductRepository repo;

    public SearchController(LuceneIndexService lucene, KafkaTemplate<String, SearchEvent> kafka, ProductRepository repo) {
        this.lucene = lucene;
        this.kafka = kafka;
        this.repo = repo;
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam("q") String q, @RequestParam(value = "limit", defaultValue = "10") int limit) throws Exception {
        List<Long> ids = lucene.search(q, limit);
        List<Product> products = repo.findAllById(ids);
        // publish event
        SearchEvent ev = new SearchEvent(q, System.currentTimeMillis(), products.size());
        kafka.send(KafkaConfig.SEARCH_TOPIC, ev);
        // preserve order of ids
        Map<Long, Product> map = products.stream().collect(Collectors.toMap(Product::getId, p -> p));
        List<Product> ordered = new ArrayList<>();
        for (Long id : ids) if (map.containsKey(id)) ordered.add(map.get(id));
        return ordered;
    }
}

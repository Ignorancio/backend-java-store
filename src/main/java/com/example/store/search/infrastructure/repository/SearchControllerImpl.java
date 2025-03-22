package com.example.store.search.infrastructure.repository;

import com.example.store.product.domain.Product;
import com.example.store.search.application.SearchServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchControllerImpl {

    private final SearchServiceImpl searchService;

    @GetMapping("/")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String name){
        return ResponseEntity.status(HttpStatus.OK).body(searchService.searchProduct(name));
    }
}

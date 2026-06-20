package dev.aj.product_service.controller;

import dev.aj.order_service.model.product.ProductStatus;
import dev.aj.product_service.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${product_service.uri}")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductStatus> getProductStatus(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
}

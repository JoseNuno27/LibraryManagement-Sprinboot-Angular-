package com.inn.library.services;

import com.inn.library.models.Product;
import com.inn.library.repositories.ProductRepository;
import com.inn.library.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    public ResponseEntity<?> getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        }
        return ResponseEntity.status(404).body("Product not found");
    }

    public ResponseEntity<?> addProduct(Product product) {
        if (product.getCategory() == null || !categoryRepository.existsById(product.getCategory().getId())) {
            return ResponseEntity.badRequest().body("Category not found");
        }
        Product newProduct = productRepository.save(product);
        return ResponseEntity.ok(newProduct);
    }

    public ResponseEntity<?> updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (productDetails.getCategory() != null && categoryRepository.existsById(productDetails.getCategory().getId())) {
                product.setTitle(productDetails.getTitle());
                product.setAuthor(productDetails.getAuthor());
                product.setEditor(productDetails.getEditor());
                product.setCategory(productDetails.getCategory());
                Product updatedProduct = productRepository.save(product);
                return ResponseEntity.ok(updatedProduct);
            }
            return ResponseEntity.badRequest().body("Category not found");
        }
        return ResponseEntity.status(404).body("Product not found");
    }

    public ResponseEntity<String> deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.status(404).body("Product not found");
    }
}

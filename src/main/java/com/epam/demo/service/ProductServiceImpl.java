package com.epam.demo.service;

import com.epam.demo.entity.Product;
import com.epam.demo.exception.ResourceNotFoundException;
import com.epam.demo.repository.ProductRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;

    public ProductServiceImpl(ProductRepository productRepository, MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void initMetrics() {
        meterRegistry.gauge("products.total", productRepository.count());
    }

    @Override
    public List<Product> getAllProducts() {
        meterRegistry.counter("products.read.total").increment();
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);
        meterRegistry.counter("products.created.total").increment();
        return saved;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
package com.infy.os.product.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.infy.os.product.dto.ProductDTO;
import com.infy.os.product.service.ProductService;

@RestController
public class ProductController {

	

	@Autowired
	ProductService productService;

	// 1 Fetches all product details
	@GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getAllProducts() {
		
		return productService.getAllProducts();
	}

	// 2 Add a product
	@PostMapping(value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
	
		String message = productService.addProduct(productDTO);
		return new ResponseEntity<>(message, HttpStatus.CREATED);

	}

	// 3 Fetches all products by category
	@GetMapping(value = "/products/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getProductsByCategory(@PathVariable String category) {
		
		return productService.searchProductsByCategory(category);
	}
	
	@GetMapping(value = "/products/{prodId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSpecificProductByProdId(@PathVariable Long prodId) {
		ProductDTO pDto= productService.searchProductsByProductId(prodId);
		return pDto.getProductName();
	}

	// 4 Fetches all products by product name
	@GetMapping(value = "/products/name/{productName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductDTO> getProductsByProductName(@PathVariable String productName) {
	
		return productService.searchProductsByProductName(productName);
	}

	// 5 Delete a product by product Id
	@DeleteMapping(value = "/products/productId/{prodId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteProduct(@PathVariable Long prodId) {
		
		boolean flag = productService.deleteProduct(prodId);
		if (flag)
			return new ResponseEntity<String>("Product successfully deleted", HttpStatus.OK);
		return new ResponseEntity<>("Product does not exist", HttpStatus.OK);

	}

	// 6 delete product by seller Id
	@DeleteMapping(value = "/products/seller/{sellerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteProductBySellerId(@PathVariable Long sellerId) {
		
		boolean flag = productService.deleteProductBySellerId(sellerId);
		if (flag)
			return new ResponseEntity<>("Deleted succesfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("No products exist in products tables", HttpStatus.OK);
	}

	// 7 get stock of a product
	@GetMapping(value = "/products/{prodId}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getStock(@PathVariable Long prodId) {
	
		Long stock = productService.getStock(prodId);
		if (stock == -1L)
			return new ResponseEntity<>("Product does not exist", HttpStatus.OK);
		return new ResponseEntity<>("Stock of Product " + prodId + " is " + stock, HttpStatus.OK);
	}

	// 8 Update stock Request
	@PutMapping(value = "/products/{prodId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateStock(@RequestBody ProductDTO productDTO, @PathVariable Long prodId) {
		
		boolean flag = productService.updateStock(prodId, productDTO.getStock());
		if (flag)
			return new ResponseEntity<String>("Update of stock successful", HttpStatus.OK);
		return new ResponseEntity<String>("Insufficient stock OR Product does not exist", HttpStatus.OK);

	}

}

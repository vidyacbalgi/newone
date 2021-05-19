package com.infy.os.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.infy.os.product.dto.ProductDTO;
import com.infy.os.product.entity.Product;
import com.infy.os.product.repository.ProductRepository;
import com.infy.os.product.validator.ProductValidator;

@Service
@Transactional
public class ProductService {



	@Autowired
	ProductRepository productRepository;

	// get all product details
	public List<ProductDTO> getAllProducts() {
	
		List<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : products) {
			ProductDTO productDTO = ProductDTO.valueOf(product);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}

	// search by category
	public List<ProductDTO> searchProductsByCategory(String category) {
	
		List<Product> products = productRepository.findByCategoryContaining(category);
		System.out.println(products);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : products) {
			ProductDTO productDTO = ProductDTO.valueOf(product);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}
	
	// search by product name
	public List<ProductDTO> searchProductsByProductName(String productName) {
	
		List<Product> products = productRepository.findByProductNameContaining(productName);
		List<ProductDTO> productDTOs = new ArrayList<>();
		for (Product product : products) {
			ProductDTO productDTO = ProductDTO.valueOf(product);
			productDTOs.add(productDTO);
		}
		return productDTOs;
	}
	
	// search by product id
		public ProductDTO searchProductsByProductId(Long prodId) {
			
			Optional<Product> products = productRepository.findByProdId(prodId);
			if(products.isPresent()) {
				Product prod=products.get();
				ProductDTO pdto=ProductDTO.valueOf(prod);
				return pdto;
			}
			return null;

		}
	
	// add product
	public String addProduct(ProductDTO productDTO) {
		
		String message = ProductValidator.validateProduct(productDTO);
		if (message.equalsIgnoreCase("ok")) {
			Product product = productDTO.createEntity();
			productRepository.save(product);
			return "Product added successfully";
		}
		return message;
	}

	// delete product
	public Boolean deleteProduct(Long prodId) {
		
		Optional<Product> optionalProduct = productRepository.findById(prodId);
		if (optionalProduct.isPresent()) {
			productRepository.deleteById(prodId);
			return true;
		}
		return false;
	}
	
	// update stock of a product
	public Boolean updateStock(Long prodId, Long stock) {
		
		Optional<Product> optionalProduct = productRepository.findById(prodId);
		if (stock<10)
			return false;
		else if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			ProductDTO productDTO = ProductDTO.valueOf(product);
			productDTO.setStock(stock);
			Product newProduct = productDTO.createEntity();
			productRepository.save(newProduct);
			return true;
		}
		return false;
	}
	
	// get stock of a product
	public Long getStock(Long prodId) {
	
		Long stock=-1L;
		Optional<Product> optionalProduct = productRepository.findByProdId(prodId);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			
			ProductDTO productDTO = ProductDTO.valueOf(product);
			stock = productDTO.getStock();
		}
		return stock;
	}
	
	// delete product by seller Id
	public Boolean deleteProductBySellerId(Long sellerId) {
	
		List<Product> products = productRepository.findBySellerId(sellerId);
		if (!products.isEmpty()) {
			productRepository.deleteBySellerId(sellerId);
			return true;
		}
		return false;
	}

}

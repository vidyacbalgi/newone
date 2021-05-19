package com.infy.os.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.os.product.dto.BuyerDTO;
import com.infy.os.product.dto.SubscribedProductDTO;
import com.infy.os.product.entity.Product;
import com.infy.os.product.entity.SubscribedProduct;
import com.infy.os.product.repository.ProductRepository;
import com.infy.os.product.repository.SubscribedProductRepository;

@Service
@Transactional
public class SubscribedProductService {



	@Autowired
	SubscribedProductRepository subscribedProductRepository;
	
	@Autowired
	ProductRepository productRepository;

	public List<SubscribedProductDTO> getAllSubscribedProducts() {
		List<SubscribedProduct> subscribedProducts = subscribedProductRepository.findAll();
		List<SubscribedProductDTO> subscribedProductDTOs = new ArrayList<>();

		for (SubscribedProduct subscribedProduct : subscribedProducts) {
			SubscribedProductDTO subscribedProductDTO = SubscribedProductDTO.valueOf(subscribedProduct);
			subscribedProductDTOs.add(subscribedProductDTO);
		}

		
		return subscribedProductDTOs;
	}

	public List<SubscribedProductDTO> getAllSubscribedProductsByBuyerId(Long buyerId) {
		List<SubscribedProduct> subscribedProducts = subscribedProductRepository.findByBuyerId(buyerId);
		List<SubscribedProductDTO> subscribedProductDTOs = new ArrayList<>();

		for (SubscribedProduct subscribedProduct : subscribedProducts) {
			SubscribedProductDTO subscribedProductDTO = SubscribedProductDTO.valueOf(subscribedProduct);
			subscribedProductDTOs.add(subscribedProductDTO);
		}
		
		return subscribedProductDTOs;
	}

	public String addSubscription(BuyerDTO buyerDTO, SubscribedProductDTO subscribedProductDTO) {
		if (buyerDTO.getIsPrivileged()==null || buyerDTO.getIsPrivileged()==0) {
			
			return "You're not a privileged buyer";
		}
		Optional<SubscribedProduct> optionalProduct = subscribedProductRepository.findByBuyerIdAndProdId(subscribedProductDTO.getBuyerId(),subscribedProductDTO.getProdId());
		Optional<Product> optProduct = productRepository.findById(subscribedProductDTO.getProdId());
		if (optionalProduct.isPresent()) {
			
			return "Product is already subscribed by buyer";
		}else if (optProduct.isEmpty()) {
		
			return "Product does not exist";
		}
		else {
			SubscribedProduct subscribedProduct = subscribedProductDTO.createEntity();
			subscribedProductRepository.save(subscribedProduct);
		
			return "Subscribed successfully";
		}
	}
	public String deleteSubscription(Long subId) {
		Optional<SubscribedProduct> optionalProduct = subscribedProductRepository.findById(subId);
		if (optionalProduct.isPresent()) {
			subscribedProductRepository.deleteById(subId);
			
			return ("Deleted subscription");
		}
	
		return "No such subscription exist";
	}
}

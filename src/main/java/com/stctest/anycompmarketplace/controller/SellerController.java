package com.stctest.anycompmarketplace.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.response.BaseResponse;
import com.stctest.anycompmarketplace.service.UserService;

@RestController
@RequestMapping("/sellers")
@PreAuthorize("hasRole('SELLER')")
public class SellerController {
    
    @Autowired
	private UserService userService;

	@GetMapping("{sellerId}/items")
    public BaseResponse getItemsBySeller(@PathVariable("sellerId") Long sellerId) {
		return new BaseResponse<>(userService.getItemsBySeller(sellerId));
	}

    @PostMapping("{sellerId}/items")
    public BaseResponse addItemBySeller(@PathVariable("sellerId") Long sellerId,
        @RequestParam("name") String name, @RequestParam("description") String description,
        @RequestParam("price") BigDecimal price, @RequestParam("quantity") Integer quantity) {
        userService.addItemBySeller(sellerId, name, description, price, quantity);
		return new BaseResponse<>();
	}
}

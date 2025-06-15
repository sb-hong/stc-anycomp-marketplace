package com.stctest.anycompmarketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.response.BaseResponse;
import com.stctest.anycompmarketplace.service.UserService;

@RestController
@RequestMapping("/buyers")
@PreAuthorize("hasRole('BUYER')")
public class BuyerController {
    
    @Autowired
	private UserService userService;

    @PostMapping("{buyerId}/purchase")
    public BaseResponse buyerPurchaseItem(@PathVariable("buyerId") Long buyerId,
            @RequestParam("itemId") Long itemId, @RequestParam("quantity") Integer quantity) {
        userService.buyerPurchaseItem(buyerId, itemId, quantity);
		return new BaseResponse<>();
	}
    

}

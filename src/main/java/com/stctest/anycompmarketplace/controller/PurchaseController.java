package com.stctest.anycompmarketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.response.BaseResponse;
import com.stctest.anycompmarketplace.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    
    @Autowired
	private PurchaseService purchaseService;

    

}

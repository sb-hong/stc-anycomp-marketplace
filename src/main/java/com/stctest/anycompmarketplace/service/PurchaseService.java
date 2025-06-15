package com.stctest.anycompmarketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stctest.anycompmarketplace.enums.ErrorCode;
import com.stctest.anycompmarketplace.exception.CustomFailureException;
import com.stctest.anycompmarketplace.repository.PurchaseRepository;

@Service
public class PurchaseService {
    
    @Autowired
    private PurchaseRepository purchaseRepository;

    


}

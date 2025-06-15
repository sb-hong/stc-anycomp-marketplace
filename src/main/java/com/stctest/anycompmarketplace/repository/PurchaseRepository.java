package com.stctest.anycompmarketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stctest.anycompmarketplace.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}

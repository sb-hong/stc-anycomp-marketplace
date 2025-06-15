package com.stctest.anycompmarketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stctest.anycompmarketplace.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);

}

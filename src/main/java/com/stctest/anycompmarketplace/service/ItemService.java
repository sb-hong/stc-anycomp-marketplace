package com.stctest.anycompmarketplace.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.stctest.anycompmarketplace.entity.Item;
import com.stctest.anycompmarketplace.repository.ItemRepository;
import com.stctest.anycompmarketplace.utils.PageableGenerator;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    // @Autowired
    // private SellerRepository sellerRepository;

    public List<Item> itemList(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageElement = PageableGenerator.gen(pageNum, pageSize, sortBy);

        Page<Item> allItems = itemRepository.findAll(pageElement);

        return allItems.hasContent() ? allItems.getContent() : Collections.emptyList();
    }
    
    public Item getItem(Long id) {
        Optional<Item> foundItem = itemRepository.findById(id);
        if (foundItem.isPresent()) {
            return foundItem.get();
        }
        return null;
    }

    public void updateItem(Long id, String name, String description, BigDecimal price, Integer quantity) {
        Item foundItem = getItem(id);
        foundItem.setName(name);
        if(description != null) {
            foundItem.setDescription(description);
        }
        if(price != null) {
            foundItem.setPrice(price);
        }
        if(quantity != null) {
            foundItem.setQuantity(quantity);
        }

        itemRepository.save(foundItem);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

}

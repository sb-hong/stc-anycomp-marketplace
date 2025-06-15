package com.stctest.anycompmarketplace.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.response.BaseResponse;
import com.stctest.anycompmarketplace.service.ItemService;

@RestController
@RequestMapping("/items")
@PreAuthorize("hasAnyRole('ADMIN','SELLER')")
public class ItemController {
    
    @Autowired
	private ItemService itemService;

	@GetMapping
	public BaseResponse itemList(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
			    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
                @RequestParam(value = "sort_by", required = false) String sortBy) {
		return new BaseResponse<>(itemService.itemList(pageNum, pageSize, sortBy));
	}

    @GetMapping("{id}")
    public BaseResponse getItem(@PathVariable("id") Long id) {
		return new BaseResponse<>(itemService.getItem(id));
	}

    @PutMapping("{id}")
    public BaseResponse updateItem(@PathVariable("id") Long id, @RequestParam("name") String name,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "price", required = false) BigDecimal price,
        @RequestParam(value = "quantity", required = false) Integer quantity) {
        itemService.updateItem(id, name, description, price, quantity);
		return new BaseResponse<>();
	}

    @DeleteMapping("{id}")
    public BaseResponse deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);
		return new BaseResponse<>();
	}
}

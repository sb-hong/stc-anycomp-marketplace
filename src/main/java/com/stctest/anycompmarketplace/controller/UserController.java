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
import com.stctest.anycompmarketplace.service.UserService;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    @Autowired
	private UserService userService;

	@GetMapping
	public BaseResponse userList(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
			    @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
                @RequestParam(value = "sort_by", required = false) String sortBy) {
		return new BaseResponse<>(userService.userList(pageNum, pageSize, sortBy));
	}

    @GetMapping("{id}")
    public BaseResponse getUser(@PathVariable("id") Long id) {
		return new BaseResponse<>(userService.getUser(id));
	}

    @PutMapping("{id}")
    public BaseResponse updateUser(@PathVariable("id") Long id, @RequestParam("email") String email, @RequestParam(value = "username", required = false) String username) {
        userService.updateUser(id, email, username);
		return new BaseResponse<>();
	}

	
    @DeleteMapping("{id}")
    public BaseResponse deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
		return new BaseResponse<>();
	}

}

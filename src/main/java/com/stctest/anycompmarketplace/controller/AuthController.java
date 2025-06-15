package com.stctest.anycompmarketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.enums.RoleType;
import com.stctest.anycompmarketplace.other.JwtBlackListService;
import com.stctest.anycompmarketplace.response.BaseResponse;
import com.stctest.anycompmarketplace.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtBlackListService blacklistService;

    @PostMapping("/register")
    public BaseResponse registerUser(@RequestParam("email") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("roleType") RoleType roleType) {
        
        return new BaseResponse<>(authService.registerUser(roleType, email, username, password));
    }
    
    @PostMapping("/login")
    public BaseResponse generateToken(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        
        return new BaseResponse<>(authService.generateUserToken(username, password));
    }

    @PostMapping("/logout")
    public BaseResponse logoutUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        blacklistService.add(token); // Store token in blacklist until it expires
        return new BaseResponse<>("Logged out");
    }
}
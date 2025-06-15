package com.stctest.anycompmarketplace.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stctest.anycompmarketplace.entity.Role;
import com.stctest.anycompmarketplace.entity.User;
import com.stctest.anycompmarketplace.enums.ErrorCode;
import com.stctest.anycompmarketplace.enums.RoleType;
import com.stctest.anycompmarketplace.exception.CustomFailureException;
import com.stctest.anycompmarketplace.repository.RoleRepository;
import com.stctest.anycompmarketplace.repository.UserRepository;
import com.stctest.anycompmarketplace.utils.JwtUtil;
import com.stctest.anycompmarketplace.utils.RegexUtil;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public String registerUser(RoleType roleType, String email, String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomFailureException(ErrorCode.USERNAME_ALD_TAKEN);
        }

        if (!RegexUtil.isEmailValid(email)) {
            throw new CustomFailureException(ErrorCode.EMAIL_INVALID_FORMAT);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomFailureException(ErrorCode.EMAIL_ALD_TAKEN);
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(roleType.getRole())
                .orElseThrow(() -> new CustomFailureException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return "User registered successfully";
    }

    public String generateUserToken(String username, String password) {
        // try {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        // } catch (Exception ex) {
        //     throw new CustomFailureException(ErrorCode.USERNAME_PASSWORD_INVALID);
        // }
        if (authentication.isAuthenticated()) {
            String jwtTk = jwtUtil.generateToken(username);
            return jwtTk;
        } else {
            throw new CustomFailureException(ErrorCode.USERNAME_PASSWORD_INVALID);
        }
    }
}

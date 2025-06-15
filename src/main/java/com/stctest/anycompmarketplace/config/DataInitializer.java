package com.stctest.anycompmarketplace.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stctest.anycompmarketplace.entity.Role;
import com.stctest.anycompmarketplace.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            if (roleRepository.findByName("ROLE_SELLER").isEmpty()) {
                Role sellerRole = new Role();
                sellerRole.setName("ROLE_SELLER");
                roleRepository.save(sellerRole);
            }
            if (roleRepository.findByName("ROLE_BUYER").isEmpty()) {
                Role buyerRole = new Role();
                buyerRole.setName("ROLE_BUYER");
                roleRepository.save(buyerRole);
            }
        };
    }
}

package com.stctest.anycompmarketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stctest.anycompmarketplace.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsernameAndIdNot(String username, Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}

package com.stctest.anycompmarketplace.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stctest.anycompmarketplace.entity.Item;
import com.stctest.anycompmarketplace.entity.Purchase;
import com.stctest.anycompmarketplace.entity.User;
import com.stctest.anycompmarketplace.enums.ErrorCode;
import com.stctest.anycompmarketplace.exception.CustomFailureException;
import com.stctest.anycompmarketplace.other.UserInfoDetails;
import com.stctest.anycompmarketplace.repository.ItemRepository;
import com.stctest.anycompmarketplace.repository.PurchaseRepository;
import com.stctest.anycompmarketplace.repository.UserRepository;
import com.stctest.anycompmarketplace.utils.PageableGenerator;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);

        return foundUser.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public List<User> userList(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageElement = PageableGenerator.gen(pageNum, pageSize, sortBy);

        Page<User> allUsers = userRepository.findAll(pageElement);

        return allUsers.hasContent() ? allUsers.getContent() : Collections.emptyList();
    }
    
    public User getUser(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        return null;
    }

    public void updateUser(Long id, String email, String username) {
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new CustomFailureException(ErrorCode.EMAIL_ALD_TAKEN);
        }
        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new CustomFailureException(ErrorCode.USERNAME_ALD_TAKEN);
        }

        User foundUser = getUser(id);
        foundUser.setEmail(email);
        if(username != null) {
            foundUser.setUsername(username);
        }

        userRepository.save(foundUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ----------------------------- ROLE TYPE SELLER ----------------------------- //
    public List<Item> getItemsBySeller(Long sellerId) {
        return itemRepository.findByUserId(sellerId);
    }

    public void addItemBySeller(Long sellerId, String name, String description, BigDecimal price, Integer quantity) {
        Optional<User> foundSeller = userRepository.findById(sellerId);
        if (foundSeller.isPresent()) {
            Item objAdd = new Item();
            objAdd.setDescription(description);
            objAdd.setName(name);
            objAdd.setPrice(price);
            objAdd.setQuantity(quantity);
            objAdd.setUser(foundSeller.get());
            itemRepository.save(objAdd);
        }
    }
    // ----------------------------- ROLE TYPE SELLER ----------------------------- //

    // ----------------------------- ROLE TYPE BUYER ----------------------------- //
    public void buyerPurchaseItem(Long buyerId, Long itemId, Integer quantity) {
        boolean itemExist = itemRepository.existsById(itemId);

        if (itemExist) {
            Optional<User> optBuyer = userRepository.findById(buyerId);
            User foundBuyer = optBuyer.orElse(null);

            if (foundBuyer == null) {
                throw new CustomFailureException(ErrorCode.BUYER_NOT_FOUND);
            }

            Optional<Item> optItem = itemRepository.findById(itemId);
            Item foundItem = optItem.get();

            if (foundItem.getQuantity() >= quantity) {
                foundItem.setQuantity(foundItem.getQuantity() - quantity);
                itemRepository.save(foundItem);
                
                if (foundBuyer != null) {
                    Purchase pur = new Purchase();
                    pur.setQuantity(quantity);
                    pur.setUser(foundBuyer);
                    pur.setItem(foundItem);
                    
                    purchaseRepository.save(pur);
                }
            } else {
                throw new CustomFailureException(ErrorCode.PURCHASE_FAIL_ITEM_QUANTITY_INSUFFICIENT);
            }
            
            
        } else {
            throw new CustomFailureException(ErrorCode.ITEM_NOT_FOUND);
        }
    }
    // ----------------------------- ROLE TYPE BUYER ----------------------------- //
    
}
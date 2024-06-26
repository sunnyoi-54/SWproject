package com.example.BookRecord.service;

import com.example.BookRecord.domain.Wishlist;
import com.example.BookRecord.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public Long upload(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
        return wishlist.getId();
    }

    public void delete(Long wishlistId) {
        Optional<Wishlist> wishlist = wishlistRepository.findById(wishlistId);
        if (wishlist.isPresent()) {
            wishlistRepository.delete(wishlistId);
        } else {
            throw new IllegalArgumentException("해당 위시리스트가 존재하지 않습니다: " + wishlistId);
        }
    }

    public List<Wishlist> listWishlist() {
        return wishlistRepository.findAll();
    }

    public Optional<Wishlist> findOne(Long wishlistId) {
        return wishlistRepository.findById(wishlistId);
    }
}

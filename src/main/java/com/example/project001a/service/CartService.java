package com.example.project001a.service;

import com.example.project001a.entity.Cart;
import com.example.project001a.entity.Products;
import com.example.project001a.entity.User;
import com.example.project001a.repository.CartRepository;
import com.example.project001a.repository.ProductRepository;
import com.example.project001a.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;  // Fix to Product
    private final UserRepository userRepo;

    public List<Cart> getCartByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return cartRepo.findByUserId(user.getId());
    }
    public void removeFromCart(String email, Long cartId) {
        User user = userRepo.findByEmail(email).orElseThrow();
        cartRepo.deleteById(cartId);  // Assumes ownership check in repo
    }

    public Cart updateQuantity(String email, Long cartId, int quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cart.setQuantity(quantity);
        return cartRepo.save(cart);
    }

    public Cart addToCart(String email, Long productId, int quantity) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        Products product = productRepo.findById(productId)  // Singular
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (quantity > product.getStock()) {
            throw new RuntimeException("Insufficient stock. Requested: " + quantity +
                    ", Available: " + product.getStock());
        }


        cartRepo.deleteByUserIdAndProductId(user.getId(), productId);

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
        return cartRepo.save(cart);
    }
}

package com.example.project001a.controller;

import com.example.project001a.entity.Cart;
import com.example.project001a.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping
    public ResponseEntity<List<Cart>> getCart(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.getCartByEmail(email));  // Needs service method
    }
    @PostMapping("/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long productId,
                                          @RequestParam(defaultValue = "1") int quantity,
                                          Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(email, productId, quantity));
    }
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartId, Authentication auth) {
        cartService.removeFromCart(auth.getName(), cartId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateQuantity(@PathVariable Long cartId,
                                               @RequestParam int quantity,
                                               Authentication auth) {
        return ResponseEntity.ok(cartService.updateQuantity(auth.getName(), cartId, quantity));
    }

}
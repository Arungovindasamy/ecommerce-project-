package com.example.project001a.controller;

import com.example.project001a.dto.OrderResponse;  // ✅ NEW IMPORT
import com.example.project001a.entity.Order;
import com.example.project001a.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(Authentication auth) {
        return ResponseEntity.ok(orderService.placeOrder(auth.getName()));
    }

    // ✅ ADD THESE
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId, Authentication auth) {
        return ResponseEntity.ok(orderService.getOrderById(auth.getName(), orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, Authentication auth) {
        orderService.cancelOrder(auth.getName(), orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long orderId,
                                              @RequestParam String status,
                                              Authentication auth) {
        return ResponseEntity.ok(orderService.updateOrderStatus(auth.getName(), orderId, status));
    }
}

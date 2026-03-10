package com.example.project001a.service;

import com.example.project001a.dto.OrderResponse;
import com.example.project001a.entity.*;
import com.example.project001a.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartRepository cartRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;


    public Order placeOrder(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> userCarts = cartRepo.findByUserId(user.getId());
        if (userCarts.isEmpty())
            throw new RuntimeException("Cart is empty");
        double total = 0.0;
        for (Cart cart : userCarts) {
            Products product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }
            total += product.getPrice() * cart.getQuantity();
        }
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalAmount(total)
                .status("PLACED")
                .build();
        order = orderRepo.save(order);
        final Order savedOrder = order;
        List<OrderItem> items = new ArrayList<>();
        for (Cart cart : userCarts) {
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(cart.getProduct())
                    .quantity(cart.getQuantity())
                    .price(cart.getProduct().getPrice())
                    .build();
            items.add(item);
        }
        orderItemRepo.saveAll(items);
        for (Cart cart : userCarts) {
            Products product = cart.getProduct();
            product.setStock(product.getStock() - cart.getQuantity());
            productRepo.save(product);
        }
        cartRepo.deleteAll(userCarts);
        return order;
    }

    // ✅ MISSING 1: Get all orders by email
    public List<OrderResponse> getOrdersByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        return orderRepo.findByUserId(user.getId()).stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    // ✅ MISSING 2: Get single order
    public OrderResponse getOrderById(String email, Long orderId) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to order");
        }

        return mapToOrderResponse(order);
    }

    // ✅ MISSING 3: Cancel order
    public void cancelOrder(String email, Long orderId) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (!"PLACED".equals(order.getStatus())) {
            throw new RuntimeException("Only PLACED orders can be cancelled");
        }

        // Restore stock
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        for (OrderItem item : items) {
            Products product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        }

        order.setStatus("CANCELLED");
        orderRepo.save(order);
    }

    // ✅ MISSING 4: Update status
    public Order updateOrderStatus(String email, Long orderId, String status) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Order order = orderRepo.findById(orderId).orElseThrow();

        order.setStatus(status.toUpperCase());
        return orderRepo.save(order);
    }

    // ✅ HELPER - Extracted from your existing code
    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userName(order.getUser().getName())
                .userEmail(order.getUser().getEmail())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(orderItemRepo.findByOrderId(order.getId()))
                .build();
    }
}

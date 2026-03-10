package com.example.project001a.dto;

import com.example.project001a.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String userName;
    private String userEmail;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private List<OrderItem> items;  // ✅ Products + quantity + price
}

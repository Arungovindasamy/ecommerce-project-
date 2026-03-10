package com.example.project001a.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Products {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, description;
    private Double price;
    private Integer stock;
}
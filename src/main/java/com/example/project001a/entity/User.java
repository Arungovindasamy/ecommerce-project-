package com.example.project001a.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, email, password;
    @Builder.Default
    private String role = "ROLE_USER"; // Default role
}
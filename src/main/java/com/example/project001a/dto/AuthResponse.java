package com.example.project001a.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
package com.uepbh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;

    // Backward-compat: frontend sends "email" field in some pages
    // This getter maps "email" → "username" so both field names work
    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }
}

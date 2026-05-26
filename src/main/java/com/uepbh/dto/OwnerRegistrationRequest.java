package com.uepbh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String boardingHouseName;
    private String address;
    private String contactNumber;
    private String description;
}

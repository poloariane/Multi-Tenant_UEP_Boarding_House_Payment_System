package com.uepbh.service;

import com.uepbh.dto.LoginRequest;
import com.uepbh.dto.LoginResponse;
import com.uepbh.dto.OwnerRegistrationRequest;
import com.uepbh.entity.Owner;
import com.uepbh.entity.User;
import com.uepbh.repository.OwnerRepository;
import com.uepbh.repository.UserRepository;
import com.uepbh.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse loginOwner(LoginRequest request) {
        Optional<Owner> ownerOpt = ownerRepository.findByUsername(request.getUsername());
        if (ownerOpt.isEmpty()) {
            throw new RuntimeException("Owner not found");
        }

        Owner owner = ownerOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(owner.getUsername(), "ADMIN", owner.getOwnerId());
        return new LoginResponse(token, owner.getUsername(), "ADMIN", owner.getOwnerId());
    }

    public LoginResponse loginTenant(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString(), user.getOwnerId());
        return new LoginResponse(token, user.getUsername(), user.getRole().toString(), user.getOwnerId());
    }

    public Owner registerOwner(OwnerRegistrationRequest request) {
        if (ownerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (ownerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Owner owner = new Owner();
        owner.setOwnerId(UUID.randomUUID().toString());
        owner.setUsername(request.getUsername());
        owner.setEmail(request.getEmail());
        owner.setPassword(passwordEncoder.encode(request.getPassword()));
        owner.setBoardingHouseName(request.getBoardingHouseName());
        owner.setAddress(request.getAddress());
        owner.setContactNumber(request.getContactNumber());
        owner.setDescription(request.getDescription());
        owner.setActive(true);

        return ownerRepository.save(owner);
    }
}

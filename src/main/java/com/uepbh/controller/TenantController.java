package com.uepbh.controller;

import com.uepbh.dto.TenantDTO;
import com.uepbh.service.TenantService;
import com.uepbh.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantDTO> addTenant(@RequestBody TenantDTO tenantDTO) {
        TenantDTO saved = tenantService.addTenant(tenantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        List<TenantDTO> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TenantDTO>> getActiveTenants() {
        List<TenantDTO> tenants = tenantService.getActiveTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Long id) {
        TenantDTO tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(tenant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> updateTenant(@PathVariable Long id, @RequestBody TenantDTO tenantDTO) {
        TenantDTO updated = tenantService.updateTenant(id, tenantDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateTenant(@PathVariable Long id) {
        tenantService.deactivateTenant(id);
        return ResponseEntity.noContent().build();
    }
}

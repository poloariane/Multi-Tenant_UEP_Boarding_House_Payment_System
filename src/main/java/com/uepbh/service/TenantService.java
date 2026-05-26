package com.uepbh.service;

import com.uepbh.dto.TenantDTO;
import com.uepbh.entity.Tenant;
import com.uepbh.repository.TenantRepository;
import com.uepbh.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;

    public TenantDTO addTenant(TenantDTO tenantDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Tenant tenant = new Tenant();
        tenant.setOwnerId(ownerId);
        tenant.setFirstName(tenantDTO.getFirstName());
        tenant.setLastName(tenantDTO.getLastName());
        tenant.setEmail(tenantDTO.getEmail());
        tenant.setContactNumber(tenantDTO.getContactNumber());
        tenant.setAddress(tenantDTO.getAddress());
        tenant.setActive(true);

        Tenant saved = tenantRepository.save(tenant);
        return convertToDTO(saved);
    }

    public List<TenantDTO> getAllTenants() {
        String ownerId = TenantContext.getCurrentTenant();
        return tenantRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TenantDTO> getActiveTenants() {
        String ownerId = TenantContext.getCurrentTenant();
        return tenantRepository.findByOwnerIdAndActive(ownerId, true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TenantDTO getTenantById(Long id) {
        String ownerId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        return convertToDTO(tenant);
    }

    public TenantDTO updateTenant(Long id, TenantDTO tenantDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setFirstName(tenantDTO.getFirstName());
        tenant.setLastName(tenantDTO.getLastName());
        tenant.setEmail(tenantDTO.getEmail());
        tenant.setContactNumber(tenantDTO.getContactNumber());
        tenant.setAddress(tenantDTO.getAddress());
        tenant.setUpdatedAt(LocalDateTime.now());

        Tenant updated = tenantRepository.save(tenant);
        return convertToDTO(updated);
    }

    public void deactivateTenant(Long id) {
        String ownerId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setActive(false);
        tenant.setInactiveDate(LocalDateTime.now());
        tenantRepository.save(tenant);
    }

    private TenantDTO convertToDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getId());
        dto.setFirstName(tenant.getFirstName());
        dto.setLastName(tenant.getLastName());
        dto.setEmail(tenant.getEmail());
        dto.setContactNumber(tenant.getContactNumber());
        dto.setAddress(tenant.getAddress());
        dto.setRoomId(tenant.getRoom() != null ? tenant.getRoom().getId() : null);
        dto.setActive(tenant.getActive());
        dto.setCreatedAt(tenant.getCreatedAt());
        dto.setUpdatedAt(tenant.getUpdatedAt());
        return dto;
    }
}

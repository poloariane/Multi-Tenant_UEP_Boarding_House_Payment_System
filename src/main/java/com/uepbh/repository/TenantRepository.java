package com.uepbh.repository;

import com.uepbh.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    List<Tenant> findByOwnerId(String ownerId);
    List<Tenant> findByOwnerIdAndActive(String ownerId, Boolean active);
    Optional<Tenant> findByIdAndOwnerId(Long id, String ownerId);
    Optional<Tenant> findByEmail(String email);
}

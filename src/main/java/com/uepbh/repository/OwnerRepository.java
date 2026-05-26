package com.uepbh.repository;

import com.uepbh.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUsername(String username);
    Optional<Owner> findByOwnerId(String ownerId);
    Optional<Owner> findByEmail(String email);
}

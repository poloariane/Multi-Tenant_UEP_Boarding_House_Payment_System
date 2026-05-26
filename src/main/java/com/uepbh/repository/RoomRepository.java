package com.uepbh.repository;

import com.uepbh.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByOwnerId(String ownerId);
    List<Room> findByOwnerIdAndActive(String ownerId, Boolean active);
    Optional<Room> findByIdAndOwnerId(Long id, String ownerId);
    Optional<Room> findByRoomNumberAndOwnerId(String roomNumber, String ownerId);
}

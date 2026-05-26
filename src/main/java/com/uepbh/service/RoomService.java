package com.uepbh.service;

import com.uepbh.dto.RoomDTO;
import com.uepbh.entity.Room;
import com.uepbh.repository.RoomRepository;
import com.uepbh.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomDTO addRoom(RoomDTO roomDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Room room = new Room();
        room.setOwnerId(ownerId);
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setDescription(roomDTO.getDescription());
        room.setMonthlyRate(roomDTO.getMonthlyRate());
        room.setCapacity(roomDTO.getCapacity());
        room.setActive(true);

        Room saved = roomRepository.save(room);
        return convertToDTO(saved);
    }

    public List<RoomDTO> getAllRooms() {
        String ownerId = TenantContext.getCurrentTenant();
        return roomRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getActiveRooms() {
        String ownerId = TenantContext.getCurrentTenant();
        return roomRepository.findByOwnerIdAndActive(ownerId, true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoomDTO getRoomById(Long id) {
        String ownerId = TenantContext.getCurrentTenant();
        Room room = roomRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return convertToDTO(room);
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Room room = roomRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setDescription(roomDTO.getDescription());
        room.setMonthlyRate(roomDTO.getMonthlyRate());
        room.setCapacity(roomDTO.getCapacity());
        room.setUpdatedAt(LocalDateTime.now());

        Room updated = roomRepository.save(room);
        return convertToDTO(updated);
    }

    public void deleteRoom(Long id) {
        String ownerId = TenantContext.getCurrentTenant();
        Room room = roomRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setActive(false);
        roomRepository.save(room);
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setDescription(room.getDescription());
        dto.setMonthlyRate(room.getMonthlyRate());
        dto.setCapacity(room.getCapacity());
        dto.setActive(room.getActive());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());
        return dto;
    }
}

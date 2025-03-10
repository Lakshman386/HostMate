package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    
    public Optional<Room> getRoomByNumber(Integer roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }
    
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public Room updateRoom(Long id, Room room) {
        if (roomRepository.existsById(id)) {
            room.setId(id);
            return roomRepository.save(room);
        }
        return null;
    }
    
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    
    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailable(true);
    }
    
    public long getTotalRooms() {
        // Return a fixed value of 1000 rooms as requested
        return 1000;
    }
    
    public boolean isRoomAvailable(Integer roomNumber) {
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        return room.map(Room::getIsAvailable).orElse(false);
    }
    
    public void setRoomAvailability(Long id, boolean isAvailable) {
        Optional<Room> room = getRoomById(id);
        if (room.isPresent()) {
            Room updatedRoom = room.get();
            updatedRoom.setIsAvailable(isAvailable);
            roomRepository.save(updatedRoom);
        }
    }
}

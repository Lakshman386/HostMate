package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    
    @Autowired
    private GuestRepository guestRepository;
    
    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }
    
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }
    
    public Optional<Guest> getGuestById(Long id) {
        return guestRepository.findById(id);
    }
    
    public Guest updateGuest(Long id, Guest guest) {
        if (guestRepository.existsById(id)) {
            guest.setId(id);
            return guestRepository.save(guest);
        }
        return null;
    }
    
    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
    
    public long getTotalGuests() {
        return guestRepository.count();
    }
}

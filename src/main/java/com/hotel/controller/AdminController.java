package com.hotel.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.model.Guest;
import com.hotel.model.Room;
import com.hotel.service.GuestService;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/login";
    }

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private GuestService guestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Add statistics to the model
            model.addAttribute("totalRooms", roomService.getTotalRooms());
            model.addAttribute("activeReservations", reservationService.getActiveReservationsCount());
            model.addAttribute("totalGuests", guestService.getTotalGuests());
            model.addAttribute("monthlyRevenue", reservationService.calculateMonthlyRevenue());
            
            // Add recent reservations
            model.addAttribute("recentReservations", reservationService.getRecentReservations());
            
            return "admin/dashboard";
        } catch (Exception e) {
            return "redirect:/index.html";
        }
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute Room room) {
        roomService.createRoom(room);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        return roomService.getRoomById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/rooms/{id}/availability")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> updateRoomAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        roomService.setRoomAvailability(id, available);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "admin/reservations";
    }

    @GetMapping("/guests")
    public String guests(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        return "admin/guests";
    }

    @PostMapping("/guests/save")
    public String saveGuest(@ModelAttribute Guest guest) {
        guestService.createGuest(guest);
        return "redirect:/admin/guests";
    }

    @GetMapping("/guests/{id}")
    @ResponseBody
    public ResponseEntity<Guest> getGuest(@PathVariable Long id) {
        return guestService.getGuestById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guests/delete/{id}")
public String deleteGuest(@PathVariable Long id) {
    guestService.deleteGuest(id);
    return "redirect:/admin/guests";
}


    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRooms", roomService.getTotalRooms());
        stats.put("activeReservations", reservationService.getActiveReservationsCount());
        stats.put("totalGuests", guestService.getTotalGuests());
        stats.put("monthlyRevenue", reservationService.calculateMonthlyRevenue());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/test")
    public String testPage() {
        return "admin/test";
    }
}

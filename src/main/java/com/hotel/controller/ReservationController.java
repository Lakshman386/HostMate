package com.hotel.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotel.model.Guest;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.service.GuestService;
import com.hotel.service.ReservationService;
import com.hotel.service.RoomService;

import jakarta.validation.Valid;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private GuestService guestService;
    
    @Autowired
    private RoomService roomService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping("/")
    public String home(Model model) {
        List<Reservation> recentReservations = reservationService.getRecentReservations();
        model.addAttribute("recentReservations", recentReservations);
        return "index";
    }

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    @GetMapping("/reservation/new")
    public String showReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomService.getAvailableRooms());
        return "reservation-form";
    }

    @PostMapping("/reservation/save")
    public String saveReservation(@ModelAttribute("reservation") @Valid Reservation reservation, 
                                  BindingResult result, 
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAvailableRooms());
            return "reservation-form";
        }

        try {
            // Set default values
            reservation.setPaymentStatus(Reservation.PaymentStatus.PAID);
            reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
            reservation.setReservationDate(LocalDateTime.now());
            
            // Create a guest if needed
            if (reservation.getGuestId() == null) {
                Guest guest = new Guest();
                guest.setName(reservation.getGuestName());
                guest.setPhoneNumber(reservation.getPhoneNumber());
                guest.setEmail(reservation.getGuestName().toLowerCase().replace(" ", ".") + "@example.com"); // Generate a dummy email
                Guest savedGuest = guestService.createGuest(guest);
                reservation.setGuestId(savedGuest.getId());
            }
            
            // Set roomId based on roomNumber if not provided
            if (reservation.getRoomId() == null && reservation.getRoomNumber() != null) {
                Room room = roomService.getRoomByNumber(reservation.getRoomNumber())
                        .orElseThrow(() -> new IllegalArgumentException("Room not found with number: " + reservation.getRoomNumber()));
                reservation.setRoomId(room.getId());
            }

            reservationService.createReservation(reservation);
            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating reservation: " + e.getMessage());
            model.addAttribute("guests", guestService.getAllGuests());
            model.addAttribute("rooms", roomService.getAvailableRooms());
            return "reservation-form";
        }
    }

    @GetMapping("/reservation/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        model.addAttribute("reservation", reservation);
        model.addAttribute("guests", guestService.getAllGuests());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "reservation-form";
    }

    @GetMapping("/reservation/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

    @GetMapping("/reservation/payment/{id}")
    public String showPaymentForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + id));
        model.addAttribute("reservation", reservation);
        return "payment-form";
    }

    @PostMapping("/reservation/payment/process")
    public String processPayment(@RequestParam Long reservationId, 
                                 @RequestParam Reservation.PaymentMethod paymentMethod,
                                 Model model) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id:" + reservationId));

            reservation.setPaymentMethod(paymentMethod);
            reservation.setPaymentStatus(Reservation.PaymentStatus.PAID);
            reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

            reservationService.updateReservation(reservationId, reservation);

            model.addAttribute("successMessage", "Payment processed successfully!");
            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error processing payment: " + e.getMessage());
            return "payment-form";
        }
    }
    
    @GetMapping("/api/rooms/available")
    @ResponseBody
    public boolean checkRoomAvailability(
            @RequestParam Integer roomNumber,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {

        LocalDateTime checkIn = LocalDateTime.parse(checkInDate, formatter);
        LocalDateTime checkOut = LocalDateTime.parse(checkOutDate, formatter);

        return reservationService.isRoomAvailable(roomNumber, checkIn, checkOut);
    }
    
}

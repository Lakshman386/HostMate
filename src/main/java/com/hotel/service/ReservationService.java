package com.hotel.service;

import com.hotel.model.Reservation;
import com.hotel.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getRecentReservations() {
        return reservationRepository.findTop5ByOrderByReservationDateDesc();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        if (reservationRepository.existsById(id)) {
            updatedReservation.setId(id);
            return reservationRepository.save(updatedReservation);
        }
        throw new IllegalArgumentException("Reservation not found with id: " + id);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public boolean isRoomAvailable(Integer roomNumber, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                roomNumber, checkIn, checkOut);
        return conflictingReservations.isEmpty();
    }

    public LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, ISO_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeStr + ". Expected format: yyyy-MM-dd'T'HH:mm");
        }
    }

    public int getActiveReservationsCount() {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.countByCheckOutDateAfterAndStatusNot(
                now, Reservation.ReservationStatus.CANCELLED);
    }

    public double calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        
        List<Reservation> monthlyReservations = reservationRepository.findByReservationDateBetweenAndPaymentStatus(
                startOfMonth, endOfMonth, Reservation.PaymentStatus.PAID);
        
        // Include all reservations regardless of date for demo purposes
        if (monthlyReservations.isEmpty()) {
            List<Reservation> allReservations = reservationRepository.findAll();
            return allReservations.stream()
                    .mapToDouble(Reservation::getAmount)
                    .sum();
        }
        
        return monthlyReservations.stream()
                .mapToDouble(Reservation::getAmount)
                .sum();
    }
}

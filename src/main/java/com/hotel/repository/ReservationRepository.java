package com.hotel.repository;

import com.hotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.roomNumber = :roomNumber AND r.status != 'CANCELLED' AND " +
           "((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    List<Reservation> findConflictingReservations(
            @Param("roomNumber") Integer roomNumber,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut);

    List<Reservation> findTop5ByOrderByReservationDateDesc();

    int countByCheckOutDateAfterAndStatusNot(LocalDateTime date, Reservation.ReservationStatus status);

    List<Reservation> findByReservationDateBetweenAndPaymentStatus(
            LocalDateTime startDate, 
            LocalDateTime endDate, 
            Reservation.PaymentStatus paymentStatus);
}

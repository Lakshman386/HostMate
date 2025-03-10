package com.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private Integer roomNumber;

    @NotNull
    private String roomType;

    @NotNull
    @Min(0)
    private Double pricePerNight;

    private String description;

    @NotNull
    private Boolean isAvailable = true;

    private String amenities;

    @Column(name = "max_occupancy")
    private Integer maxOccupancy;
}

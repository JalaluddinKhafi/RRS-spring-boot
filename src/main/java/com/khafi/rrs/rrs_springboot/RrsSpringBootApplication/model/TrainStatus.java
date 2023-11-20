package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "train_status")
public class TrainStatus {
    @Id
    @Column(name = "st_id")
    private Long statusId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "train_id")
    private Train train;

    @Column(name = "st_available_seat")
    private Integer availableSeat;

    @Column(name = "st_booked_seat")
    private Integer bookedSeat;

    @Column(name = "st_departure_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @Column(name = "st_departure_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime departureTime;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;



    // Getters and setters
}

package com.hotelreservation.roomreservationservice.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservationlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReservationList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_id")
    private Long roomid;

    @Column(name = "user_id")
    private Long userid;

    @Column(name ="entrydate")
    private String entrydate;

    @Column(name ="departdate")
    private String departdate;

    @Column(name = "checkin")
    private String checkin;

}

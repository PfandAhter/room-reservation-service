package com.hotelreservation.roomreservationservice.rest.repository;

import com.hotelreservation.roomreservationservice.model.entity.ReservationList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationListRepository extends JpaRepository<ReservationList,Long> {
    ReservationList findUserById(Long userid);

    ReservationList findReservationListByUserid(Long userid);
}

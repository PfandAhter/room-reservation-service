package com.hotelreservation.roomreservationservice.rest.controller;


import com.hotelreservation.roomreservationservice.api.request.BaseRequest;
import com.hotelreservation.roomreservationservice.api.request.BuyRoomRequest;
import com.hotelreservation.roomreservationservice.api.request.SetCheckInRequest;
import com.hotelreservation.roomreservationservice.api.request.UserListInRoomsRequest;
import com.hotelreservation.roomreservationservice.api.response.BaseResponse;
import com.hotelreservation.roomreservationservice.api.response.BuyRoomResponse;
import com.hotelreservation.roomreservationservice.api.response.UserListInRoomsResponse;
import com.hotelreservation.roomreservationservice.model.dto.RoomDTO;
import com.hotelreservation.roomreservationservice.rest.exception.AuthException;
import com.hotelreservation.roomreservationservice.rest.service.RoomReservationServiceImp;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/roomreservationservice")

public class RoomReservationServiceController {

    private final RoomReservationServiceImp roomReservationServiceImp;


    /*@GetMapping("/roomlist")
    public ResponseEntity<List<RoomDTO>> getRoomList (BaseRequest request){
        return ResponseEntity.ok(roomReservationServiceImp.findAllAvailableRooms(request));
    }*/

    @PostMapping(path = "/roomlist")
    public ResponseEntity<List<RoomDTO>> getRoomList(@RequestBody BaseRequest request){
        return ResponseEntity.ok(roomReservationServiceImp.findAllAvailableRooms(request));
    }

    @PostMapping(path = "/buyroom")
    public ResponseEntity<BuyRoomResponse> buyRoomRequest (@RequestBody BuyRoomRequest request) throws AuthException {
        return ResponseEntity.ok(roomReservationServiceImp.buyRoom(request));
    }

    @PostMapping(path = "/getuserlistinrooms")
    public ResponseEntity<UserListInRoomsResponse> getUserListInRooms (@NonNull @RequestBody UserListInRoomsRequest request) throws AuthException{
        return ResponseEntity.ok(roomReservationServiceImp.getUserListInRoom(request));
    }

    @PostMapping(path = "/setcheckin")
    public ResponseEntity<BaseResponse> setCheckIn (@NonNull @RequestBody SetCheckInRequest request) throws AuthException{
        return ResponseEntity.ok(roomReservationServiceImp.setCheckIn(request));
    }

}

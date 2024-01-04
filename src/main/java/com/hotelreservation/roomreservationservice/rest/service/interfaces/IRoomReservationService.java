package com.hotelreservation.roomreservationservice.rest.service.interfaces;
import com.hotelreservation.roomreservationservice.api.request.BaseRequest;
import com.hotelreservation.roomreservationservice.api.request.BuyRoomRequest;
import com.hotelreservation.roomreservationservice.api.request.SetCheckInRequest;
import com.hotelreservation.roomreservationservice.api.request.UserListInRoomsRequest;
import com.hotelreservation.roomreservationservice.api.response.BaseResponse;
import com.hotelreservation.roomreservationservice.api.response.BuyRoomResponse;
import com.hotelreservation.roomreservationservice.api.response.UserListInRoomsResponse;
import com.hotelreservation.roomreservationservice.model.dto.RoomDTO;
import com.hotelreservation.roomreservationservice.rest.exception.AuthException;

import java.util.List;

public interface IRoomReservationService {

    List<RoomDTO> findAllAvailableRooms(BaseRequest request);

    BuyRoomResponse buyRoom(BuyRoomRequest request) throws AuthException;

    UserListInRoomsResponse getUserListInRoom(UserListInRoomsRequest request) throws AuthException;

    BaseResponse setCheckIn(SetCheckInRequest request) throws AuthException;

}

package com.hotelreservation.roomreservationservice.api.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserListInRoomsRequest extends BaseRequest{
    private Long userid;
}

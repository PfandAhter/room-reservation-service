package com.hotelreservation.roomreservationservice.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SetCheckInRequest extends BaseRequest {
    Long userid;
}

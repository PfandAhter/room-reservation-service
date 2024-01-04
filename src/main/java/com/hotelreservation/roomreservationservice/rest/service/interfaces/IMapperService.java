package com.hotelreservation.roomreservationservice.rest.service.interfaces;

import java.util.List;

public interface IMapperService {
    public <T,D> List<D> modelMapper (List<T> source, Class<D> destination);
}

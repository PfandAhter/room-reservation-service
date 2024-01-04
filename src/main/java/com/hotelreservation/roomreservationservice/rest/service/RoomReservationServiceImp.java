package com.hotelreservation.roomreservationservice.rest.service;

import com.hotelreservation.roomreservationservice.api.request.BaseRequest;
import com.hotelreservation.roomreservationservice.api.request.BuyRoomRequest;
import com.hotelreservation.roomreservationservice.api.request.SetCheckInRequest;
import com.hotelreservation.roomreservationservice.api.request.UserListInRoomsRequest;
import com.hotelreservation.roomreservationservice.api.response.BaseResponse;
import com.hotelreservation.roomreservationservice.api.response.BuyRoomResponse;
import com.hotelreservation.roomreservationservice.api.response.UserListInRoomsResponse;
import com.hotelreservation.roomreservationservice.lib.constants.Constants;
import com.hotelreservation.roomreservationservice.model.dto.RoomDTO;
import com.hotelreservation.roomreservationservice.model.entity.Balance;
import com.hotelreservation.roomreservationservice.model.entity.ReservationList;
import com.hotelreservation.roomreservationservice.model.entity.Room;
import com.hotelreservation.roomreservationservice.model.entity.User;
import com.hotelreservation.roomreservationservice.rest.exception.AuthException;
import com.hotelreservation.roomreservationservice.rest.repository.BalanceRepository;
import com.hotelreservation.roomreservationservice.rest.repository.ReservationListRepository;
import com.hotelreservation.roomreservationservice.rest.repository.RoomRepository;
import com.hotelreservation.roomreservationservice.rest.repository.UserRepository;
import com.hotelreservation.roomreservationservice.rest.service.interfaces.IRoomReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class RoomReservationServiceImp implements IRoomReservationService {

    private final UserRepository userRepository;

    private final BalanceRepository balanceRepository;

    private final ReservationListRepository reservationListRepository;

    private final RoomRepository roomRepository;

    private final MapperServiceImpl mapperService;

    private final RestTemplate restTemplate;


    @Override
    public List<RoomDTO> findAllAvailableRooms(BaseRequest baseRequest) {
        resetRoomIfDepartDateExpired();
        return mapperService.modelMapper(roomRepository.findAll(), RoomDTO.class);
    }

    @Override
    public BuyRoomResponse buyRoom(BuyRoomRequest request) throws AuthException {
        if (restTemplate.postForObject("http://localhost:8082/jwt/checkTokenValid", request, Boolean.class)) {
            String jwtUsername = restTemplate.postForObject("http://localhost:8082/jwt/extractUsername", request, String.class);
            User user = userRepository.findByUsername(jwtUsername);
            Balance balanceRepo = balanceRepository.findByUserId(user.getId());
            Room room = roomRepository.findRoomById(request.getRoomnumber());

            if (room.getIsAvailable().equals("TRUE")) {
                balanceRepo.setAmount(balanceRepo.getAmount() - room.getPrice());

                balanceRepository.save(balanceRepo);
                room.setIsAvailable("FALSE");
                setReservationList(request); // will set reservationList for details information

                room.setMember1(request.getMember1());
                room.setMember2(request.getMember2());
                roomRepository.save(room);

                BuyRoomResponse buyRoomResponse = new BuyRoomResponse();
                buyRoomResponse.setPurchasedRoom(room.getId());
                buyRoomResponse.setBalance(balanceRepo.getAmount());

                return buyRoomResponse;
            } else {
                throw new AuthException(Constants.ROOM_IS_NOT_AVAILABLE);
            }
        } else {
            throw new AuthException(Constants.ACCESS_DENIED);
        }
    }

    @Override
    public UserListInRoomsResponse getUserListInRoom(UserListInRoomsRequest request) throws AuthException {
        if (restTemplate.postForObject("http://localhost:8082/jwt/checkTokenValid", request, Boolean.class)) {
            resetRoomIfDepartDateExpired();
            String jwtUsername = restTemplate.postForObject("http://localhost:8082/jwt/extractUsername", request, String.class);
            User tokenUser = userRepository.findByUsername(jwtUsername);

            ReservationList reservationList = reservationListRepository.findReservationListByUserid(tokenUser.getId());

            Room room = roomRepository.findRoomById(reservationList.getRoomid());

            UserListInRoomsResponse userListInRoomsResponse = new UserListInRoomsResponse();

            //TODO use modelmapper

            userListInRoomsResponse.setIsAvailable(room.getIsAvailable());
            userListInRoomsResponse.setFloor(room.getFloor());
            userListInRoomsResponse.setRoomnumber(room.getId());
            userListInRoomsResponse.setMember1(room.getMember1());
            userListInRoomsResponse.setMember2(room.getMember2());

            return userListInRoomsResponse;
        } else {
            throw new AuthException(Constants.ACCESS_DENIED);
        }
    }

    private void resetRoomIfDepartDateExpired() {
        Date date = new Date();

        int month = date.getMonth() + 1;
        int day = date.getDay();
        int year = date.getYear() - 100;

        for (int i = 0; i < reservationListRepository.findAll().size(); i++) {
            ReservationList reservationList = reservationListRepository.findUserById(Long.valueOf(i) + 1);
            Room room = roomRepository.findRoomById(reservationList.getRoomid());

            int userDepMonth = Integer.parseInt(reservationList.getDepartdate().substring(0, 2));
            int userDepDay = Integer.parseInt(reservationList.getDepartdate().substring(3, 5));
            int userDepYear = Integer.parseInt(reservationList.getDepartdate().substring(8, 10));

            if (month > userDepMonth || day > userDepDay || year > userDepYear) {
                room.setIsAvailable("TRUE");
                room.setMember1("NULL");
                room.setMember2("NULL");
                reservationList.setCheckin("DEPARTED");
                reservationListRepository.save(reservationList);
                roomRepository.save(room);
            }
        }
    }

    private void setReservationList(BuyRoomRequest request) {
        Room room = roomRepository.findRoomById(request.getRoomnumber());

        User user1 = userRepository.findByUsername(request.getMember1());
        User user2 = userRepository.findByUsername(request.getMember2());

        ReservationList reservationList1 = new ReservationList();
        ReservationList reservationList2 = new ReservationList();

        reservationList1.setUserid(user1.getId());
        reservationList1.setRoomid(room.getId());
        reservationList1.setCheckin("FALSE");
        reservationList1.setEntrydate(request.getEntrydate());
        reservationList1.setDepartdate(request.getDepartdate());

        reservationList2.setUserid(user2.getId());
        reservationList2.setRoomid(room.getId());
        reservationList2.setCheckin("FALSE");
        reservationList2.setEntrydate(request.getEntrydate());
        reservationList2.setDepartdate(request.getDepartdate());

        reservationListRepository.save(reservationList1);
        reservationListRepository.save(reservationList2);
    }

    @Override
    public BaseResponse setCheckIn(SetCheckInRequest request) throws AuthException {
        if (restTemplate.postForObject("http://localhost:8082/jwt/checkTokenValid", request, Boolean.class)) {
            String jwtUsername = restTemplate.postForObject("http://localhost:8082/jwt/extractUsername", request, String.class);
            User tokenUser = userRepository.findByUsername(jwtUsername);

            ReservationList reservationList = reservationListRepository.findReservationListByUserid(tokenUser.getId());

            reservationList.setCheckin("TRUE");
            reservationListRepository.save(reservationList);

            return new BaseResponse();
        } else {
            throw new AuthException(Constants.ACCESS_DENIED);
        }
    }
}

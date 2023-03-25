package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
        ParkingLot parkingLot;
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);

        try {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        } catch (Exception e) {
            throw new Exception("Cannot make reservation");
        }
        reservation.setUser(user);

        user.getReservationList().add(reservation);

        List<Spot> spotList = parkingLot.getSpotList();
        List<Spot> notOccupied = new ArrayList<>();
        for (Spot spot1 : spotList) {
            if (spot1.getOccupied() == false) {
                notOccupied.add(spot1);
            }
        }
        if (notOccupied.isEmpty()) {
            throw new Exception("Cannot make reservation");
        }
        Spot spot = null;
        int mini = Integer.MAX_VALUE;
        for (Spot spot1 : notOccupied) {
            int value = spot1.getPricePerHour();
            if(value<mini)
            {
                mini = value;
                spot = spot1;
            }
        }

        if(numberOfWheels>4){
            if(spot.getSpotType().equals(SpotType.OTHERS))
            {
                reservation.setSpot(spot);
            }
        }
        else if(numberOfWheels>2){
            if(spot.getSpotType().equals(SpotType.FOUR_WHEELER))
            {
                reservation.setSpot(spot);
            }

        }
        else spot.setSpotType(SpotType.TWO_WHEELER);


        spot.getReservationList().add(reservation);
        return reservation;
    }

}

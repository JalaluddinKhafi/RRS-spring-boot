package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Passenger;

import java.util.List;

public interface PassengerService {

    Passenger savePassenger(Passenger passenger);
    Passenger getPassengerById(Long id);
    List<Passenger> getAllPassengers();
    void deletePassenger(Long id);
    public double getTotalPricesOfAllPassengers();
    public int getTotalPassengerCount();



}

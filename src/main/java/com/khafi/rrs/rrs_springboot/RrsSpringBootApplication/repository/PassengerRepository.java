package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}


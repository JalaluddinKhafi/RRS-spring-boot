package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.TrainStatus;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;


public interface TrainStatusService {
    void saveTrainStatus(TrainStatus trainStatus);
    TrainStatus getTrainStatusByTrainId(Long trainId);
    List<TrainStatus> getAllTrainStatuses();
    @Transactional
    void deleteTrainStatus(Long trainId);
   // public List<Object[]> getTrainDetails();
   boolean existsByTrainId(Long id);
    List<TrainStatus> searchSchedules(String source, String destination, LocalDate departureTime, int numberOfSeats);


}


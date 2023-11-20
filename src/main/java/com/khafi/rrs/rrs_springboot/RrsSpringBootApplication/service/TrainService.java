package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Train;


import java.util.List;


public interface TrainService {
        Train saveTrain(Train train);
        Train getTrainById(Long id);
        List<Train> getAllTrains();
        void deleteTrain(Long id);
        int totalTrainsCount();


}

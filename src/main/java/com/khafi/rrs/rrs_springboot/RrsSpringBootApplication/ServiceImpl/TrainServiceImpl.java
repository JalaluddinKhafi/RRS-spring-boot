package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Train;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.TrainRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.TrainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;

    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public Train getTrainById(Long id) {
        return trainRepository.findById(id).orElse(null);
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    @Override
    public int totalTrainsCount() {
        List<Train> trainList=getAllTrains();
        int totalTrains=trainList.size();
        return totalTrains;
    }
}


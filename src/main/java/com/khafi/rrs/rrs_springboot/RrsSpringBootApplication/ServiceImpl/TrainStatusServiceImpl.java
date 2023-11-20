package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.TrainStatus;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.TrainStatusRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.TrainStatusService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStatusServiceImpl implements TrainStatusService {
    private final TrainStatusRepository trainStatusRepository;

    public TrainStatusServiceImpl(TrainStatusRepository trainStatusRepository) {
        this.trainStatusRepository = trainStatusRepository;
    }

    @Override
    public void saveTrainStatus(TrainStatus trainStatus) {
        trainStatusRepository.save(trainStatus);
    }

    @Override
    public TrainStatus getTrainStatusByTrainId(Long trainId) {
        return trainStatusRepository.findById(trainId).orElse(null);
    }

    @Override
    public List<TrainStatus> getAllTrainStatuses() {
        return trainStatusRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteTrainStatus(Long trainId) {
        try {
            trainStatusRepository.deleteById(trainId);
            System.out.println("Train Status deleted by ID: "+trainId);
        } catch (Exception e) {
            System.out.println("Error deleting Train Status with ID: {} " +trainId+" "+ e);
            // Handle the exception as needed.
        }
    }

    @Override
    public boolean existsByTrainId(Long id) {
        return trainStatusRepository.existsByTrainId(id);
    }

//    @Override
//    public List<Object[]> getTrainDetails() {
//        return trainStatusRepository.getTrainDetails();
//    }
}


package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Route;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.RouteRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.RouteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public Route getRouteById(Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}


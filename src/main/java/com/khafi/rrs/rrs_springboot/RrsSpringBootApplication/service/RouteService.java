package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Route;

import java.util.List;

public interface RouteService {
    Route saveRoute(Route route);
    Route getRouteById(Long id);
    List<Route> getAllRoutes();
    void deleteRoute(Long id);
}


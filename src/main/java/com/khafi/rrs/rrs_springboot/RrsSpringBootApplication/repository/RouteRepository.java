package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route,Long> {
}

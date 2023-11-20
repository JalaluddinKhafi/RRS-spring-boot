package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.controller;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.RouteServiceImpl;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.TrainStatusServiceImpl;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Route;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.TrainStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    TrainStatusServiceImpl trainStatusService;
    @Autowired
    RouteServiceImpl routeService;
    @GetMapping("/guestTrains")
    public String getAllTriansDetials(Model model){
        List<TrainStatus> schedules = trainStatusService.getAllTrainStatuses();
        model.addAttribute("schedules", schedules);
        return "guestTrains";

    }
    @GetMapping("/guestRouts")
    public String getAllRoutes(Model model){
        List<Route> routes=routeService.getAllRoutes();
        model.addAttribute("allRoutes",routes);
        return "guestRouts";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contcat() {
        return "contact";
    }
}

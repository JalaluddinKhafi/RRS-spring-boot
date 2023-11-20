package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.controller;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.*;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.*;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.TrainStatusRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.*;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RouteServiceImpl routeService;
    private final UserServiceImpl userService;
    private final TrainServiceImpl trainService;
    private final TrainStatusServiceImpl trainStatusService;
    private final PassengerServiceImpl passengerService;
    private final TicketServiceImpl ticketService;
    private final TrainStatusRepository trainStatusRepository;

    public AdminController(RouteServiceImpl routeService, UserServiceImpl userServicImpl, TrainServiceImpl trainService, TrainStatusServiceImpl trainStatusService, PassengerServiceImpl passengerService, TicketServiceImpl ticketService, TrainStatusRepository trainStatusRepository) {
        this.routeService = routeService;
        this.userService = userServicImpl;
        this.trainService = trainService;
        this.trainStatusService = trainStatusService;
        this.passengerService = passengerService;
        this.ticketService = ticketService;
        this.trainStatusRepository = trainStatusRepository;
    }


    @GetMapping("/adminHomePage")
    public String adminHome(Model model) {
        double totalPrice = passengerService.getTotalPricesOfAllPassengers();
        model.addAttribute("totalPrice", totalPrice);
        int totalPassengers = passengerService.getTotalPassengerCount();
        model.addAttribute("totalPassengers", totalPassengers);
        int totalUser = userService.getTotalUserCount();
        model.addAttribute("totalUsers", totalUser);
        int totalTrain = trainService.totalTrainsCount();
        model.addAttribute("totalTrains", totalTrain);
        return "adminPages/adminHomePage";
    }

    @GetMapping("/admin_all_trains")
    public String showTrains(Model model) {
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("allTrains", allTrains);

        return "adminPages/admin_all_trains";
    }

    @PostMapping("/addTrain")
    public String addTrain(@ModelAttribute Train train) {
        trainService.saveTrain(train);
        return "redirect:/admin/admin_all_trains";
    }

    @GetMapping("/editTrain/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Train train = trainService.getTrainById(id);
        model.addAttribute("train", train);
        return "adminPages/editTrain";
    }

    @GetMapping("/deleteTrain/{id}")
    public String deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return "redirect:/admin/admin_all_trains";
    }

    @GetMapping("/admin_all_routes")
    public String allRoutes(Model model) {
        List<Route> routeList = routeService.getAllRoutes();
        model.addAttribute("route", routeList);

        return "adminPages/admin_all_routes";
    }

    @GetMapping("/tickets")
    public String allTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);

        return "adminPages/tickets";
    }

    @GetMapping("/passengers")
    public String allTrains(Model model) {
        List<Passenger> passengers = passengerService.getAllPassengers();
        model.addAttribute("passengers", passengers);

        return "adminPages/passengers";
    }


    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        List<TrainStatus> schedules = trainStatusService.getAllTrainStatuses();
        model.addAttribute("schedules", schedules);
        return "adminPages/schedule";
    }
    @GetMapping("/users")
    public String allUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);

        return "adminPages/users";
    }

    //Started Operation On User

    @GetMapping("/userFormToAdd")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "adminPages/add_user";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user, BindingResult bindingResult, Model model) {


        // Check if email or username already exists
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists in the system");
        }
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists in the system");
        }

        // If there are validation errors, return to the form with error messages
        if (bindingResult.hasErrors()) {
            return "adminPages/add_user";
        }
        // Save the user if no validation errors
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/editUser/{id}")
    public String showUpdateForm(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "adminPages/editUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User updatedUser, BindingResult bindingResult, Model model) {
        // Validate the updated user (similar to your addUser method)

        // If there are validation errors, return to the form with error messages
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Your Information have error!!");
            return "adminPages/editUser";
        }

        // Update the user if no validation errors
        userService.updateUser(updatedUser);

        // Redirect to the user list page or wherever appropriate
        return "redirect:/admin/users";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable long id) {
        System.out.println("user deleted!!" + " " + id);
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    //Ended Operation On User

    //add route to the database
    @GetMapping("/routeForm")
    public String showFormToAddRoute(Model model) {
        model.addAttribute("route", new Route());
        return "adminPages/add_route";
    }

    @PostMapping("/addRoute")
    public String addRoute(@ModelAttribute Route route) {
        routeService.saveRoute(route);
        return "redirect:/admin/admin_all_routes";
    }
    @GetMapping("/editRoute/{id}")
    public String updateRoute(@PathVariable long id,Model model) {
    Route route=routeService.getRouteById(id);
    model.addAttribute("route",route);
        return "adminPages/editRoute";
    }

    @GetMapping("/deleteRoute/{id}")
    public String deleteRoute(@PathVariable long id) {
        routeService.deleteRoute(id);
        return "redirect:/admin/admin_all_routes";
    }

    @GetMapping("/trainForm")
    public String showTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "adminPages/add_train";
    }

    @GetMapping("/scheduleForm")
    public String showScheduleForm(Model model) {
        List<Train> trains = trainService.getAllTrains();
        model.addAttribute("trains", trains);
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        model.addAttribute("trainStatus", new TrainStatus());
        return "adminPages/add_schedule";
    }

    @PostMapping("/addSchedule")
    public String saveSchedule(@ModelAttribute TrainStatus trainStatus, Model model) {

        // Get the selected Buss based on the bussId from the form
        long trainId = trainStatus.getTrain().getId();
        Long routeId = trainStatus.getRoute().getId();
        Train selectedTrain = trainService.getTrainById(trainId);
        Route selectedRoute = routeService.getRouteById(routeId);
        boolean scheduleExists = trainStatusService.existsByTrainId(trainId);
        if (selectedTrain != null && selectedRoute != null && !scheduleExists) {
            System.out.println("the id is:" + selectedTrain.getId());
            trainStatus.setTrain(selectedTrain);
            trainStatusService.saveTrainStatus(trainStatus);
            return "redirect:/admin/schedule";

        } else {
            model.addAttribute("trains", trainService.getAllTrains());
            model.addAttribute("routes", routeService.getAllRoutes());
            model.addAttribute("trainStatus", trainStatus);
            System.out.println("error to use already bus");
            // Schedule with the same buss_id already exists, add an error message
            model.addAttribute("error", "A schedule for this Train already exists.");
            return "adminPages/add_schedule";
        }

    }
    @GetMapping("/editSchedule/{id}")
    public String updateSchedule(@PathVariable long id,Model model){
        List<Train> trains = trainService.getAllTrains();
        model.addAttribute("trains", trains);
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        TrainStatus trainStatus=trainStatusService.getTrainStatusByTrainId(id);
        model.addAttribute("schedule",trainStatus);
        return "adminPages/editSchedule";
    }

    @Transactional
    @GetMapping("/deleteSchedule/{id}")
    public String deleteSchedule(@PathVariable long id) {
        System.out.println("Schedule deleted by ID:" + id);
        trainStatusService.deleteTrainStatus(id);
        return "redirect:/admin/schedule";
    }
}

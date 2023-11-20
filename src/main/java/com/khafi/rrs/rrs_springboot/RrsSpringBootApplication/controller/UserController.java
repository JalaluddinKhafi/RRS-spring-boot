package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.controller;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.*;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    Long trianId;

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");
        String username = (String) session.getAttribute("username");
        Long id = (Long) session.getAttribute("id");
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("username", username);
        model.addAttribute("id", id);
        //model.addAttribute("id", id);
    }

    private final RouteServiceImpl routeService;
    private final UserServiceImpl userService;

    private final TrainServiceImpl trainService;
    private final TrainStatusServiceImpl trainStatusService;
    private final PassengerServiceImpl passengerService;
    private final TicketServiceImpl ticketService;

    public UserController(RouteServiceImpl routeService, UserServiceImpl userServicImpl, TrainServiceImpl trainService, TrainStatusServiceImpl trainStatusService, PassengerServiceImpl passengerService, TicketServiceImpl ticketService) {
        this.routeService = routeService;
        this.userService = userServicImpl;
        this.trainService = trainService;
        this.trainStatusService = trainStatusService;
        this.passengerService = passengerService;
        this.ticketService = ticketService;
    }

    @GetMapping("/createAccount")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "createAccount";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("errorMessage", "Username is already in use");
            return "redirect:/createAccount";
        }

        // Check if email already exists
        User existingEmailUser = userService.findByEmail(user.getEmail());
        if (existingEmailUser != null) {
            model.addAttribute("errorMessage", "Email is already in use");
            return "redirect:/createAccount";
        }

        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }



    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes attributes,
                        HttpSession session) {

        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("firstName", user.getFirstName());
            session.setAttribute("lastName", user.getLastName());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("id", user.getId());

            return "redirect:/userHomePage";
        } else {
            attributes.addAttribute("error", "Invalid username or password");
            return "redirect:/login";

        }
    }


    @GetMapping("/userHomePage")
    public String userHome(HttpSession session, Model model) {
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        System.out.println("welcome " + firstName + " " + lastName);
        return "userPages/userHomePage";
    }

    @GetMapping("/user_all_routes")
    public String allRoutes(Model model) {
        List<Route> routeList = routeService.getAllRoutes();
        model.addAttribute("route", routeList);
        return "userPages/user_all_routes";
    }


    @GetMapping("/user_all_trains")
    public String allTrains(Model model) {
        List<Train> allTrains = trainService.getAllTrains();
        model.addAttribute("allTrains", allTrains);
        return "userPages/user_all_trains";
    }

    @GetMapping("/userProfile")
    public String profile() {
        return "userPages/userProfile";
    }

    @RequestMapping("/userEdite/{id}")
    public String showUpdateTableUser(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "userPages/userEdite";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute User updatedUser, HttpSession session) {

                // Update the user information in the session
                session.setAttribute("userId", updatedUser.getId());
                session.setAttribute("firstName", updatedUser.getFirstName());
                session.setAttribute("lastName", updatedUser.getLastName());
                session.setAttribute("email", updatedUser.getEmail());
                session.setAttribute("username", updatedUser.getUsername());
                // Update other session attributes as needed
        // Update the user details in the database
        userService.saveUser(updatedUser);
        return "redirect:/userProfile";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        List<TrainStatus> schedules = trainStatusService.getAllTrainStatuses();
        model.addAttribute("schedules", schedules);
        return "userPages/schedule";
    }
    @GetMapping("/errorPage")
    public String showPopup(Model model) {
        model.addAttribute("errorMessage", "Not enough available seats for booking.");
        return "userPages/errorPage";
    }
    @GetMapping("/book/{id}")
    public String showPassengerForm(Model model, @PathVariable Long id){
        Train train = trainService.getTrainById(id);
        trianId = id;
        model.addAttribute("train", train);
        model.addAttribute("passenger", new Passenger());
        model.addAttribute("errorMessage", ""); // Add an empty error message initially
        return "userPages/passengerForm";
    }

    @PostMapping("/bookTicket")
    public String bookTicket(
            @ModelAttribute Passenger passenger,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("id");
        User user = userService.getUserById(userId);
        Train bookedTrain = trainService.getTrainById(trianId);

        int requestedSeats = passenger.getNumberOfSeat();
        int availableSeats = bookedTrain.getTrainStatus().getAvailableSeat();

        if (requestedSeats > availableSeats) {
            // Handle the case where the requested seats are more than available seats
            return "redirect:/errorPage"; // Redirect to the form page with the error message
        } else {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTrain(bookedTrain);
            ticket.setPassenger(passengerService.savePassenger(passenger));
            ticketService.bookTicket(ticket);

            return "redirect:/passengerTickets";
        }
    }

    @GetMapping("/passengerTickets")
    public String allTickets(Model model, HttpSession session) {
        Long userId=(Long) session.getAttribute("id");
        User user =userService.getUserById(userId);

        List<Ticket> tickets=user.getTickets();
        model.addAttribute("tickets",tickets);

        return "userPages/passengerTickets";
    }

//    @GetMapping("/cancelTicket/{id}")
//    public String cancelTicket(@PathVariable Long id, Model model) {
//        try {
//            // Attempt to cancel the ticket
//            ticketService.cancelTicket(id);
//
//            // If no exception occurred, assume the cancellation was successful
//            model.addAttribute("cancellationMessage", "Ticket successfully canceled.");
//        } catch (Exception e) {
//            // Handle the exception (e.g., log it)
//            e.printStackTrace();
//
//            // If an exception occurred, assume the cancellation failed
//            model.addAttribute("cancellationMessage", "Failed to cancel the ticket.");
//        }
//
//        // Redirect to the ticket list page
//        return "redirect:/passengerTickets";
//    }

    @GetMapping("/cancelTicket/{id}")
    public String showCancelConfirmation(@PathVariable Long id, Model model) {
        model.addAttribute("ticketId", id);
        return "userPages/cancelPage";
    }
    @GetMapping("/confirmCancelTicket/{id}")
    public String confirmCancelTicket(@PathVariable Long id) {
        // Call the service method to cancel the ticket
        ticketService.cancelTicket(id);

        // Redirect to the ticket list page or another appropriate page
        return "redirect:/passengerTickets";
    }




}
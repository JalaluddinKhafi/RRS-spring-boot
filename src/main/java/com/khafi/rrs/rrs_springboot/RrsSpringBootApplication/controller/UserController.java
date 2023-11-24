package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.controller;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.*;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    Long trianId;

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");
        String username = (String) session.getAttribute("username");
        Long id = (Long) session.getAttribute("id");
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
//
//    @GetMapping("/createAccount")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User());
//        return "createAccount";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") User user, Model model) {
//        User existingUser = userService.findByUsername(user.getUsername());
//        if (existingUser != null) {
//            model.addAttribute("errorMessage", "Username is already in use");
//            return "redirect:/user/createAccount";
//        }
//
//        // Check if email already exists
//        User existingEmailUser = userService.findByEmail(user.getEmail());
//        if (existingEmailUser != null) {
//            model.addAttribute("errorMessage", "Email is already in use");
//            return "redirect:/user/createAccount";
//        }
//
//        userService.saveUser(user);
//        return "redirect:/user/login";
//    }
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//
//
//    @PostMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password,
//                        RedirectAttributes attributes,
//                        HttpSession session) {
//
//        User user = userService.findByUsername(username);
//        if (user != null && user.getPassword().equals(password)) {
//            session.setAttribute("firstName", user.getFirstName());
//            session.setAttribute("lastName", user.getLastName());
//            session.setAttribute("email", user.getEmail());
//            session.setAttribute("password", user.getPassword());
//            session.setAttribute("username", user.getUsername());
//            session.setAttribute("id", user.getId());
//
//            return "redirect:/user/userHomePage";
//        } else {
//            attributes.addAttribute("error", "Invalid username or password");
//            return "redirect:/user/login";
//
//        }
//    }


    @GetMapping("/userHomePage")
    public String userHome(Model model) {
        List<TrainStatus> schedules = trainStatusService.getAllTrainStatuses();
        model.addAttribute("schedules", schedules);
        return "userPages/userHomePage";
    }
    @PostMapping("/search")
    public String searchTrains(@RequestParam String source,
                               @RequestParam String destination,
                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate,
                               @RequestParam int numberOfSeats,
                               Model model) {
        List<TrainStatus> matchingSchedules = trainStatusService.searchSchedules(source, destination, departureDate, numberOfSeats);
        model.addAttribute("schedules", matchingSchedules);
        System.out.println("source form:"+source);
        System.out.println("destination form:"+destination);
        System.out.println("departure date form:"+departureDate);
        System.out.println("number of seats form:"+numberOfSeats);
        return "fragments/topNav::userScheduleFragment"; // Return the updated schedule fragment
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
            return "redirect:/user/errorPage"; // Redirect to the form page with the error message
        } else {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTrain(bookedTrain);
            ticket.setPassenger(passengerService.savePassenger(passenger));
            ticketService.bookTicket(ticket);

            return "redirect:/user/passengerTickets";
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
        return "redirect:/user/passengerTickets";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/index";
    }

    @GetMapping("/about")
    public String about() {
        return "userPages/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "userPages/contact";
    }



}
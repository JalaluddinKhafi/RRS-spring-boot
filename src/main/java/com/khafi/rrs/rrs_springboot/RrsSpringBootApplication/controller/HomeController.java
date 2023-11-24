package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.controller;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.RouteServiceImpl;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.TrainStatusServiceImpl;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl.UserServiceImpl;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Route;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.TrainStatus;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    TrainStatusServiceImpl trainStatusService;
    @Autowired
    RouteServiceImpl routeService;
    @Autowired
    UserServiceImpl userService;
    @GetMapping("/index")
    public String index(Model model) {
        List<TrainStatus> schedules = trainStatusService.getAllTrainStatuses();
        model.addAttribute("schedules", schedules);
        return "index";
    }
    @GetMapping("/")
    public String showIndexPage(){
        return "redirect:/index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contcat() {
        return "contact";
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
            System.out.println("register page is started !!" + "Username is already in use");

            return "redirect:/createAccount";
        }

        // Check if email already exists
        User existingEmailUser = userService.findByEmail(user.getEmail());
        if (existingEmailUser != null) {
            model.addAttribute("errorMessage", "Email is already in use");
            System.out.println("register page is started !!! "+" email is already in use");
            return "redirect:/createAccount";
        }

        userService.saveUser(user);
        System.out.println("register page is started !!! "+" User successfully added to the database");

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        System.out.println("show login page is started !!");
        return "login";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes attributes,
                        HttpSession session) {

        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login page is started !!! "+" Password"+user.getPassword());
            session.setAttribute("id",user.getId());
            return "redirect:/user/userHomePage";

        } else {
            attributes.addAttribute("error", "Invalid username or password");
            System.out.println("Login page is started but has error !!! "+" Password"+user.getPassword());
            return "redirect:/login";

        }
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
        return "fragments/topNav::scheduleFragment"; // Return the updated schedule fragment
    }

}

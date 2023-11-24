package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Passenger;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Ticket;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Train;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.TrainStatus;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.TicketRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.PassengerService;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.TicketService;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.TrainStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TrainStatusService trainService;
    private final PassengerService passengerService;
    private final EmailService emailService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TrainStatusService trainService, PassengerService passengerService, EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.trainService = trainService;
        this.passengerService = passengerService;
        this.emailService = emailService;
    }

    // ... other methods ...

    @Override
    @Transactional
    public void bookTicket(Ticket ticket) {
        TrainStatus trainStatus = ticket.getTrain().getTrainStatus();
        Train train = ticket.getTrain();
        int numberOfSeats = ticket.getPassenger().getNumberOfSeat();

        // Ensure that bookedSeat and availableSeat are not null
        int currentBookedSeats = trainStatus.getBookedSeat() != null ? trainStatus.getBookedSeat() : 0;
        int currentAvailableSeats = trainStatus.getAvailableSeat() != null ? trainStatus.getAvailableSeat() : 0;

        // Check if there are enough available seats
        if (currentAvailableSeats < numberOfSeats) {
            System.out.println("Not enough available seats for booking.");
        }else {

            // Calculate the total price based on the number of seats and price per seat
            double pricePerSeat = train.getPrice() != null ? train.getPrice() : 0.0;
            double totalPrice = numberOfSeats * pricePerSeat;

            // Update booked seats and available seats in TrainStatus
            trainStatus.setBookedSeat(currentBookedSeats + numberOfSeats);
            trainStatus.setAvailableSeat(currentAvailableSeats - numberOfSeats);

            // Save the modified TrainStatus
            trainService.saveTrainStatus(trainStatus);

            // Save passenger information with calculated total price
            Passenger passenger = ticket.getPassenger();
            passenger.setTotalPrice(totalPrice);
            passengerService.savePassenger(passenger);

            // Save the ticket
            ticketRepository.save(ticket);
            emailService.sendTicketConfirmationEmail(ticket.getUser().getEmail(), ticket);
        }
    }
    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));

        TrainStatus trainStatus = ticket.getTrain().getTrainStatus();
        int numberOfSeats = ticket.getPassenger().getNumberOfSeat();

        // Update booked seats and available seats in TrainStatus
        trainStatus.setBookedSeat(trainStatus.getBookedSeat() - numberOfSeats);
        trainStatus.setAvailableSeat(trainStatus.getAvailableSeat() + numberOfSeats);

        // Save the modified TrainStatus
        trainService.saveTrainStatus(trainStatus);

        // Remove passenger information
        Passenger passenger = ticket.getPassenger();
        passengerService.deletePassenger(passenger.getId());

        // Delete the ticket
        // You might have a TicketRepository, delete the ticket accordingly
        ticketRepository.delete(ticket);
    }




}

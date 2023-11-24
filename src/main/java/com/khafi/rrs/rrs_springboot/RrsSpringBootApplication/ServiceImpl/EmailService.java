package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTicketConfirmationEmail(String to, Ticket ticket) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Ticket Confirmation - " + ticket.getTrain().getName());

        String ticketInfo = "Ticket ID: " + ticket.getId() + "\n" +
                "Passenger Name: " + ticket.getPassenger().getFirstName() + "\n" +
                "Passenger Last Name: " + ticket.getPassenger().getLastName() + "\n" +
                "Source: " + ticket.getTrain().getTrainStatus().getRoute().getSource()+ "\n" +
                "Destination: " + ticket.getTrain().getTrainStatus().getRoute().getDestination() + "\n" +
                "Journey Date: " + ticket.getTrain().getTrainStatus().getDepartureDate() + "\n" +
                "Journey Time: " + ticket.getTrain().getTrainStatus().getDepartureTime() + "\n" +
                "Total Price: " + ticket.getPassenger().getTotalPrice() +ticket.getTrain().getCurrencySymbol() + "\n";

        mailMessage.setText("Dear " + ticket.getUser().getUsername() + ",\n\n"
                + "Thank you for booking your ticket with us!\n\n"
                + ticketInfo
                + "\n\nSafe travels!");

        mailMessage.setFrom("khafi98117@gmail.com");

        javaMailSender.send(mailMessage);
    }
}
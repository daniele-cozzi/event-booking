package org.elis.event_booking.service.implementations;

import org.elis.event_booking.model.Booking;
import org.elis.event_booking.model.Email;
import org.elis.event_booking.model.EventDate;
import org.elis.event_booking.model.User;
import org.elis.event_booking.service.definitions.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendBookingConfirmationEmail(Booking booking) {
        User user = booking.getUser();
        EventDate eventDate = booking.getEventDate();

        LocalDate startDate = eventDate.getStart().toLocalDate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalTime startTime = eventDate.getStart().toLocalTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Email email = new Email();
        email.setRecipient(user.getEmail());
        email.setSubject("Conferma prenotazione per " + eventDate.getEvent().getName());
        email.setBody(
                "Gentile " + user.getName() + " " + user.getSurname() + ",\n\n" +
                "la tua prenotazione per l'evento " + eventDate.getEvent().getName() + " è stata confermata con successo.\n\n" +
                "Dettagli della prenotazione:\n" +
                "-  data: " + startDate.format(dateFormatter) + "\n" +
                "-  orario: " + startTime.format(timeFormatter) + "\n" +
                "-  luogo: " + eventDate.getPlace().getName() + "\n" +
                "-  numero di prenotazione: " + booking.getId() + "\n\n" +
                "Grazie per aver scelto di partecipare all'evento. Attendiamo con impazienza di darti il benvenuto.\n\n" +
                "Cordiali saluti,\n\n" +
                "Event Booking Team"
        );

        return sendEmail(email);
    }

    @Override
    public boolean sendBookingDisabledEmail(Booking booking) {
        User user = booking.getUser();
        EventDate eventDate = booking.getEventDate();

        LocalDate startDate = eventDate.getStart().toLocalDate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalTime startTime = eventDate.getStart().toLocalTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Email email = new Email();
        email.setRecipient(user.getEmail());
        email.setSubject("Annullamento prenotazione per " + eventDate.getEvent().getName());
        email.setBody(
                "Gentile " + user.getName() + " " + user.getSurname() + ",\n\n" +
                "la tua prenotazione per l'evento " + eventDate.getEvent().getName() + " è stata annullata con successo.\n\n" +
                "Dettagli della prenotazione:\n" +
                "-  data: " + startDate.format(dateFormatter) + "\n" +
                "-  orario: " + startTime.format(timeFormatter) + "\n" +
                "-  luogo: " + eventDate.getPlace().getName() + "\n" +
                "-  numero di prenotazione: " + booking.getId() + "\n\n" +
                "Ci dispiace che tu abbia annullato la prenotazione. Speriamo di vederti presto ad un altro evento.\n\n" +
                "Cordiali saluti,\n\n" +
                "Event Booking Team"
        );

        return sendEmail(email);
    }

    private boolean sendEmail(Email email) {
        try {
            MimeMessagePreparator mailMessage = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                message.setFrom(sender);
                message.setTo(email.getRecipient());
                message.setSubject(email.getSubject());
                message.setText(email.getBody());
            };

            mailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            // error while sending mail
            return false;
        }
    }
}

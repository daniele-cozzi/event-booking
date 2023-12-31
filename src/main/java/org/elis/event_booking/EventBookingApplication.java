package org.elis.event_booking;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.model.Role;
import org.elis.event_booking.security.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@SpringBootApplication
public class EventBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBookingApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) throws IOException {
		final String SUPER_ADMIN_EMAIL = "superadmin@gmail.com";
		final String SUPER_ADMIN_PASSWORD = "superadmin";

		final Logger logger = LogManager.getLogger(EventBookingApplication.class.getName());

		// I create this file on first boot
		String path = "first-boot.txt";
		File file = new File(path);

		if (!file.exists()) {
			// I create the super admin account
			UserDTO superAdminDTO = new UserDTO(0L, "SuperAdmin", "SuperAdmin", SUPER_ADMIN_EMAIL, SUPER_ADMIN_PASSWORD, LocalDate.now(), "AAAAAAAAAAAA", Role.SUPER_ADMIN.name(), true, 0L);

			authenticationService.registerSuperAdmin(superAdminDTO);

			// I write the super admin credentials to the file
			try (FileWriter writer = new FileWriter(path)) {
				writer.write("Super admin account" + "\n");
				writer.write("Email: " + SUPER_ADMIN_EMAIL + "\n");
				writer.write("Password: " + SUPER_ADMIN_PASSWORD + "\n");

				logger.log(Level.INFO, "'first-boot.txt' file created.");

				// I set the file as hidden
				Runtime.getRuntime().exec("attrib +H first-boot.txt");
			} catch (IOException e) {
				logger.log(Level.ERROR, "Error while trying to write the super admin credentials to the file.");
			}
		}

		// I print the super admin credentials to the console
		logger.log(Level.INFO, "Super admin account - email: " + SUPER_ADMIN_EMAIL + " , password: " + SUPER_ADMIN_PASSWORD);

		return null;
	}
}

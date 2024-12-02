package es.deusto.sd.strava;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.deusto.sd.strava.dao.TrainingSessionRepository;
import es.deusto.sd.strava.dao.UserRepository;
import es.deusto.sd.strava.entity.User;
import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {
	
	Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
	@Bean
	@Transactional
	CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            // Database is already initialized
            if (userRepository.count() > 0) {                
                return;
            }			
            
            // Create some users
            User johnDoe = new User("john.doe@gmail.com", "JohnDoe", LocalDate.parse("1990-01-01"), 80.0f, 180.0f, 190, 60);
            User janeDoe = new User("jane.doe@gmail.com", "JaneDoe", LocalDate.parse("1992-01-01"), 60.0f, 160.0f, 180, 70);
            User alice = new User("alice@gmail.com", "Alice", LocalDate.parse("1995-01-01"), 70.0f, 170.0f, 200, 50);
            //User bob = new User("bob@gmail.com", "Bob", LocalDate.parse("1998-01-01"), 90.0f, 190.0f, 210, 55);
            
            userRepository.saveAll(List.of(johnDoe, janeDoe, alice));
            
            logger.info("Users saved");
            
            // Initialize training sessions
            //TrainingSession ts1 = new TrainingSession();
        };
    }

}

package es.deusto.sd.strava;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.deusto.sd.strava.dao.ChallengeRepository;
import es.deusto.sd.strava.dao.TrainingSessionRepository;
import es.deusto.sd.strava.dao.UserRepository;
import es.deusto.sd.strava.entity.Challenge;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;
import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {
	
	Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
	@Bean
	@Transactional
	CommandLineRunner initData(UserRepository userRepository, TrainingSessionRepository trainingSessionRepository, ChallengeRepository challengeRepository) {
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
            logger.info("Users created");
            
            //Create some training sessions
            TrainingSession ts1 = new TrainingSession(johnDoe.getId(), "Morning run", "Running", 10.0f, LocalDate.parse("2024-04-24"), LocalTime.parse("06:00:00"), 3600, LocalDateTime.now());
            TrainingSession ts2 = new TrainingSession(johnDoe.getId(), "Afternoon run", "Running", 5.0f, LocalDate.parse("2024-07-28"), LocalTime.parse("18:00:00"), 1800, LocalDateTime.now());
            TrainingSession ts3 = new TrainingSession(janeDoe.getId(), "Night cycling", "Cycling", 15.0f, LocalDate.parse("2024-06-24"), LocalTime.parse("23:13:00"), 5400, LocalDateTime.now());
            
            trainingSessionRepository.saveAll(List.of(ts1, ts2, ts3));
            logger.info("Training sessions created");
            
            // Create some challenges
            Challenge c1 = new Challenge("Running 100km in a month", "Running", 100.0f, null, LocalDate.parse("2024-04-01"), LocalDate.parse("2025-04-30"), LocalDateTime.now(), johnDoe.getId());
            Challenge c2 = new Challenge("Cycling 500km in a month", "Cycling", 500.0f, null, LocalDate.parse("2024-06-01"), LocalDate.parse("2025-06-30"), LocalDateTime.now(), janeDoe.getId());
            
            challengeRepository.saveAll(List.of(c1, c2));
            logger.info("Challenges created");
        };
    }

}
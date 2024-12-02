package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dao.TrainingSessionRepository;
import es.deusto.sd.strava.dto.TrainingSessionDTO;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for managing training sessions.
 */
@Service
public class TrainingSessionService {    
    private final TrainingSessionRepository trainingSessionRepository;
    
    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
    	this.trainingSessionRepository = trainingSessionRepository;
    }

    /**
     * Adds a new training session
     *
     * @param dto   Session data.
     * @param user  User that creates the session.
     * @return Session created.
     */
    public TrainingSession addTrainingSession(TrainingSessionDTO dto, User user) {
        TrainingSession session = new TrainingSession(
                user.getId(),
                dto.getTitle(),
                dto.getSport(),
                dto.getDistance(),
                dto.getStartDate(),
                dto.getStartTime(),
                dto.getDuration(),
                LocalDateTime.now()
        );

        trainingSessionRepository.save(session);
        return session;
    }

    /**
     * Retrieves a user's training sessions, optionally filtered by date.
     *
     * @param user      User
     * @param startDate Start date (optional).
     * @param endDate   End date (optional).
     * @return Session list.
     */
    public List<TrainingSession> getTrainingSessions(User user, LocalDate startDate, LocalDate endDate) {
        List<TrainingSession> sessions = trainingSessionRepository.findByUserId(user.getId());

        return sessions.stream()
                .filter(session -> {
                    boolean afterStart = (startDate == null) || !session.getStartDate().isBefore(startDate);
                    boolean beforeEnd = (endDate == null) || !session.getStartDate().isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .sorted(Comparator.comparing(TrainingSession::getStartDate).reversed())
                .toList();
    }
}

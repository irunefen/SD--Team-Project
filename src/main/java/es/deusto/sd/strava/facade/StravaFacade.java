package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.*;
import es.deusto.sd.strava.entity.Challenge;
import es.deusto.sd.strava.entity.ChallengeProgress;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.ChallengeService;
import es.deusto.sd.strava.service.TrainingSessionService;
import es.deusto.sd.strava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade for Strava.
 */
@Component
public class StravaFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TrainingSessionService trainingSessionService;

    @Autowired
    private ChallengeService challengeService;

    // Methods for registering, login...

    public RegistrationStatusDTO registerUser(RegisterUserDTO dto, String authProviderName) {
        boolean isRegistered = userService.isEmailRegistered(dto.getEmail(), authProviderName);
        if (isRegistered) {
            return new RegistrationStatusDTO("Conflict", "Email already registered with this provider.", null);
        }

        User user = userService.createUser(dto, authProviderName);
        if (user == null) {
            return new RegistrationStatusDTO("Conflict", "Email already registered.", null);
        }

        return new RegistrationStatusDTO("Created", "User registrated succesfully.", user.getUserId());
    }

    public AuthTokenDTO loginUser(String email, String password, String authProviderName) {
        User user = userService.authenticateUser(email, password, authProviderName);
        if (user != null) {
            String token = authService.generateToken(user);
            return new AuthTokenDTO(token);
        }
        return null;
    }

    public LogoutConfirmationDTO logoutUser(String token) {
        authService.invalidateToken(token);
        return new LogoutConfirmationDTO("OK", "Logout successful.");
    }

    public TrainingSessionDetailsDTO createTrainingSession(String token, TrainingSessionDTO dto) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        TrainingSession session = trainingSessionService.addTrainingSession(dto, user);
        return new TrainingSessionDetailsDTO(
                session.getSessionId(),
                session.getTitle(),
                session.getSport(),
                session.getDistance(),
                session.getStartDate(),
                session.getStartTime(),
                session.getDuration(),
                session.getCreatedAt()
        );
    }

    public List<TrainingSessionResponseDTO> queryTrainingSessions(String token, LocalDate startDate, LocalDate endDate) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        return trainingSessionService.retrieveTrainingSessions(user, startDate, endDate);
    }

    public ChallengeDetailsDTO setupChallenge(String token, ChallengeRegistrationDTO dto) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        Challenge challenge = challengeService.createChallenge(dto, user);
        return new ChallengeDetailsDTO(
                challenge.getChallengeId(),
                challenge.getName(),
                challenge.getSport(),
                challenge.getTargetDistance(),
                challenge.getTargetTime(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getCreatedAt()
        );
    }

    public List<ChallengeResponseDTO> getActiveChallenges(String token, String sport, LocalDate startDate, LocalDate endDate, Integer limit) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        
        int actualLimit = (limit != null) ? limit : 5;
        LocalDate start = startDate;
        LocalDate end = endDate;

        List<Challenge> challenges = challengeService.fetchActiveChallenges(sport, start, end, actualLimit);
        return challenges.stream()
                .map(ch -> new ChallengeResponseDTO(
                        ch.getChallengeId(),
                        ch.getName(),
                        ch.getSport(),
                        ch.getTargetDistance(),
                        ch.getTargetTime(),
                        ch.getStartDate(),
                        ch.getEndDate(),
                        ch.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public AcceptanceConfirmationDTO acceptChallenge(String token, String challengeId) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        boolean success = challengeService.registerChallengeAcceptance(user, challengeId);
        if (success) {
            return new AcceptanceConfirmationDTO("OK", "Challenge succesfully accepted", challengeId);
        } else {
            return new AcceptanceConfirmationDTO("Not Found", "Challenge not found.", challengeId);
        }
    }

    public List<ChallengeResponseDTO> getAcceptedChallenges(String token) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        List<Challenge> challenges = challengeService.getUserAcceptedChallenges(user);
        return challenges.stream()
                .map(ch -> new ChallengeResponseDTO(
                        ch.getChallengeId(),
                        ch.getName(),
                        ch.getSport(),
                        ch.getTargetDistance(),
                        ch.getTargetTime(),
                        ch.getStartDate(),
                        ch.getEndDate(),
                        ch.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<ChallengeProgress> getChallengeStatus(String token, List<TrainingSession> trainingSessions) {
        User user = authService.validateToken(token);
        if (user == null) {
            return null;
        }
        return challengeService.calculateChallengeProgress(user, trainingSessions);
    }
}

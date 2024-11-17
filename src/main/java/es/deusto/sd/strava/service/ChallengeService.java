package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dto.ChallengeRegistrationDTO;
import es.deusto.sd.strava.entity.Challenge;
import es.deusto.sd.strava.entity.ChallengeProgress;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for managing challenges.
 */
@Service
public class ChallengeService {

    // Simulated database
    private Map<String, Challenge> challengesById = new HashMap<>();
    private Map<String, Set<String>> acceptedChallengesByUserId = new HashMap<>();

    /**
     * Create a new challenge
     *
     * @param dto   Challenge data.
     * @param user  User that creates the challenge.
     * @return Challenge created.
     */
    public Challenge createChallenge(ChallengeRegistrationDTO dto, User user) {
        String challengeId = UUID.randomUUID().toString();
        Challenge challenge = new Challenge(
                challengeId,
                dto.getName(),
                dto.getSport(),
                dto.getTargetDistance(),
                dto.getTargetTime(),
                dto.getStartDate(),
                dto.getEndDate(),
                LocalDateTime.now(),
                user.getUserId()
        );

        challengesById.put(challengeId, challenge);
        return challenge;
    }

    /**
     * Retrieve active challenges, optionally filtered by sport and date.
     *
     * @param sport     Sport (optional).
     * @param startDate Start date (optional).
     * @param endDate   End date (optional).
     * @param limit     Result limit.
     * @return List of active challenges.
     */
    public List<Challenge> getActiveChallenges(String sport, LocalDate startDate, LocalDate endDate, int limit) {
        LocalDate today = LocalDate.now();
        return challengesById.values().stream()
                .filter(challenge -> !challenge.getEndDate().isBefore(today))
                .filter(challenge -> (sport == null || challenge.getSport().equalsIgnoreCase(sport)))
                .filter(challenge -> {
                    boolean afterStart = (startDate == null) || !challenge.getStartDate().isBefore(startDate);
                    boolean beforeEnd = (endDate == null) || !challenge.getEndDate().isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .sorted(Comparator.comparing(Challenge::getCreatedAt).reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Registers the acceptance of a challenge by a user
     *
     * @param user         User that accepts the challenge.
     * @param challengeId  Challenge ID.
     * @return true if it registered successfully, false otherwise.
     */
    public boolean registerChallengeAcceptance(User user, String challengeId) {
        if (!challengesById.containsKey(challengeId)) {
            return false;
        }
        acceptedChallengesByUserId.computeIfAbsent(user.getUserId(), k -> new HashSet<>()).add(challengeId);
        return true;
    }

    /**
     * Retrieves the challenges accepted by a user.
     *
     * @param user User.
     * @return List of accepted challenges.
     */
    public List<Challenge> getUserAcceptedChallenges(User user) {
        Set<String> challengeIds = acceptedChallengesByUserId.getOrDefault(user.getUserId(), Collections.emptySet());
        return challengeIds.stream()
                .map(challengesById::get)
                .filter(Objects::nonNull)
                .toList();
    }
    
    /**
     * Retrieves the active challenges accepted by a user.
     *
     * @param user User.
     * @return List of active accepted challenges.
     */
    public List<Challenge> getUserAcceptedActiveChallenges(User user) {
        Set<String> challengeIds = acceptedChallengesByUserId.getOrDefault(user.getUserId(), Collections.emptySet());
        
        LocalDate today = LocalDate.now();
        return challengeIds.stream()
                .map(challengesById::get)
                .filter(Objects::nonNull)
                .filter(challenge -> !challenge.getEndDate().isBefore(today))
                .toList();       
    }

    /**
     * Calculates the progress of the challenges accepted
     *
     * @param user             User
     * @param trainingSessions User training sessions
     * @return List of challenge progresses of the user
     */
    public List<ChallengeProgress> calculateChallengeProgresses(User user, List<TrainingSession> trainingSessions) {
        List<Challenge> activeAcceptedChallenges = getUserAcceptedActiveChallenges(user);
        List<ChallengeProgress> progresses = new ArrayList<>();

        for (Challenge challenge : activeAcceptedChallenges) {
            float total = trainingSessions.stream()
            		.filter(session -> session.getSport().equalsIgnoreCase(challenge.getSport()))
                    .filter(session -> {
	                    boolean afterStart = !session.getStartDate().isBefore(challenge.getStartDate());
	                    boolean beforeEnd = !session.getStartDate().isAfter(challenge.getEndDate());
	                    return afterStart && beforeEnd;
                    })
                    .map(session -> {
                        if (challenge.getTargetDistance() != null) {
                            return session.getDistance();
                        } else if (challenge.getTargetTime() != null) {
                            return (float) session.getDuration();
                        }
                        return 0f;
                    })
                    .reduce(0f, Float::sum);

            float target = (challenge.getTargetDistance() != null) ? challenge.getTargetDistance() :
                    (challenge.getTargetTime() != null) ? (float) challenge.getTargetTime().getSecond() : 1f;

            float percentage = Math.min((total / target) * 100, 100);

            ChallengeProgress progress = new ChallengeProgress(
                    challenge.getChallengeId(),
                    total,
                    percentage,
                    user.getUserId()
            );
            progresses.add(progress);
        }

        return progresses;
    }
}


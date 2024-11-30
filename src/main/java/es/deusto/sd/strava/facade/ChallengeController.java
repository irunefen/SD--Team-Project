package es.deusto.sd.strava.facade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.strava.dto.ChallengeRegistrationDTO;
import es.deusto.sd.strava.dto.ChallengeResponseDTO;
import es.deusto.sd.strava.entity.Challenge;
import es.deusto.sd.strava.entity.ChallengeProgress;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.ChallengeService;
import es.deusto.sd.strava.service.TrainingSessionService;
import es.deusto.sd.strava.util.TokenUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
@Tag(name = "Challenges", description = "Operations for managing user challenges")
public class ChallengeController {
	
    private final ChallengeService challengeService;
    private final AuthService authService;
    private final TrainingSessionService trainingSessionService;
    
	public ChallengeController(ChallengeService challengeService, AuthService authService, TrainingSessionService trainingSessionService) {
		this.challengeService = challengeService;
		this.authService = authService;
		this.trainingSessionService = trainingSessionService;
	}

    @PostMapping
    @Operation(
        summary = "Setup Challenge",
        description = "Allows the user to set up a new challenge.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: Challenge set up successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided"),
        }
    )
    public ResponseEntity<?> setupChallenge(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChallengeRegistrationDTO dto) {        
        
        User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		if (dto.getTargetDistance() != null && dto.getTargetTime() != null) 
			return new ResponseEntity<>(Map.of("message", "The challenge cannot contain both target distance and target time"), HttpStatus.BAD_REQUEST);
	
        Challenge challenge = challengeService.createChallenge(dto, user);
        if (challenge == null) return new ResponseEntity<>(Map.of("message", "Invalid data."), HttpStatus.BAD_REQUEST);
        
        return new ResponseEntity<>(challenge, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    @Operation(
        summary = "Get Active Challenges",
        description = "Retrieves a list of active challenges for the user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Active challenges retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No active challenges found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided")
        }
    )
    public ResponseEntity<?> getActiveChallenges(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(name = "sport", description = "Sport type filter (optional)")
            @RequestParam(name = "sport", required = false) String sport,
            @Parameter(name = "startDate", description = "Start date for filtering challenges (optional)", example = "2023-01-01")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(name = "endDate", description = "End date for filtering challenges (optional)", example = "2023-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(name = "limit", description = "Limit for the number of challenges returned (optional)", example = "5")
            @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit) {

    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
        List<ChallengeResponseDTO> challenges = challengeService.getActiveChallenges(sport, startDate, endDate, limit).stream()
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
                .toList();
        
        if (challenges == null || challenges.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return ResponseEntity.ok(challenges);
    }


    @PostMapping("/accepted")
    @Operation(
        summary = "Accept Challenge",
        description = "Allows the user to accept a challenge by its ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Challenge accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found: Challenge not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided"),
        }
    )
    public ResponseEntity<?> acceptChallenge(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(name = "challengeId", description = "ID of the challenge to accept", required = true)
            @RequestParam("challengeId") String challengeId) {

    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        boolean success = challengeService.registerChallengeAcceptance(user, challengeId);
        
		if (!success) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(HttpStatus.OK);       
    }
    
    @GetMapping("/accepted")
    @Operation(
		summary = "Get accepted challenges", 
		description = "Retrieve the list of challenges that the authenticated user has accepted.",
		responses = {
	            @ApiResponse(responseCode = "200", description = "OK: Accepted challenges retrieved successfully"),
	            @ApiResponse(responseCode = "204", description = "Not content: No accepted challenges found"),
	            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided"), }
		)
    public ResponseEntity<?> getAcceptedChallenges(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		List<ChallengeResponseDTO> acceptedChallenges = challengeService.getUserAcceptedChallenges(user).stream()
				.map(ch -> new ChallengeResponseDTO(
						ch.getChallengeId(), 
						ch.getName(), 
						ch.getSport(),
						ch.getTargetDistance(), 
						ch.getTargetTime(), 
						ch.getStartDate(), 
						ch.getEndDate(),
						ch.getCreatedAt()))
				.toList();
		
        if (acceptedChallenges.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        return ResponseEntity.ok(acceptedChallenges);
    }
    
    @GetMapping("/status")
    @Operation(
    		summary = "Get challenge status", 
    		description = "Retrieve the status of all challenges that the authenticated user is participating in.", 
			responses = {
					@ApiResponse(responseCode = "200", description = "OK: Challenge status retrieved successfully"),
					@ApiResponse(responseCode = "204", description = "No content: No challenge progress found"),
					@ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided"), }
    		)
    public ResponseEntity<?> getChallengeStatus(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		List<Challenge> activeChallenges = challengeService.getUserAcceptedActiveChallenges(user);
		if (activeChallenges.isEmpty())	return new ResponseEntity<>(HttpStatus.NO_CONTENT);		
		
		List<TrainingSession> trainingSessions = trainingSessionService.getTrainingSessions(user, null, null);		
        List<ChallengeProgress> challengeProgresses = challengeService.calculateChallengeProgresses(user, trainingSessions);
        
        return ResponseEntity.ok(challengeProgresses);
    }
}

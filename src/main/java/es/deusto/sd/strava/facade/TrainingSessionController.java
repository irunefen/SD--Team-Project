package es.deusto.sd.strava.facade;


import es.deusto.sd.strava.dto.TrainingSessionDTO;
import es.deusto.sd.strava.dto.TrainingSessionResponseDTO;
import es.deusto.sd.strava.entity.TrainingSession;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.TrainingSessionService;
import es.deusto.sd.strava.util.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/training-sessions")
@Tag(name = "Training Sessions", description = "Operations related to managing training sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;
    private final AuthService authService;

    public TrainingSessionController(TrainingSessionService trainingSessionService, AuthService authService) {
        this.trainingSessionService = trainingSessionService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(
        summary = "Create Training Session",
        description = "Creates a new training session for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: Training session created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided")
        }
    )
    public ResponseEntity<?> createTrainingSession(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TrainingSessionDTO dto) {

    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        TrainingSession session = trainingSessionService.addTrainingSession(dto, user);
        
		TrainingSessionResponseDTO sessionDetails = new TrainingSessionResponseDTO(
				session.getId(), 
				session.getTitle(),
				session.getSport(), 
				session.getDistance(), 
				session.getStartDate(), 
				session.getStartTime(),
				session.getDuration(), 
				session.getCreatedAt()
			);
		
        return new ResponseEntity<>(sessionDetails, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(
        summary = "Query Training Sessions",
        description = "Retrieves a list of training sessions for the authenticated user within an optional date range.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: List of training sessions retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No training sessions found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided")
        }
    )
    public ResponseEntity<?> queryTrainingSessions(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(name = "startDate", description = "Start date for filtering sessions (optional)", example = "2023-01-01")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(name = "endDate", description = "End date for filtering sessions (optional)", example = "2023-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    	User user = authService.getUserFromToken(TokenUtils.extractToken(authorizationHeader));
		if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<TrainingSessionResponseDTO> sessions = trainingSessionService.getTrainingSessions(user, startDate, endDate).stream()
        		.map(session -> new TrainingSessionResponseDTO(
		                session.getId(),
		                session.getTitle(),
		                session.getSport(),
		                session.getDistance(),
		                session.getStartDate(),
		                session.getStartTime(),
		                session.getDuration(),
		                session.getCreatedAt()
        		))
        		.toList();

        
        if (sessions.isEmpty())return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(sessions);
    }
}


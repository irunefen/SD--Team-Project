package es.deusto.sd.strava.controller;


import es.deusto.sd.strava.dto.TrainingSessionDTO;
import es.deusto.sd.strava.dto.TrainingSessionDetailsDTO;
import es.deusto.sd.strava.dto.TrainingSessionResponseDTO;
import es.deusto.sd.strava.facade.StravaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/training-sessions")
@Tag(name = "Training Sessions", description = "Operations related to managing training sessions")
public class TrainingSessionController {

    private final StravaFacade facade;

    @Autowired
    public TrainingSessionController(StravaFacade facade) {
        this.facade = facade;
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

        String token = extractToken(authorizationHeader);

        if (token == null) {
            return new ResponseEntity<>(
                    Map.of("status", "Unauthorized", "message", "Invalid token."),
                    HttpStatus.UNAUTHORIZED
            );
        }

        TrainingSessionDetailsDTO session = facade.createTrainingSession(token, dto);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(
                    Map.of("status", "Unauthorized", "message", "Invalid token."),
                    HttpStatus.UNAUTHORIZED
            );
        }
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

        String token = extractToken(authorizationHeader);
        if (token == null) {
            return new ResponseEntity<>(
                    Map.of("status", "Unauthorized", "message", "Invalid token."),
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<TrainingSessionResponseDTO> sessions = facade.queryTrainingSessions(token, startDate, endDate);
        if (sessions == null || sessions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(sessions);
    }

    /**
     * Extracts the token from the Authorization header.
     *
     * @param header Authorization header.
     * @return Token or null if invalid.
     */
    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}


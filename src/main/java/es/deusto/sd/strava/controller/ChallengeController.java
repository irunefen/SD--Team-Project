package es.deusto.sd.strava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.strava.dto.ChallengeDetailsDTO;
import es.deusto.sd.strava.dto.ChallengeRegistrationDTO;
import es.deusto.sd.strava.dto.ChallengeResponseDTO;
import es.deusto.sd.strava.facade.StravaFacade;
import es.deusto.sd.strava.dto.AcceptanceConfirmationDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
@Tag(name = "Challenges", description = "Operations for managing user challenges")
public class ChallengeController {

    private final StravaFacade facade;

    @Autowired
    public ChallengeController(StravaFacade facade) {
        this.facade = facade;
    }

    /**
     * Extract the token 
     *
     * @param authorizationHeader Token header
     * @return Extracted token or null if not found
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @PostMapping
    @Operation(
        summary = "Setup Challenge",
        description = "Allows the user to set up a new challenge.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: Challenge set up successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid token provided"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided")
        }
    )
    public ResponseEntity<?> setupChallenge(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChallengeRegistrationDTO dto) {

        String token = extractToken(authorizationHeader);
        if (token == null) {
            return new ResponseEntity<>(
                Map.of("status", "Unauthorized", "message", "Invalid token."),
                HttpStatus.UNAUTHORIZED
            );
        }

        ChallengeDetailsDTO challenge = facade.setupChallenge(token, dto);
        if (challenge != null) {
            return new ResponseEntity<>(challenge, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(
                Map.of("status", "Bad Request", "message", "Invalid data."),
                HttpStatus.BAD_REQUEST
            );
        }
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
            @RequestParam(required = false) String sport,
            @Parameter(name = "startDate", description = "Start date for filtering challenges (optional)", example = "2023-01-01")
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(name = "endDate", description = "End date for filtering challenges (optional)", example = "2023-12-31")
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(name = "limit", description = "Limit for the number of challenges returned (optional)", example = "5")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {

        String token = extractToken(authorizationHeader);
        if (token == null) {
            return new ResponseEntity<>(
                Map.of("status", "Unauthorized", "message", "Invalid token."),
                HttpStatus.UNAUTHORIZED
            );
        }

        List<ChallengeResponseDTO> challenges = facade.getActiveChallenges(token, sport, startDate, endDate, limit);
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
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid request")
        }
    )
    public ResponseEntity<?> acceptChallenge(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(name = "challengeId", description = "ID of the challenge to accept", required = true)
            @RequestParam String challengeId) {

        String token = extractToken(authorizationHeader);
        if (token == null) {
            return new ResponseEntity<>(
                Map.of("status", "Unauthorized", "message", "Invalid token."),
                HttpStatus.UNAUTHORIZED
            );
        }

        AcceptanceConfirmationDTO confirmation = facade.acceptChallenge(token, challengeId);
        switch (confirmation.getStatus()) {
            case "OK":
                return ResponseEntity.ok(confirmation);
            case "Not Found":
                return new ResponseEntity<>(confirmation, HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(confirmation, HttpStatus.BAD_REQUEST);
        }
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
        String token = extractToken(authorizationHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "Unauthorized", "message", "Invalid or missing Authorization token"));
        }
        List<ChallengeResponseDTO> acceptedChallenges = facade.getAcceptedChallenges(token);
        if (acceptedChallenges.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
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
        String token = extractToken(authorizationHeader);
        ResponseEntity<?> response = (ResponseEntity<?>) facade.getChallengeStatus(token, null);
        return response;
    }
}

package es.deusto.sd.strava.controller;


import es.deusto.sd.strava.dto.AuthTokenDTO;
import es.deusto.sd.strava.dto.LoginRequestDTO;
import es.deusto.sd.strava.dto.LogoutConfirmationDTO;
import es.deusto.sd.strava.facade.StravaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User login and logout operations")
public class AuthController {

    private final StravaFacade facade;

    @Autowired
    public AuthController(StravaFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/login")
    @Operation(
        summary = "User Login",
        description = "Authenticates a user and provides an authentication token if successful.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: User authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials")
        }
    )
    public ResponseEntity<?> loginUser(
            @RequestBody LoginRequestDTO dto) {
        
        AuthTokenDTO token = facade.loginUser(dto.getEmail(), dto.getPassword(), dto.getAuthProviderName());
        
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return new ResponseEntity<>(
                    Map.of("status", "Unauthorized", "message", "Invalid credentials."),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @PostMapping("/logout")
    @Operation(
        summary = "User Logout",
        description = "Logs out the user by invalidating the provided authentication token.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: User logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing token")
        }
    )
    public ResponseEntity<?> logoutUser(
            @Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token")
            @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = extractToken(authorizationHeader);
        
        if (token == null) {
            return new ResponseEntity<>(
                    Map.of("status", "Unauthorized", "message", "Invalid token."),
                    HttpStatus.UNAUTHORIZED
            );
        }
        
        LogoutConfirmationDTO confirmation = facade.logoutUser(token);
        return ResponseEntity.ok(confirmation);
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


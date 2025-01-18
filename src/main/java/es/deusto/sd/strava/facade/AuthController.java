package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.LoginCredentialsDTO;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.external.AuthServiceProvider;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.UserService;
import es.deusto.sd.strava.util.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User login and logout operations")
public class AuthController {
	
	AuthService authService;
	UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping("/login")
	@Operation(summary = "User Login", description = "Authenticates a user and provides an authentication token if successful.", responses = {
			@ApiResponse(responseCode = "200", description = "OK: User authenticated successfully", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"1737220995413\"}"))),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Email not provided\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials") })
	public ResponseEntity<?> loginUser(
			@RequestParam(name = "authProviderName") String authProviderName,
			@RequestBody LoginCredentialsDTO dto) {
		
		if (dto.getEmail() == null || dto.getEmail().isEmpty()) return new ResponseEntity<>(Map.of("message", "Email not provided"), HttpStatus.BAD_REQUEST);
		if (dto.getPassword() == null || dto.getEmail().isEmpty()) return new ResponseEntity<>(Map.of("message", "Password not provided"), HttpStatus.BAD_REQUEST);
	
		boolean isAuthorized = authService.authenticateUser(dto.getEmail(), dto.getPassword(), AuthServiceProvider.valueOf(authProviderName.toUpperCase()));
        if (!isAuthorized) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        User user = userService.getUserByEmail(dto.getEmail());
        
        String token = authService.generateSessionToken(user.getId());
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
	}

	@PostMapping("/logout")
	@Operation(summary = "User Logout", description = "Logs out the user by invalidating the provided authentication token.", responses = {
			@ApiResponse(responseCode = "200", description = "OK: User logged out successfully"),
			@ApiResponse(responseCode = "404", description = "Not found: Could not find the session") })
	public ResponseEntity<?> logoutUser(
			@Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer 1737220995413") 
			@RequestHeader("Authorization") String authorizationHeader) {

		String token = TokenUtils.extractToken(authorizationHeader);
		User user = authService.getUserFromToken(token);
		if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Session not found

		authService.invalidateToken(token);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

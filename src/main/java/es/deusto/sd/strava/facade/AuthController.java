package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.LoginCredentialsDTO;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.UserService;
import es.deusto.sd.strava.util.TokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
			@ApiResponse(responseCode = "200", description = "OK: User authenticated successfully"),
			@ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials") })
	public ResponseEntity<?> loginUser(
			@RequestParam(name = "authProviderName") String authProviderName,
			@RequestBody LoginCredentialsDTO dto) {
		
		User user = userService.authenticateUser(dto.getEmail(), dto.getPassword(), authProviderName);
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        
        String token = authService.generateToken(user);
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
	}

	@PostMapping("/logout")
	@Operation(summary = "User Logout", description = "Logs out the user by invalidating the provided authentication token.", responses = {
			@ApiResponse(responseCode = "200", description = "OK: User logged out successfully"),
			@ApiResponse(responseCode = "404", description = "Not found: Could not find the session") })
	public ResponseEntity<?> logoutUser(
			@Parameter(name = "Authorization", description = "Bearer token", required = true, example = "Bearer your_token") 
			@RequestHeader("Authorization") String authorizationHeader) {

		String token = TokenUtils.extractToken(authorizationHeader);
		User user = authService.getUserFromToken(token);
		if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Session not found

		authService.invalidateToken(token);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

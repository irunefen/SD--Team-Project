package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.RegisterUserDTO;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.external.AuthServiceProvider;
import es.deusto.sd.strava.service.AuthService;
import es.deusto.sd.strava.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user registration and management")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(
        summary = "User Registration",
        description = "Registers a new user in the Strava system",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: User registered successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict: User already exists"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid data provided")
        }
    )
    public ResponseEntity<?> registerUser(
            @Parameter(name = "authProviderName", description = "Name of the authentication provider", required = true, example = "Google")
            @RequestParam("authProviderName") String authProviderName,
            @RequestBody RegisterUserDTO dto) {
    	
    	// Check if there is already a Strava user with the same email
    	if(userService.getUserRepository().existsByEmail(dto.getEmail())){
    		return new ResponseEntity<>(Map.of("conflict", "User already exists."), HttpStatus.CONFLICT);
    	}
    	
    	AuthServiceProvider authProvider = AuthServiceProvider.valueOf(authProviderName.toUpperCase()); 
        
    	// Check if the email corresponds to an account in the provider's server
        if(!authService.isEmailRegistered(dto.getEmail(), authProvider)){
        	return new ResponseEntity<>(Map.of("conflict", "Account does not exist in " + authProviderName), HttpStatus.CONFLICT);
        }

        User user = userService.createUser(dto);
		if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Some invalid data provided
		
        
        return new ResponseEntity<>(Map.of("userId", user.getId()), HttpStatus.CREATED);
    }
}

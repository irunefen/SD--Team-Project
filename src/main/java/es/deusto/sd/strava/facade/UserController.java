package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.RegisterUserDTO;
import es.deusto.sd.strava.entity.User;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
        
        if(userService.isEmailRegistered(dto.getEmail(), authProviderName)){
        	return new ResponseEntity<>(Map.of("conflict", "Email already registered with this provider."), HttpStatus.CONFLICT);
        }

        User user = userService.createUser(dto, authProviderName);
		if (user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Some invalid data provided
		
        
        return new ResponseEntity<>(Map.of("userId", user.getUserId()), HttpStatus.CREATED);
    }
}

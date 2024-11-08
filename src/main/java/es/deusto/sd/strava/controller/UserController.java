package es.deusto.sd.strava.controller;

import es.deusto.sd.strava.dto.RegisterUserDTO;
import es.deusto.sd.strava.dto.RegistrationStatusDTO;
import es.deusto.sd.strava.facade.StravaFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user registration and management")
public class UserController {

    private final StravaFacade facade;

    @Autowired
    public UserController(StravaFacade facade) {
        this.facade = facade;
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
    public ResponseEntity<RegistrationStatusDTO> registerUser(
            @Parameter(name = "authProviderName", description = "Name of the authentication provider", required = true, example = "Google")
            @RequestHeader("authProviderName") String authProviderName,
            @RequestBody RegisterUserDTO dto) {
        
        RegistrationStatusDTO status = facade.registerUser(dto, authProviderName);

        if ("Created".equals(status.getStatus())) {
            return new ResponseEntity<>(status, HttpStatus.CREATED);
        } else if ("Conflict".equals(status.getStatus())) {
            return new ResponseEntity<>(status, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
    }
}

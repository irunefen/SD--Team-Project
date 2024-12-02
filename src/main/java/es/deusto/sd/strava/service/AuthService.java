package es.deusto.sd.strava.service;

import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.external.AuthServiceGatewayFactory;
import es.deusto.sd.strava.external.AuthServiceProvider;
import es.deusto.sd.strava.external.IAuthServiceGateway;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing authentication and token management.
 */
@Service
public class AuthService {
	
    // Map of user tokens
    private Map<String, User> tokens = new HashMap<>();
    
    /**
     * Autenticates a user.
     *
     * @param email            User email
     * @param password         Password
     * @param authProviderName Authentication provider
     * @return true if user is authorized, false otherwise
     */
    public boolean authenticateUser(String email, String password, AuthServiceProvider authProvider) {
    	return AuthServiceGatewayFactory.createAuthServiceGateway(authProvider).authenticateUser(email, password).orElse(false);
    }
    
    /**
     * Verifies if an email is already registered.
     *
     * @param email            email to verify.
     * @param authProviderName provider name
     * @return true if the email is registered, false otherwise
     */
    //TODO
    public boolean isEmailRegistered(String email, AuthServiceProvider authProvider) {
    	return AuthServiceGatewayFactory.createAuthServiceGateway(authProvider).isEmailRegistered(email).orElse(false);
    }

    /**
     * Generates a token for a user.
     *
     * @param user User authenticated.
     * @return Token generated.
     */
    public String generateToken(User user) {
        String token = String.valueOf(Instant.now().toEpochMilli());
        tokens.put(token, user);
        return token;
    }

    /**
     * Retrieves the associated user.
     *
     * @param token Token to validate
     * @return Associated user if token is valid, null otherwise.
     */
    public User getUserFromToken(String token) {
    	if (token == null) return null;
        return tokens.get(token);
    }

    /**
     * Invalidates a token
     *
     * @param token Token to invalidate
     */
    public void invalidateToken(String token) {
        tokens.remove(token);
    }
}

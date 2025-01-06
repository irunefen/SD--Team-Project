package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dao.UserRepository;
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
	
    // Map of user tokens to user ids
    private Map<String, Long> tokenToUserId = new HashMap<>();
    
    private final UserRepository userRepository;
    
	public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
	}
	
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
     * Generates a token for a user session.
     *
     * @param user User authenticated.
     * @return Token generated.
     */
    public String generateSessionToken(Long userId) {
        String token = String.valueOf(Instant.now().toEpochMilli());
        tokenToUserId.put(token, userId);
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
    	Long userId = tokenToUserId.get(token);
    	if (userId == null) return null;
    	return userRepository.findById(userId).orElse(null);
    }

    /**
     * Invalidates a token
     *
     * @param token Token to invalidate
     */
    public void invalidateToken(String token) {
    	tokenToUserId.remove(token);
    }
}

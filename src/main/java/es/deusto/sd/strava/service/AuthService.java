package es.deusto.sd.strava.service;

import es.deusto.sd.strava.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing authentication and token management.
 */
@Service
public class AuthService {

    // Map of user tokens
    private Map<String, User> tokens = new HashMap<>();

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
     * Validates a token
     *
     * @param token Token to validate
     * @return Asociated user if token is valid, null otherwise.
     */
    public User validateToken(String token) {
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

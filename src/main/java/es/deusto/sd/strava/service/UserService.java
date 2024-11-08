package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dto.RegisterUserDTO;
import es.deusto.sd.strava.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for managing users.
 */
@Service
public class UserService {

    // User data is stored in memory 
    private Map<String, User> usersByEmail = new HashMap<>();
    private Map<String, User> usersById = new HashMap<>();

    /**
     * Crate a new user
     *
     * @param dto user data.
     * @return user created or null if there is a conflict
     */
    public User createUser(RegisterUserDTO dto, String authProviderName) {
        if (usersByEmail.containsKey(dto.getEmail())) {
            return null; // Conflict
        }

        String userId = UUID.randomUUID().toString();
        User user = new User(userId, dto.getEmail(), dto.getName(), dto.getBirthdate(),
                dto.getWeight(), dto.getHeight(), dto.getMaxHeartRate(),
                dto.getRestHeartRate(), authProviderName);

        usersByEmail.put(dto.getEmail(), user);
        usersById.put(userId, user);
        return user;
    }

    /**
     * Autenticates a user.
     *
     * @param email            User email
     * @param password         Password
     * @param authProviderName Name of the authentication provider
     * @return User if authenticated, null otherwise.
     */
    public User authenticateUser(String email, String password, String authProviderName) {
        User user = usersByEmail.get(email);
        if (user != null && user.getAuthProviderName().equalsIgnoreCase(authProviderName)) {
            // Simulate authentication --> Delegate to Google/Facebook
            return user;
        }
        return null;
    }

    /**
     * Get a user by id
     *
     * @param userId id of the user
     * @return user or null if not found
     */
    public User getUserById(String userId) {
        return usersById.get(userId);
    }

    /**
     * Verifies if an email is already registered.
     *
     * @param email            email to verify.
     * @param authProviderName provider name
     * @return true if the email is registered, false otherwise
     */
    public boolean isEmailRegistered(String email, String authProviderName) {
        User user = usersByEmail.get(email);
        if (user == null) {
            // simulate verification --> Delegate to Google/Facebook
            // in this prototype we assume that the email is not registered if it doesn't exist
            return false;
        }
        return user.getAuthProviderName().equalsIgnoreCase(authProviderName);
    }
}

package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dao.UserRepository;
import es.deusto.sd.strava.dto.RegisterUserDTO;
import es.deusto.sd.strava.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing users.
 */
@Service
public class UserService {
	
	private final UserRepository userRepository;
    
	public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

    /**
     * Crate a new user
     *
     * @param dto user data.
     * @return user created or null if there is a conflict
     */
    public User createUser(RegisterUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return null; // Conflict
        }

        User user = new User(dto.getEmail(), dto.getName(), dto.getBirthdate(), dto.getWeight(), dto.getHeight(), dto.getMaxHeartRate(), dto.getRestHeartRate());

        userRepository.save(user);
        return user;
    }

    /**
     * Get a user by id
     *
     * @param userId id of the user
     * @return user or null if not found
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
	/**
	 * Get a user by email
	 *
	 * @param email email of the user
	 * @return user or null if not found
	 */
    public User getUserByEmail(String email) {
    	return userRepository.findByEmail(email).orElse(null);
    }
}

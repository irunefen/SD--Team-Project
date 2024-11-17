package es.deusto.sd.strava.util;

public class TokenUtils {
	/**
     * Extract the token 
     *
     * @param authorizationHeader Token header
     * @return Extracted token or null if not found
     */
    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    
}

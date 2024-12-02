package es.deusto.sd.strava.external;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GoogleAuthServiceGateway implements IAuthServiceGateway {

	private static String API_URL;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
	public GoogleAuthServiceGateway() {
		this.httpClient = HttpClient.newHttpClient();
		this.objectMapper = new ObjectMapper();
	}
	
	@Value("${auth.google.url}")
	public void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }
	
	
	@Override
	public Optional<Boolean> isEmailRegistered(String email) {
		String url = API_URL + "/users/exists?email=" + email;
		
		try {
            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and obtain the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        	
            if (response.statusCode() != 200) return Optional.empty();
            
        	// If response is OK, parse the response body
			@SuppressWarnings("unchecked")
			Boolean exists = ((Map<String, Boolean>) (objectMapper.readValue(response.body(), Map.class))).get("exists");
			return Optional.of(exists);			

        } catch (Exception ex) {
        	return Optional.empty();
        }
	}

	@Override
	public Optional<Boolean> authenticateUser(String email, String password) {
		String url = API_URL + "/login";
		
		try {
            // Create the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                    .build();

            // Send the request and obtain the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        	
            if (response.statusCode() != 200) return Optional.empty();
            
        	// If response is OK, parse the response body
			@SuppressWarnings("unchecked")
			Boolean exists = ((Map<String, Boolean>) (objectMapper.readValue(response.body(), Map.class))).get("authorized");
			return Optional.of(exists);			

        } catch (Exception ex) {
        	return Optional.empty();
        }
	}

}

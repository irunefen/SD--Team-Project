package es.deusto.sd.strava.external;

import java.util.Optional;

public interface IAuthServiceGateway {
	public Optional<Boolean> isEmailRegistered(String email);
	public Optional<Boolean> authenticateUser(String email, String password);
}

package es.deusto.sd.strava.external;

public class AuthServiceGatewayFactory {	
	public static IAuthServiceGateway createAuthServiceGateway(AuthServiceProvider provider) {
		switch (provider) {
		case GOOGLE:
			return new GoogleAuthServiceGateway();
		case FACEBOOK:
			return new FacebookAuthServiceGateway();
		default:
			return null;
		}
	}
}

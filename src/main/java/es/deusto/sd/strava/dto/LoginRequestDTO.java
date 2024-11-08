package es.deusto.sd.strava.dto;

public class LoginRequestDTO {
    private String email;
    private String password;
    private String authProviderName;


    public LoginRequestDTO() {}
    
	public LoginRequestDTO(String email, String password, String authProviderName) {
		this.email = email;
		this.password = password;
		this.authProviderName = authProviderName;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getAuthProviderName() {
		return authProviderName;
	}


	public void setAuthProviderName(String authProviderName) {
		this.authProviderName = authProviderName;
	}

   
}

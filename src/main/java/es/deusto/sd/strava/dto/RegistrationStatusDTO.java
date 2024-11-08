package es.deusto.sd.strava.dto;

public class RegistrationStatusDTO {
    private String status;
    private String message;
    private String userId;

    public RegistrationStatusDTO() {}

    public RegistrationStatusDTO(String status, String message, String userId) {
        this.status = status;
        this.message = message;
        this.userId = userId;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    
    
    
}
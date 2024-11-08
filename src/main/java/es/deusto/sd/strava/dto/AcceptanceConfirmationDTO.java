package es.deusto.sd.strava.dto;

public class AcceptanceConfirmationDTO {
    private String status;
    private String message;
    private String challengeId;

    public AcceptanceConfirmationDTO() {}

    public AcceptanceConfirmationDTO(String status, String message, String challengeId) {
        this.status = status;
        this.message = message;
        this.challengeId = challengeId;
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

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

   
}
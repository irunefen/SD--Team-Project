package es.deusto.sd.strava.entity;


public class ChallengeProgress {
    private String challengeId;
    private Float currentProgress;
    private Float percentageAchieved;
    private String userId;


    public ChallengeProgress() {}

    public ChallengeProgress(String challengeId, Float currentProgress, Float percentageAchieved, String userId) {
        this.challengeId = challengeId;
        this.currentProgress = currentProgress;
        this.percentageAchieved = percentageAchieved;
        this.userId = userId;
    }

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public Float getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(Float currentProgress) {
		this.currentProgress = currentProgress;
	}

	public Float getPercentageAchieved() {
		return percentageAchieved;
	}

	public void setPercentageAchieved(Float percentageAchieved) {
		this.percentageAchieved = percentageAchieved;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    
}

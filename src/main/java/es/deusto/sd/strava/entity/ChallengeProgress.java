package es.deusto.sd.strava.entity;


public class ChallengeProgress {
    private Long challengeId;
    private Float currentProgress;
    private Float percentageAchieved;
    private Long userId;


    public ChallengeProgress() {}

    public ChallengeProgress(Long challengeId, Float currentProgress, Float percentageAchieved, Long userId) {
        this.challengeId = challengeId;
        this.currentProgress = currentProgress;
        this.percentageAchieved = percentageAchieved;
        this.userId = userId;
    }

	public Long getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(Long challengeId) {
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    
}

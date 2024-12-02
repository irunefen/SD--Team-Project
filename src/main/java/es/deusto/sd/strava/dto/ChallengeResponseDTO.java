package es.deusto.sd.strava.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ChallengeResponseDTO {
    private Long challengeId;
    private String name;
    private String sport;
    private Float targetDistance;
    private LocalTime targetTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;


    public ChallengeResponseDTO() {}

    public ChallengeResponseDTO(Long challengeId, String name, String sport, Float targetDistance,
    		LocalTime targetTime, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt) {
        this.challengeId = challengeId;
        this.name = name;
        this.sport = sport;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }

	public Long getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public Float getTargetDistance() {
		return targetDistance;
	}

	public void setTargetDistance(Float targetDistance) {
		this.targetDistance = targetDistance;
	}

	public LocalTime getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(LocalTime targetTime) {
		this.targetTime = targetTime;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
}

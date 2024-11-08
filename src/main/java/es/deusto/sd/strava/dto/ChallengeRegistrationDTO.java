package es.deusto.sd.strava.dto;

import java.time.Duration;
import java.time.LocalDate;

public class ChallengeRegistrationDTO {
    private String name;
    private String sport;
    private Float targetDistance; // Optional
    private Duration targetTime; // Optional
    private LocalDate startDate;
    private LocalDate endDate;

    public ChallengeRegistrationDTO(String name, String sport, Float targetDistance, Duration targetTime,
			LocalDate startDate, LocalDate endDate) {
		super();
		this.name = name;
		this.sport = sport;
		this.targetDistance = targetDistance;
		this.targetTime = targetTime;
		this.startDate = startDate;
		this.endDate = endDate;
	}


	public ChallengeRegistrationDTO() {}


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


	public Duration getTargetTime() {
		return targetTime;
	}


	public void setTargetTime(Duration targetTime) {
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

    
}
package es.deusto.sd.strava.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ChallengeRegistrationDTO {
    private String name;
    private String sport;
    private Float targetDistance; // Optional
    private Float targetTime; // Optional
    private LocalDate startDate;
    private LocalDate endDate;

    public ChallengeRegistrationDTO(String name, String sport, Float targetDistance, Float targetTime,
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


	public Float getTargetTime() {
		return targetTime;
	}


	public void setTargetTime(Float targetTime) {
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
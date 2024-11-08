package es.deusto.sd.strava.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class TrainingSessionDTO {
    private String title;
    private String sport;
    private Float distance; // km
    private LocalDate startDate;
    private LocalTime startTime;
    private Duration duration;

    public TrainingSessionDTO() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

    
}
package es.deusto.sd.strava.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

public class TrainingSessionResponseDTO {
    private Long id;
    private String title;
    private String sport;
    private Float distance;
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    private Integer duration;
    private LocalDateTime createdAt;

    public TrainingSessionResponseDTO() {}

    public TrainingSessionResponseDTO(Long sessionId, String title, String sport, Float distance,
                                      LocalDate startDate, LocalTime startTime, Integer duration, LocalDateTime createdAt) {
        this.id = sessionId;
        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.createdAt = createdAt;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long sessionId) {
		this.id = sessionId;
	}

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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
}
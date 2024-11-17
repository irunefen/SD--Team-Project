package es.deusto.sd.strava.entity;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class TrainingSession {
    private String sessionId;
    private String userId;
    private String title;
    private String sport;
    private float distance; // km
    private LocalDate startDate;
    private LocalTime startTime;
    private Integer duration; //seconds
    private LocalDateTime createdAt;


    public TrainingSession() {}

    public TrainingSession(String sessionId, String userId, String title, String sport, float distance,
                           LocalDate startDate, LocalTime startTime, Integer duration, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.title = title;
        this.sport = sport;
        this.distance = distance;
        this.startDate = startDate;
        this.startTime = startTime;
        this.duration = duration;
        this.createdAt = createdAt;
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
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


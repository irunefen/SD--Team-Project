package es.deusto.sd.strava.entity;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class TrainingSession {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "user_id", nullable = false)
    private Long userId;
	
	@Column(name = "title", nullable = false)
    private String title;
	
	@Column(name = "sport", nullable = false)
    private String sport;
	
	@Column(name = "distance", nullable = false)
    private float distance; // km
	
	@Column(name = "start_date", nullable = false)
    private LocalDate startDate;
	
	@Column(name = "start_time", nullable = false)
    private LocalTime startTime;
	
	@Column(name = "duration", nullable = false)
    private Integer duration; //seconds
	
	@Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public TrainingSession() {}

    public TrainingSession(Long userId, String title, String sport, float distance,
                           LocalDate startDate, LocalTime startTime, Integer duration, LocalDateTime createdAt) {
        this.userId = userId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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


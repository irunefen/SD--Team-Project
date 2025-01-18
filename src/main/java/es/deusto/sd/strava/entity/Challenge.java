package es.deusto.sd.strava.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Challenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
    private String name;
	
	@Column(nullable = false)
    private String sport;
    
	@Column(name = "target_distance")
    private Float targetDistance; // km, optional
    
	@Column(name = "target_time")
    private Integer targetTime; // optional
    
	@Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
	@Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
	@Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
	@Column(name = "creator_id", nullable = false)
    private Long creatorId;


    public Challenge() {}

    public Challenge(String name, String sport, Float targetDistance, Integer targetTime,
                     LocalDate startDate, LocalDate endDate, LocalDateTime createdAt, Long creatorId) {
        this.name = name;
        this.sport = sport;
        this.targetDistance = targetDistance;
        this.targetTime = targetTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.creatorId = creatorId;
    }

	public Long getId() {
		return id;
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

	public Integer getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(Integer targetTime) {
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

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

    
}


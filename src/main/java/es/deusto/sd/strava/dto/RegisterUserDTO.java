package es.deusto.sd.strava.dto;

import java.time.LocalDate;

public class RegisterUserDTO {
    private String email;
    private String name;
    private LocalDate birthdate;
    private Float weight; // Optional
    private Float height; // Optional
    private Integer maxHeartRate; // Optional
    private Integer restHeartRate; // Optional

    public RegisterUserDTO() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Integer getMaxHeartRate() {
		return maxHeartRate;
	}

	public void setMaxHeartRate(Integer maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}

	public Integer getRestHeartRate() {
		return restHeartRate;
	}

	public void setRestHeartRate(Integer restHeartRate) {
		this.restHeartRate = restHeartRate;
	}

 
}